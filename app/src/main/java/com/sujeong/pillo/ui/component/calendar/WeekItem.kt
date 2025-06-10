package com.sujeong.pillo.ui.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sujeong.pillo.R
import com.sujeong.pillo.presentation.home.model.CalendarDateModel
import com.sujeong.pillo.ui.theme.PilloTheme
import java.time.LocalDate

@Composable
fun DayWeekText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = PilloTheme.colors.onSurface,
        style = PilloTheme.typography.labelLargeBold,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
fun DayWeekRow(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DayWeekText(
            text = stringResource(R.string.label_day_week_sun),
            modifier = Modifier.weight(1f)
        )

        DayWeekText(
            text = stringResource(R.string.label_day_week_mon),
            modifier = Modifier.weight(1f)
        )

        DayWeekText(
            text = stringResource(R.string.label_day_week_tue),
            modifier = Modifier.weight(1f)
        )

        DayWeekText(
            text = stringResource(R.string.label_day_week_wed),
            modifier = Modifier.weight(1f)
        )

        DayWeekText(
            text = stringResource(R.string.label_day_week_thu),
            modifier = Modifier.weight(1f)
        )

        DayWeekText(
            text = stringResource(R.string.label_day_week_fri),
            modifier = Modifier.weight(1f)
        )

        DayWeekText(
            text = stringResource(R.string.label_day_week_sat),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun DateText(
    date: CalendarDateModel,
    onClickDate: (date: CalendarDateModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = date.date.dayOfMonth.toString(),
        color = if(date.isSelected) {
            PilloTheme.colors.onPrimary
        } else if(date.isToday) {
            PilloTheme.colors.primary
        } else {
            PilloTheme.colors.onSurface
        },
        style = if(date.isSelected) {
            PilloTheme.typography.labelLargeBold
        } else {
            PilloTheme.typography.labelLarge
        },
        textAlign = TextAlign.Center,
        modifier = modifier
            .widthIn(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if(date.isSelected) {
                    PilloTheme.colors.primary
                } else {
                    Color.Transparent
                }
            )
            .clickable {
                onClickDate(date)
            }
            .padding(vertical = 8.dp)

    )
}

@Composable
fun DateRow(
    dates: List<CalendarDateModel>,
    onclickDate: (date: CalendarDateModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        dates.forEach { date ->
            DateText(
                date = date,
                modifier = Modifier.weight(1f),
                onClickDate = onclickDate
            )
        }
    }
}

@Composable
@Preview()
fun DayTextPreview() {
    PilloTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            val today = LocalDate.now()
            val baseDate = LocalDate.now()

            DateRow(
                dates = (0..6).map {
                    CalendarDateModel(
                        baseDate.plusDays(it.toLong()),
                        today.isEqual(baseDate),
                        baseDate.dayOfMonth == 11,
                    )
                },
                onclickDate = { }
            )
        }
    }
}