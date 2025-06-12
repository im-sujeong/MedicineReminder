package com.sujeong.pillo.presentation.home

import androidx.lifecycle.viewModelScope
import com.sujeong.pillo.R
import com.sujeong.pillo.common.ResourceProvider
import com.sujeong.pillo.domain.result.AlarmError
import com.sujeong.pillo.domain.result.onError
import com.sujeong.pillo.domain.result.onSuccess
import com.sujeong.pillo.domain.usecase.DeleteMedicineAlarmUseCase
import com.sujeong.pillo.domain.usecase.GetMedicineAlarmUseCase
import com.sujeong.pillo.domain.usecase.SaveMedicineAlarmUseCase
import com.sujeong.pillo.presentation.base.BaseViewModel
import com.sujeong.pillo.presentation.home.model.CalendarDateModel
import com.sujeong.pillo.presentation.home.model.CalendarWeekModel
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
    private val getMedicineAlarmUseCase: GetMedicineAlarmUseCase,
    private val saveMedicineAlarmUseCase: SaveMedicineAlarmUseCase,
    private val deleteMedicineAlarmUseCase: DeleteMedicineAlarmUseCase
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
                    getMedicineAlarmUseCase(it)
                }.collect { result ->
                    result.onSuccess {
                        _state.value = _state.value.copy(
                            medicineAlarm = it
                        )
                    }.onError {
                        _state.value = _state.value.copy(
                            medicineAlarm = null
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
            is HomeEvent.SelectedDate -> selectedDate(
                selectedDate = event.date
            )

            HomeEvent.AddMedicineAlarm -> saveMedicineAlarm()

            is HomeEvent.DeleteMedicineAlarm -> deleteMedicineAlarm(event.id)
        }
    }

    private fun selectedDate(
        selectedDate: LocalDate
    ) {
        _state.value = _state.value.copy(
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

    private fun saveMedicineAlarm() {
        viewModelScope.launch {
            saveMedicineAlarmUseCase(
                alarmDateTime = _state.value.selectedDate.date.atTime(12, 0)
            ).onError {
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

    private fun deleteMedicineAlarm(alarmId: Long) {
        viewModelScope.launch {
            deleteMedicineAlarmUseCase(alarmId)
                .onSuccess {
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