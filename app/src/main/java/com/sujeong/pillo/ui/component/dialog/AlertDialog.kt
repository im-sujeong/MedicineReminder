package com.sujeong.pillo.ui.component.dialog

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sujeong.pillo.ui.theme.PilloTheme

@Composable
fun AlertDialog(
    @StringRes title: Int,
    @StringRes message: Int,
    @StringRes positiveButton: Int,
    onPositiveClick: () -> Unit,
    @StringRes negativeButton: Int? = null,
    onNegativeClick: (() -> Unit)? = null
) {
    AlertDialog(
        title = stringResource(id = title),
        message = stringResource(id = message),
        positiveButton = stringResource(id = positiveButton),
        onPositiveClick = onPositiveClick,
        negativeButton = negativeButton?.let { stringResource(id = it) },
        onNegativeClick = onNegativeClick
    )
}

@Composable
fun AlertDialog(
    title: String,
    message: String,
    positiveButton: String,
    onPositiveClick: () -> Unit,
    negativeButton: String? = null,
    onNegativeClick: (() -> Unit)? = null
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(
                    top = 24.dp,
                    bottom = 16.dp
                )
            ) {
                Text(
                    text = title,
                    style = PilloTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    color = PilloTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = message,
                    style = PilloTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    color = PilloTheme.colors.onSurface
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
                ) {
                    negativeButton?.let {
                        DialogButton(
                            text = it,
                            textColor = PilloTheme.colors.onSurfaceVariant,
                            onClick = onNegativeClick
                        )
                    }

                    DialogButton(
                        text = positiveButton,
                        textColor = PilloTheme.colors.primary,
                        onClick = onPositiveClick
                    )
                }
            }
        }
    }
}

@Composable
private fun DialogButton(
    text: String,
    textColor: Color,
    onClick: (() -> Unit)? = null
) {
    Text(
        text = text,
        style = PilloTheme.typography.bodyMediumBold,
        color = textColor,
        modifier = Modifier
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp
            )
            .clickable {
                onClick?.invoke()
            }
    )
}

@Composable
@Preview
fun AlertDialogPreview() {
    PilloTheme {
        AlertDialog(
            title = "타이틀",
            message = "다이얼로그 내용",
            positiveButton = "확인",
            onPositiveClick = {},
            negativeButton = "취소",
            onNegativeClick = {}
        )
    }
}