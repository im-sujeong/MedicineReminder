package com.sujeong.pillo.presentation.home

import androidx.lifecycle.ViewModel
import com.sujeong.pillo.R
import com.sujeong.pillo.common.ResourceProvider
import com.sujeong.pillo.common.extension.toString
import com.sujeong.pillo.presentation.home.model.CalendarDateModel
import com.sujeong.pillo.presentation.home.model.CalendarWeekModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider
): ViewModel() {
    private val _state = MutableStateFlow(
        HomeState(
            weeks = getWeekData(),
            selectedDate = resourceProvider.getString(R.string.label_today)
        )
    )
    val state = _state.asStateFlow()

    private fun getWeekData(): List<CalendarWeekModel> {
        val baseDate = LocalDate.now()

        val startDate = baseDate
            .with(DayOfWeek.MONDAY)
            .minusDays(7)

        val week = (0..2).map {
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

        return week
    }

    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.SelectedDate -> selectedDate(
                selectedDate = event.date
            )
        }
    }

    private fun selectedDate(
        selectedDate: CalendarDateModel
    ) {
        _state.value = _state.value.copy(
            weeks = _state.value.weeks.map { week ->
                week.copy(
                    dates = week.dates.map {
                        it.copy(
                            isSelected = it.date.isEqual(selectedDate.date)
                        )
                    }
                )
            },
            selectedDate = if(selectedDate.isToday) {
                resourceProvider.getString(R.string.label_today)
            } else {
                selectedDate.date.toString(
                    resourceProvider.getString(R.string.format_date_mm_dd)
                )
            }
        )
    }
}