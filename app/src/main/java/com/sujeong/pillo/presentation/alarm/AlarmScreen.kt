package com.sujeong.pillo.presentation.alarm

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sujeong.pillo.R
import com.sujeong.pillo.ui.component.button.LargePrimaryButton
import com.sujeong.pillo.ui.component.button.LargeSecondaryOutlineButton
import com.sujeong.pillo.ui.theme.PilloTheme

@Composable
fun AlarmScreen(
    onGoBack: () -> Unit,
    viewModel: AlarmViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    BackHandler { }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect {
            when(it) {
                AlarmUiEffect.TakenMedicine,
                AlarmUiEffect.SkippedMedicine -> onGoBack()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PilloTheme.colors.secondary,
    ) { innerPadding ->
        AlarmContent(
            alarmTime = state.value.alarmTime,
            title = state.value.title,
            onEvent = viewModel::onEvent,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun AlarmContent(
    alarmTime: String,
    title: String,
    onEvent: (AlarmEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = 60.dp,
                bottom = 24.dp
            )
            .padding(
                horizontal = 24.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = alarmTime,
            color = PilloTheme.colors.onSecondary,
            style = PilloTheme.typography.displayMediumSemiBold
        )

        Text(
            text = stringResource(R.string.label_alarm_time),
            color = PilloTheme.colors.onSecondary,
            style = PilloTheme.typography.displayMedium
        )

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                color = PilloTheme.colors.onSecondary,
                style = PilloTheme.typography.displayLarge,
                textAlign = TextAlign.Center
            )
        }

        LargePrimaryButton(
            text = R.string.btn_now_take,
            onClick = {
                onEvent(
                    AlarmEvent.TakeMedicine
                )
            }
        )

        LargeSecondaryOutlineButton(
            text = R.string.btn_alarm_skip,
            onClick = {
                onEvent(
                    AlarmEvent.SkipMedicine
                )
            }
        )
    }
}

@Composable
@Preview
fun AlarmScreenPreview() {
    PilloTheme {
        AlarmContent(
            alarmTime = "12:00",
            title = "마그네슘",
            onEvent = {},
            modifier = Modifier.background(
                PilloTheme.colors.secondary
            )
        )
    }
}