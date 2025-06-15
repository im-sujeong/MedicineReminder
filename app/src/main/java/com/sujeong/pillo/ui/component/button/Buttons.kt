package com.sujeong.pillo.ui.component.button

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sujeong.pillo.ui.theme.PilloTheme

@Composable
fun SmallButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    iconSize: Dp = 20.dp,
    shadowElevation: Dp = 0.dp,
    containerColor: Color = PilloTheme.colors.primary,
    contentColor: Color = PilloTheme.colors.onPrimary,
) {
    Row(
        modifier = modifier
            .widthIn(min = 20.dp)
            .heightIn(min = 44.dp)
            .then(
                if(shadowElevation.value > 0) {
                    Modifier.shadow(
                        elevation = shadowElevation,
                        shape = RoundedCornerShape(16.dp)
                    )
                } else {
                    Modifier.clip(RoundedCornerShape(16.dp))
                }
            )
            .background(containerColor)
            .clickable(enabled = enabled) {
                onClick()
            }.padding(
                vertical = 12.dp
            ).padding(
                start = 16.dp,
                end = 20.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = "",
                tint = contentColor,
                modifier = Modifier.size(iconSize)
            )
        }

        Text(
            text = text,
            color = contentColor,
            style = PilloTheme.typography.bodyMediumBold,
            modifier = Modifier.padding(
                start = 4.dp
            )
        )
    }
}

@Composable
fun LargePrimaryButton(
    @StringRes text: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LargePrimaryButton(
        text = stringResource(text),
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun LargePrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = { onClick() },
        modifier = modifier.fillMaxWidth()
            .heightIn(min = 60.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PilloTheme.colors.primary,
            contentColor = PilloTheme.colors.onPrimary
        )
    ) {
        Text(
            text = text,
            color = PilloTheme.colors.onPrimary,
            style = PilloTheme.typography.titleLargeSemiBold
        )
    }
}

@Composable
fun LargeSecondaryOutlineButton(
    @StringRes text: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LargeSecondaryOutlineButton(
        text = stringResource(text),
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun LargeSecondaryOutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onClick() },
        modifier = modifier.fillMaxWidth()
            .heightIn(min = 60.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PilloTheme.colors.secondary,
            contentColor = PilloTheme.colors.onSecondary
        ),
        border = BorderStroke(1.dp, PilloTheme.colors.primary)
    ) {
        Text(
            text = text,
            color = PilloTheme.colors.onPrimary,
            style = PilloTheme.typography.titleLarge
        )
    }
}

@Composable
@Preview
fun SmallButtonPreview() {
    PilloTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PilloTheme.colors.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SmallButton(
                text = "Button",
                onClick = {},
                icon = Icons.Rounded.Add
            )

            LargePrimaryButton(
                text = "테스트",
                onClick = {}
            )

            LargeSecondaryOutlineButton(
                text = "아웃라인",
                onClick = {}
            )
        }
    }
}