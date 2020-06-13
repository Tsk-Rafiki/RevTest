package com.example.revtest.models.data.dto

import com.google.gson.annotations.SerializedName

data class CurrencyRates(
    @SerializedName("baseCurrency")
    val baseCurrency: String,
    @SerializedName("rates")
    val rates: Map<String, Double> = emptyMap()
)