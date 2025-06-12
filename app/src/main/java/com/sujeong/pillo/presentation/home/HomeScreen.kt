package com.sujeong.pillo.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sujeong.pillo.R
import com.sujeong.pillo.common.extension.toString
import com.sujeong.pillo.domain.model.MedicineAlarm
import com.sujeong.pillo.domain.model.enums.AlarmStatus
import com.sujeong.pillo.presentation.home.model.CalendarDateModel
import com.sujeong.pillo.presentation.home.model.CalendarWeekModel
import com.sujeong.pillo.ui.component.alarm.MedicineAlarmItem
import com.sujeong.pillo.ui.component.button.SmallButton
import com.sujeong.pillo.ui.component.calendar.DateText
import com.sujeong.pillo.ui.component.calendar.DayWeekRow
import com.sujeong.pillo.ui.theme.PilloTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    HomeContainer(
        onEvent = viewModel::onEvent
    ) { innerPadding ->
        HomeColumn(innerPadding) {
            HomeHeader(state.value.selectedDate)
            HomeDayWeekRow()
            HomeDatePager(
                weeks = state.value.weeks,
                selectedPage = state.value.selectedPage,
                onEvent = viewModel::onEvent
            )

            HomeMedicineAlarmContent(
                medicineAlarm = state.value.medicineAlarm,
                onEvent = viewModel::onEvent
            )
        }
    }
}

@Composable
private fun HomeContainer(
    onEvent: (HomeEvent) -> Unit,
    content: @Composable (innerPadding: PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PilloTheme.colors.surface,
        floatingActionButton = {
            SmallButton(
                text = stringResource(R.string.btn_add_alarm),
                onClick = {
                    onEvent(HomeEvent.AddMedicineAlarm)
                },
                icon = Icons.Rounded.Add,
                shadowElevation = 2.dp
            )
        },
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Composable
private fun HomeColumn(
    innerPadding: PaddingValues,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(
                top = 16.dp,
            )
    ) {
        content()
    }
}

@Composable
private fun HomeHeader(
    selectedDate: CalendarDateModel
) {
    Text(
        text = if(selectedDate.isToday) {
            stringResource(R.string.label_today)
        } else {
            selectedDate.date.toString(
                stringResource(R.string.format_date_M_d)
            )
        },
        color = PilloTheme.colors.onSurface,
        style = PilloTheme.typography.titleLarge,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}

@Composable
private fun HomeDayWeekRow() {
    DayWeekRow(
        modifier = Modifier
            .padding(
                horizontal = 16.dp
            )
            .padding(
                top = 24.dp
            )
    )
}

@Composable
private fun HomeDatePager(
    weeks: List<CalendarWeekModel>,
    selectedPage: Int,
    onEvent: (HomeEvent) -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = selectedPage
    ) {
        weeks.size
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.padding(top = 4.dp)
    ) { currentPage ->
        HomeDateRow(
            dates = weeks[currentPage].dates,
            modifier = Modifier.padding(
                horizontal = 16.dp
            ),
            onclickDate = { date ->
                onEvent(HomeEvent.SelectedDate(date))
            }
        )
    }
}

@Composable
private fun HomeDateRow(
    dates: List<CalendarDateModel>,
    onclickDate: (date: LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        dates.forEach { date ->
            DateText(
                date = date.date,
                isToday = date.isToday,
                isSelected = date.isSelected,
                modifier = Modifier.weight(1f),
                onClickDate = onclickDate
            )
        }
    }
}

@Composable
private fun HomeNoDateText() {
    Text(
        text = stringResource(R.string.msg_no_alarm),
        color = PilloTheme.colors.onSurfaceVariant,
        style = PilloTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 64.dp,
            ).padding(
                horizontal = 20.dp
            ),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun HomeMedicineAlarmContent(
    medicineAlarm: MedicineAlarm?,
    onEvent: (HomeEvent) -> Unit
) {
    medicineAlarm?.let {
        var alarmStatusText = ""
        var alarmStatusColor = PilloTheme.colors.onSurfaceVariant

        when(medicineAlarm.alarmStatus) {
            AlarmStatus.SCHEDULED -> {
                alarmStatusText = stringResource(R.string.label_alarm_status_scheduled)
                alarmStatusColor = PilloTheme.colors.onSurfaceVariant
            }
            AlarmStatus.TAKEN -> {
                alarmStatusText = stringResource(R.string.label_alarm_status_taken)
                alarmStatusColor = PilloTheme.colors.primary
            }
            AlarmStatus.SKIPPED -> {
                alarmStatusText = stringResource(R.string.label_alarm_status_skipped)
                alarmStatusColor = PilloTheme.colors.error
            }
        }

        MedicineAlarmItem(
            alarmTime = it.alarmDateTime.toString(
                stringResource(R.string.format_time_HH_mm)
            ),
            title = it.title,
            alarmStatusText = alarmStatusText,
            alarmStatusColor = alarmStatusColor,
            onDeleted = {
                onEvent(
                    HomeEvent.DeleteMedicineAlarm(it.id)
                )
            },
            modifier = Modifier.padding(
                horizontal = 20.dp
            ).padding(
                top = 24.dp
            )
        )
    } ?: run {
        HomeNoDateText()
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    PilloTheme {
        val today = LocalDate.now()
        val baseDate = today.with(DayOfWeek.MONDAY)

        HomeContainer(
            onEvent = { }
        ) { innerPadding ->
            HomeColumn(innerPadding) {
                HomeHeader(
                    selectedDate = CalendarDateModel(
                        date = today,
                        isToday = true,
                        isSelected = true
                    )
                )
                HomeDayWeekRow()
                HomeDatePager(
                    weeks = listOf(
                        CalendarWeekModel(
                            dates = (0..6).map {
                                val date = baseDate.plusDays(it.toLong())

                                CalendarDateModel(
                                    date = date,
                                    isToday = today.isEqual(date),
                                    isSelected = today.isEqual(date)
                                )
                            }
                        )
                    ),
                    selectedPage = 1,
                    onEvent = {}
                )

                HomeMedicineAlarmContent(
                    medicineAlarm = MedicineAlarm(
                        id = 1,
                        title = "약",
                        alarmDateTime = LocalDateTime.now(),
                        alarmStatus = AlarmStatus.SCHEDULED,
                        takenAt = null
                    ),
                    onEvent = { }
                )
            }
        }
    }
}