package com.lastro.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.R
import com.lastro.core.ui.theme.LastroColors
import com.lastro.core.ui.theme.LastroSpacing
import com.lastro.core.ui.theme.LastroTheme

enum class BannerType { Error, Success, Info }

@Composable
fun LastroBanner(
    message: String,
    modifier: Modifier = Modifier,
    type: BannerType = BannerType.Error,
) {
    val (iconPainter, tint, background, border) = when (type) {
        BannerType.Error -> BannerStyle(
            iconPainter = painterResource(R.drawable.ic_alert_triangle),
            tint = LastroColors.Negative,
            background = LastroColors.Negative.copy(alpha = 0.12f),
            border = LastroColors.Negative.copy(alpha = 0.3f),
        )
        BannerType.Success -> BannerStyle(
            iconPainter = painterResource(R.drawable.ic_check_circle),
            tint = LastroColors.Positive,
            background = LastroColors.Positive.copy(alpha = 0.10f),
            border = LastroColors.Positive.copy(alpha = 0.3f),
        )
        BannerType.Info -> BannerStyle(
            iconPainter = painterResource(R.drawable.ic_info_circle),
            tint = LastroColors.Accent,
            background = LastroColors.Accent.copy(alpha = 0.10f),
            border = LastroColors.Accent.copy(alpha = 0.3f),
        )
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = background,
        border = androidx.compose.foundation.BorderStroke(1.dp, border),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = LastroSpacing.md, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = iconPainter,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = tint,
            )
        }
    }
}

private data class BannerStyle(
    val iconPainter: Painter,
    val tint: Color,
    val background: Color,
    val border: Color,
)

@Preview(name = "Banner Error - Dark")
@Composable
private fun PreviewError() = LastroTheme {
    LastroBanner(message = "Email ou senha incorretos.")
}

@Preview(name = "Banner Success - Dark")
@Composable
private fun PreviewSuccess() = LastroTheme {
    LastroBanner(message = "Convite enviado! Seu parceiro receberá um email.", type = BannerType.Success)
}

@Preview(name = "Banner Info - Light")
@Composable
private fun PreviewInfo() = LastroTheme(darkTheme = false) {
    LastroBanner(message = "Regra 50/30/20 — sugerimos guardar pelo menos 20% da renda.", type = BannerType.Info)
}
