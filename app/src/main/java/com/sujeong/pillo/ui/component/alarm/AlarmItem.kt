package com.sujeong.pillo.ui.component.alarm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sujeong.pillo.ui.theme.PilloTheme

@Composable
fun MedicineAlarmItem(
    alarmTime: String,
    title: String,
    alarmStatusText: String,
    alarmStatusColor: Color,
    onDeleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp)
            ).background(PilloTheme.colors.surface)
            .padding(
                vertical = 12.dp,
            )
    ) {
        Text(
            text = alarmTime,
            color = PilloTheme.colors.onSurface,
            style = PilloTheme.typography.titleLarge,
            modifier = Modifier.padding(
                horizontal = 16.dp
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = PilloTheme.colors.divider,
            modifier = Modifier.padding(
                horizontal = 16.dp
            )
        )

        Row(
            modifier = Modifier.padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    color = PilloTheme.colors.onSurface,
                    style = PilloTheme.typography.bodyMediumBold,
                )

                Text(
                    text = alarmStatusText,
                    color = alarmStatusColor,
                    style = PilloTheme.typography.labelLarge,
                )
            }

            IconButton(
                onClick = { onDeleted() },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "",
                    tint = PilloTheme.colors.iconDefault,
                )
            }
        }
    }
}

@Composable
@Preview
fun AlarmItemPreview() {
    PilloTheme {
        Column(
            modifier = Modifier
                .background(PilloTheme.colors.surface)
                .padding(16.dp)
        ) {
            MedicineAlarmItem(
                alarmTime = "12:00",
                title = "약",
                alarmStatusText = "예정됨",
                alarmStatusColor = PilloTheme.colors.onSurfaceVariant,
                onDeleted = { }
            )
        }
    }
}