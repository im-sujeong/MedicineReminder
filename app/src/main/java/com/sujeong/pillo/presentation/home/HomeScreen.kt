package com.sujeong.pillo.presentation.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sujeong.pillo.R
import com.sujeong.pillo.common.extension.toString
import com.sujeong.pillo.domain.model.Medicine
import com.sujeong.pillo.domain.model.enums.AlarmStatus
import com.sujeong.pillo.presentation.home.model.CalendarDateModel
import com.sujeong.pillo.presentation.home.model.CalendarWeekModel
import com.sujeong.pillo.presentation.home.model.PermissionDialogType.*
import com.sujeong.pillo.presentation.util.toUiColor
import com.sujeong.pillo.presentation.util.toUiText
import com.sujeong.pillo.ui.component.medicine.MedicineItem
import com.sujeong.pillo.ui.component.button.SmallButton
import com.sujeong.pillo.ui.component.calendar.DateText
import com.sujeong.pillo.ui.component.calendar.DayWeekRow
import com.sujeong.pillo.ui.component.dialog.AlertDialog
import com.sujeong.pillo.ui.theme.PilloTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import androidx.core.net.toUri

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val state = viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onEvent(HomeEvent.OnNotificationPermissionResult(granted))
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect {
            when(it) {
                HomeUiEffect.RequestNotificationPermission -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }

                HomeUiEffect.RequestSystemAlertWindowPermission -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val intent =  Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                            data = "package:${context.packageName}".toUri()
                        }

                        context.startActivity(intent)
                    }
                }

                is HomeUiEffect.ShowMessage -> {
                    snackBarHostState.showSnackbar(it.message)
                }
            }
        }
    }

    when(state.value.showPermissionDialogType) {
        NONE -> Unit

        NOTIFICATION -> {
            NotificationPermissionDialog(
                onClick = { isCancel ->
                    viewModel.onEvent(
                        HomeEvent.RequestNotificationPermission(isCancel)
                    )
                }
            )
        }

        SYSTEM_ALERT_WINDOW -> {
            SystemAlertWindowPermissionDialog(
                onClick = { isCancel ->
                    viewModel.onEvent(
                        HomeEvent.RequestSystemAlertWindowPermission(isCancel)
                    )
                }
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PilloTheme.colors.surface,
        floatingActionButton = {
            SmallButton(
                text = stringResource(R.string.btn_add_alarm),
                onClick = {
                    viewModel.onEvent(HomeEvent.AddMedicine)
                },
                icon = Icons.Rounded.Add,
                shadowElevation = 2.dp
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) { innerPadding ->
        HomeContent(
            currentWeekPage = state.value.currentWeekPage,
            weeks = state.value.weeks,
            selectedDate = state.value.selectedDate,
            medicine = state.value.medicine,
            onEvent = viewModel::onEvent,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun NotificationPermissionDialog(
    onClick: (isCancel: Boolean) -> Unit,
) {
    val context = LocalContext.current

    val isGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else true

    if(!isGranted) {
        AlertDialog(
            title = R.string.dialog_title_notification_permission,
            message = R.string.dialog_message_notification_permission,
            positiveButton = R.string.btn_grant,
            onPositiveClick = {
                onClick(false)
            },
            negativeButton = R.string.btn_cancel,
            onNegativeClick = {
                onClick(true)
            }
        )
    } else {
        onClick(true)
    }
}

@Composable
private fun SystemAlertWindowPermissionDialog(
    onClick: (isCancel: Boolean) -> Unit,
) {
    val context = LocalContext.current

    val canDrawOverlays = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Settings.canDrawOverlays(context)
    } else true

    if(!canDrawOverlays) {
        AlertDialog(
            title = R.string.dialog_title_system_alert_window_permission,
            message = R.string.dialog_message_system_alert_window_permission,
            positiveButton = R.string.btn_grant,
            onPositiveClick = {
                onClick(false)
            },
            negativeButton = R.string.btn_cancel,
            onNegativeClick = {
                onClick(true)
            }
        )
    } else {
        onClick(true)
    }
}

@Composable
private fun HomeContent(
    currentWeekPage: Int,
    weeks: List<CalendarWeekModel>,
    selectedDate: CalendarDateModel,
    medicine: Medicine?,
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
            currentWeekPage = currentWeekPage,
            weeks = weeks,
            onEvent = onEvent
        )

        HomeMedicineAlarmContent(
            medicine = medicine,
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
        style = PilloTheme.typography.titleLargeBold,
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
    currentWeekPage: Int,
    weeks: List<CalendarWeekModel>,
    onEvent: (HomeEvent) -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = currentWeekPage
    ) {
        weeks.size
    }

    LaunchedEffect(pagerState.currentPage) {
        onEvent(HomeEvent.ChangedWeekPage(pagerState.currentPage))
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
    medicine: Medicine?,
    onEvent: (HomeEvent) -> Unit
) {
    medicine?.let {
        var alarmStatusColor = medicine.alarmStatus.toUiColor()
        var alarmStatusText = medicine.alarmStatus.toUiText(
            it.takenAt?.hour ?: 0,
            it.takenAt?.minute ?: 0
        )

        MedicineItem(
            alarmTime = it.alarmDateTime.toString(
                stringResource(R.string.format_time_HH_mm)
            ),
            title = it.title,
            alarmStatusText = alarmStatusText,
            alarmStatusColor = alarmStatusColor,
            onDeleted = {
                onEvent(
                    HomeEvent.DeleteMedicine(it.id)
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
            currentWeekPage = 1,
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
            medicine = Medicine(
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