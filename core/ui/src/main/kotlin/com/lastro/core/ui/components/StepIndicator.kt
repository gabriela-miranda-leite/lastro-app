package com.lastro.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.theme.LastroTheme

@Composable
fun LastroStepIndicator(
    totalSteps: Int,
    currentStep: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        repeat(totalSteps) { index ->
            val isCompleted = index < currentStep
            val isActive = index == currentStep
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(3.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(
                        when {
                            isCompleted -> MaterialTheme.colorScheme.primary
                            isActive -> MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
                            else -> MaterialTheme.colorScheme.outline
                        }
                    ),
            )
        }
    }
}

@Preview(name = "StepIndicator passo 2 - Dark")
@Composable
private fun PreviewDark() = LastroTheme {
    LastroStepIndicator(
        totalSteps = 3,
        currentStep = 1,
        modifier = Modifier.padding(16.dp),
    )
}

@Preview(name = "StepIndicator passo 1 - Light")
@Composable
private fun PreviewLight() = LastroTheme(darkTheme = false) {
    LastroStepIndicator(
        totalSteps = 3,
        currentStep = 0,
        modifier = Modifier.padding(16.dp),
    )
}
