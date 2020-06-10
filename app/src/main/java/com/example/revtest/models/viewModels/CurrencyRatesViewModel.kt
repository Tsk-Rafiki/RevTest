package com.example.revtest.models.viewModels

data class CurrencyRatesViewModel(
    val countryIcon: Int = -1,
    val currency: String,
    val description: String,
    var value: Double,
    val isBaseCurrency: Boolean = false
)