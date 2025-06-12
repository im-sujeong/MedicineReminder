package com.sujeong.pillo.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sujeong.pillo.domain.result.AlarmError
import com.sujeong.pillo.domain.result.Result
import com.sujeong.pillo.domain.usecase.GetMedicineAlarmUseCase
import com.sujeong.pillo.domain.usecase.SaveMedicineAlarmUseCase
import com.sujeong.pillo.presentation.home.model.CalendarDateModel
import com.sujeong.pillo.presentation.home.model.CalendarWeekModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMedicineAlarmUseCase: GetMedicineAlarmUseCase,
    private val saveMedicineAlarmUseCase: SaveMedicineAlarmUseCase
): ViewModel() {
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

            is HomeEvent.DeleteMedicineAlarm -> deleteMedicineAlarm()
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
            ).let { result ->
                when(result) {
                    is Result.Success<*> -> {

                    }

                    is Result.Error -> {
                        if(result.error is AlarmError) {

                        }
                    }
                }
            }
        }
    }

    private fun deleteMedicineAlarm() {

    }
}