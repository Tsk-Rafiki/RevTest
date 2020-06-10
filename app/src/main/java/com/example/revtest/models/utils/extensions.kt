package com.example.revtest.models.utils

fun Double.roundTo2() = try {
        String.format("%.2f", this).toDouble()
    } catch (ex: NumberFormatException) {
        0.0
    }
