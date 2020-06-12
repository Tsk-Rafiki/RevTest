package com.example.revtest.models.utils

import android.util.Log
import kotlin.math.roundToInt

fun Double.roundTo2() = try {
//        "%.2f".format(Locale.ENGLISH,this).toDouble()
    (this * 100).roundToInt() / 100.0
    } catch (ex: NumberFormatException) {
        Log.e("round2", "Exception: $ex")
        1.0
    }
