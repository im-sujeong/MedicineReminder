package com.sujeong.pillo.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sujeong.pillo.ui.component.calendar.DateRow
import com.sujeong.pillo.ui.component.calendar.DayWeekRow
import com.sujeong.pillo.ui.theme.PilloTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PilloTheme.colors.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    top = 16.dp,
                )
        ) {
            HomeHeader(
                text = state.value.selectedDate,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            DayWeekRow(
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp
                    )
                    .padding(
                        top = 24.dp
                    )
            )

            val pagerState = rememberPagerState(
                initialPage = state.value.selectedPage
            ) {
                state.value.weeks.size
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.padding(top = 4.dp)
            ) { currentPage ->
                DateRow(
                    dates = state.value.weeks[currentPage].dates,
                    modifier = Modifier.padding(
                        horizontal = 16.dp
                    ),
                    onclickDate = { date ->
                        viewModel.onEvent(
                            HomeEvent.SelectedDate(date)
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun HomeHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = PilloTheme.colors.onSurface,
        style = PilloTheme.typography.titleLarge,
        modifier = modifier
    )
}

@Composable
@Preview
fun HomeScreenPreview() {
    PilloTheme {
        HomeScreen()
    }
}