package com.sujeong.pillo.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import com.sujeong.pillo.presentation.util.toUiColor
import com.sujeong.pillo.presentation.util.toUiText
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
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect {
            when(it) {
                is HomeUiEffect.ShowMessage -> {
                    snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PilloTheme.colors.surface,
        floatingActionButton = {
            SmallButton(
                text = stringResource(R.string.btn_add_alarm),
                onClick = {
                    viewModel.onEvent(HomeEvent.AddMedicineAlarm)
                },
                icon = Icons.Rounded.Add,
                shadowElevation = 2.dp
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { innerPadding ->
        HomeContent(
            weeks = state.value.weeks,
            selectedDate = state.value.selectedDate,
            medicineAlarm = state.value.medicineAlarm,
            onEvent = viewModel::onEvent,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun HomeContent(
    weeks: List<CalendarWeekModel>,
    selectedDate: CalendarDateModel,
    medicineAlarm: MedicineAlarm?,
    onEvent: (HomeEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(
                top = 16.dp,
            )
    ) {
        HomeHeader(
            selectedDate = selectedDate
        )

        HomeDayWeekRow()
        HomeDatePager(
            weeks = weeks,
            onEvent = onEvent
        )

        HomeMedicineAlarmContent(
            medicineAlarm = medicineAlarm,
            onEvent = onEvent
        )
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
    onEvent: (HomeEvent) -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = 1
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
            )
            .padding(
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
        var alarmStatusColor = medicineAlarm.alarmStatus.toUiColor()
        var alarmStatusText = medicineAlarm.alarmStatus.toUiText(
            it.takenAt?.hour ?: 0,
            it.takenAt?.minute ?: 0
        )

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
            modifier = Modifier
                .padding(
                    horizontal = 20.dp
                )
                .padding(
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

        HomeContent(
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
            selectedDate = CalendarDateModel(
                date = today,
                isToday = true,
                isSelected = true
            ),
            medicineAlarm = MedicineAlarm(
                id = 1,
                title = "약",
                alarmDateTime = LocalDateTime.now(),
                alarmStatus = AlarmStatus.TAKEN,
                takenAt = LocalDateTime.now()
            ),
            onEvent = { },
            modifier = Modifier
                .fillMaxSize()
                .background(
                    PilloTheme.colors.surface
                )
        )
    }
}