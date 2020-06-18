package com.example.revtest.models.viewModels

data class CurrencyRatesViewModel(
    val countryIconId: Int = -1,
    val currency: String,
    val description: String = "",
    var rate: Double,
    val isBaseCurrency: Boolean = false
)