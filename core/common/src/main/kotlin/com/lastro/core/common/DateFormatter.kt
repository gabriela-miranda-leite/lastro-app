package com.lastro.core.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val ptBR = Locale("pt", "BR")

fun Date.toDisplayDate(): String =
    SimpleDateFormat("dd/MM/yyyy", ptBR).format(this)

fun Date.toDisplayDateTime(): String =
    SimpleDateFormat("dd/MM/yyyy HH:mm", ptBR).format(this)

fun Date.toMonthYear(): String =
    SimpleDateFormat("MMMM yyyy", ptBR).format(this)
