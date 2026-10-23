package com.lastro.core.common

import java.text.NumberFormat
import java.util.Locale

private val brlFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

fun Double.toBRL(): String = brlFormat.format(this)

fun Long.centsToBRL(): String = brlFormat.format(this / 100.0)

fun java.math.BigDecimal.toBRL(): String = brlFormat.format(this)
