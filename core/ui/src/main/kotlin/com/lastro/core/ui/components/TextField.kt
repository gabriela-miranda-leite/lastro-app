package com.lastro.core.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.lastro.core.ui.theme.LastroTheme

enum class TextFieldType { Default, Money, Password }

@Composable
fun LastroTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    type: TextFieldType = TextFieldType.Default,
    error: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val resolvedTrailingIcon: @Composable (() -> Unit)? = when {
        type == TextFieldType.Password -> ({
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                    contentDescription = if (passwordVisible) "Ocultar senha" else "Mostrar senha",
                )
            }
        })
        trailingIcon != null -> trailingIcon
        else -> null
    }

    val resolvedVisualTransformation = when {
        type == TextFieldType.Password && !passwordVisible -> PasswordVisualTransformation()
        type == TextFieldType.Money -> MoneyVisualTransformation
        else -> VisualTransformation.None
    }

    val resolvedKeyboardOptions = when {
        type == TextFieldType.Money -> KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
        type == TextFieldType.Password -> KeyboardOptions(keyboardType = KeyboardType.Password)
        else -> keyboardOptions
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(label, style = MaterialTheme.typography.labelMedium) },
        isError = error != null,
        supportingText = error?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
        trailingIcon = resolvedTrailingIcon,
        visualTransformation = resolvedVisualTransformation,
        keyboardOptions = resolvedKeyboardOptions,
        singleLine = singleLine,
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            errorBorderColor = MaterialTheme.colorScheme.error,
        ),
    )
}

internal val moneyVisualTransformationForTest: VisualTransformation get() = MoneyVisualTransformation

private object MoneyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter { it.isDigit() }.takeLast(13)
        val number = digits.toLongOrNull() ?: 0L
        val cents = number % 100
        val units = number / 100
        val intPart = String.format(java.util.Locale.US, "%,d", units)
            .replace(',', '.')
        val formatted = "R$ $intPart,${cents.toString().padStart(2, '0')}"
        return TransformedText(
            AnnotatedString(formatted),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int) = formatted.length
                override fun transformedToOriginal(offset: Int) = text.length
            }
        )
    }
}

@Preview(name = "Default - Dark")
@Composable
private fun PreviewDefault() = LastroTheme {
    LastroTextField(value = "gabriela@email.com", onValueChange = {}, label = "Email")
}

@Preview(name = "Money - Dark")
@Composable
private fun PreviewMoney() = LastroTheme {
    LastroTextField(value = "25000", onValueChange = {}, label = "Valor", type = TextFieldType.Money)
}

@Preview(name = "Error - Dark")
@Composable
private fun PreviewError() = LastroTheme {
    LastroTextField(value = "", onValueChange = {}, label = "Email", error = "Campo obrigatório")
}

@Preview(name = "Password - Dark")
@Composable
private fun PreviewPassword() = LastroTheme {
    LastroTextField(value = "senha123", onValueChange = {}, label = "Senha", type = TextFieldType.Password)
}
