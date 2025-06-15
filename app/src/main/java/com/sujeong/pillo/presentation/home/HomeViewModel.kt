package com.sujeong.pillo.presentation.home

import androidx.lifecycle.viewModelScope
import com.sujeong.pillo.R
import com.sujeong.pillo.common.extension.millis
import com.sujeong.pillo.common.manager.MedicineAlarmManager
import com.sujeong.pillo.common.provider.ResourceProvider
import com.sujeong.pillo.domain.result.AlarmError
import com.sujeong.pillo.domain.result.onError
import com.sujeong.pillo.domain.result.onSuccess
import com.sujeong.pillo.domain.usecase.DeleteMedicineUseCase
import com.sujeong.pillo.domain.usecase.GetMedicineUseCase
import com.sujeong.pillo.domain.usecase.SaveMedicineUseCase
import com.sujeong.pillo.common.base.BaseViewModel
import com.sujeong.pillo.presentation.home.model.CalendarDateModel
import com.sujeong.pillo.presentation.home.model.CalendarWeekModel
import com.sujeong.pillo.presentation.home.model.PermissionDialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val getMedicineUseCase: GetMedicineUseCase,
    private val saveMedicineUseCase: SaveMedicineUseCase,
    private val deleteMedicineUseCase: DeleteMedicineUseCase,
    private val medicineAlarmManager: MedicineAlarmManager
): BaseViewModel<HomeUiEffect>() {
    private val _state = MutableStateFlow(
        HomeState(
            weeks = getWeeks(),
            selectedDate = CalendarDateModel(
                date = LocalDate.now(),
                isToday = true,
                isSelected = true
            )
        )
    )
    val state get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.map { it.selectedDate.date }
                .distinctUntilChanged()
                .flatMapLatest {
                    getMedicineUseCase(it)
                }.collect { result ->
                    result.onSuccess {
                        _state.value = _state.value.copy(
                            medicine = it
                        )
                    }.onError {
                        _state.value = _state.value.copy(
                            medicine = null
                        )
                    }
                }
        }
    }

    private fun getWeeks(): List<CalendarWeekModel> {
        val baseDate = LocalDate.now()

        val startDate = baseDate
            .with(DayOfWeek.MONDAY)
            .minusDays(7)

        val weeks = (0..2).map {
            var hasToday = false
            val offset = it * 7

            val dates = (0 .. 6).map {
                val date = startDate.plusDays(it.toLong() + offset)
                val isToday = date.isEqual(LocalDate.now())

                hasToday = isToday

                CalendarDateModel(
                    date = date,
                    isToday = isToday,
                    isSelected = isToday
                )
            }

            CalendarWeekModel(
                dates = dates
            )
        }

        return weeks
    }

    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.RequestNotificationPermission -> requestNotificationPermission(event.isCancel)

            is HomeEvent.OnNotificationPermissionResult -> onNotificationPermissionResult(event.isGranted)

            is HomeEvent.RequestSystemAlertWindowPermission -> requestSystemAlertWindowPermission(event.isCancel)

            is HomeEvent.ChangedWeekPage -> changedWeekPage(event.page)

            is HomeEvent.SelectedDate -> selectedDate(
                selectedDate = event.date
            )

            HomeEvent.AddMedicine -> addMedicine()

            is HomeEvent.DeleteMedicine -> deleteMedicine(event.id)
        }
    }

    private fun requestNotificationPermission(isCancel: Boolean) {
        if(isCancel) {
            _state.value = _state.value.copy(
                showPermissionDialogType = PermissionDialogType.SYSTEM_ALERT_WINDOW
            )
        } else {
            onUiEffect(
                HomeUiEffect.RequestNotificationPermission
            )
        }
    }

    private fun onNotificationPermissionResult(isGranted: Boolean) {
        _state.value = _state.value.copy(
            showPermissionDialogType = PermissionDialogType.SYSTEM_ALERT_WINDOW
        )

        onUiEffect(
            HomeUiEffect.ShowMessage(
                if(isGranted) {
                    resourceProvider.getString(
                        R.string.snack_bar_granted_notification_permission
                    )
                } else {
                    resourceProvider.getString(
                        R.string.snack_bar_denied_notification_permission
                    )
                }
            )
        )
    }

    private fun requestSystemAlertWindowPermission(isCancel: Boolean) {
        _state.value = _state.value.copy(
            showPermissionDialogType = PermissionDialogType.NONE
        )

        if(!isCancel) {
            onUiEffect(
                HomeUiEffect.RequestSystemAlertWindowPermission
            )
        }
    }

    private fun changedWeekPage(page: Int) {
        if(page > _state.value.currentWeekPage) {
            selectedDate(
                _state.value.weeks[page].dates.first().date,
                currentWeekPage = page
            )
        } else if(page < _state.value.currentWeekPage) {
            selectedDate(
                _state.value.weeks[page].dates.last().date,
                currentWeekPage = page
            )
        }
    }

    private fun selectedDate(
        selectedDate: LocalDate,
        currentWeekPage: Int = _state.value.currentWeekPage,
    ) {
        _state.value = _state.value.copy(
            currentWeekPage = currentWeekPage,
            weeks = _state.value.weeks.map { week ->
                week.copy(
                    dates = week.dates.map {
                        it.copy(
                            isSelected = it.date.isEqual(selectedDate)
                        )
                    }
                )
            },
            selectedDate = CalendarDateModel(
                date = selectedDate,
                isToday = selectedDate.isEqual(LocalDate.now()),
                isSelected = true
            )
        )
    }

    private fun addMedicine() {
        viewModelScope.launch {
            val alarmDateTime = _state.value.selectedDate.date.atTime(12, 0)

            saveMedicineUseCase(
                alarmDateTime = alarmDateTime
            ).onSuccess{ medicineId ->
                medicineAlarmManager.setAlarm(
                    medicineId = medicineId,
                    alarmDateTime = alarmDateTime.millis()
                )
            }.onError {
                when(it) {
                    is AlarmError.DuplicatedAlarmDate -> {
                        onUiEffect(
                            HomeUiEffect.ShowMessage(
                                resourceProvider.getString(
                                    R.string.snack_bar_msg_duplicate_alarm_date
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    private fun deleteMedicine(medicineId: Long) {
        viewModelScope.launch {
            deleteMedicineUseCase(medicineId)
                .onSuccess {
                    medicineAlarmManager.cancelAlarm(medicineId)

                    onUiEffect(
                        HomeUiEffect.ShowMessage(
                            resourceProvider.getString(
                                R.string.snack_bar_msg_deleted
                            )
                        )
                    )
                }
        }
    }
}