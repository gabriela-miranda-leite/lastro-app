package com.lastro.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import com.lastro.core.ui.R
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastro.core.ui.theme.LastroTheme

enum class TextFieldType { Default, Money, Password, Email }

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
                    painter = painterResource(if (passwordVisible) R.drawable.ic_eye else R.drawable.ic_eye_off),
                    contentDescription = if (passwordVisible) "Ocultar senha" else "Mostrar senha",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        })
        type == TextFieldType.Email -> ({
            Icon(
                painter = painterResource(R.drawable.ic_mail),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
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
        type == TextFieldType.Email -> KeyboardOptions(keyboardType = KeyboardType.Email)
        else -> keyboardOptions
    }

    Column(modifier = modifier) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(6.dp))
        val inputTextStyle = if (type == TextFieldType.Money)
            MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        else
            MaterialTheme.typography.bodyLarge

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = inputTextStyle,
            label = null,
            placeholder = {
                Text(
                    text = if (type == TextFieldType.Money) "R$ 0,00" else label,
                    style = inputTextStyle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
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

@Preview(name = "Default - Light")
@Composable
private fun PreviewDefault() = LastroTheme(darkTheme = false) {
    LastroTextField(value = "gabriela@email.com", onValueChange = {}, label = "Email", type = TextFieldType.Email)
}

@Preview(name = "Money - Light")
@Composable
private fun PreviewMoney() = LastroTheme(darkTheme = false) {
    LastroTextField(value = "25000", onValueChange = {}, label = "Valor", type = TextFieldType.Money)
}

@Preview(name = "Error - Light")
@Composable
private fun PreviewError() = LastroTheme(darkTheme = false) {
    LastroTextField(value = "", onValueChange = {}, label = "Email", error = "Campo obrigatório")
}

@Preview(name = "Password - Light")
@Composable
private fun PreviewPassword() = LastroTheme(darkTheme = false) {
    LastroTextField(value = "senha123", onValueChange = {}, label = "Senha", type = TextFieldType.Password)
}
