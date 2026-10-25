package com.lastro.core.common

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

private val brlFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

fun Double.toBRL(): String = brlFormat.format(this)

fun Long.centsToBRL(): String = brlFormat.format(this / 100.0)

fun BigDecimal.toBRL(): String = brlFormat.format(this)

fun String?.centsStringToReais(): BigDecimal =
    (this?.toBigDecimalOrNull() ?: BigDecimal.ZERO)
        .divide(BigDecimal(100), 2, RoundingMode.HALF_UP)
