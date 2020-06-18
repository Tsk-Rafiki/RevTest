package com.example.revtest.presenters

import com.example.revtest.models.viewModels.CurrencyRatesViewModel
import io.reactivex.Observable

interface ICurrencyRatesPresenter {
    fun setBaseCurrency(baseCurrency: String)
    fun setNewBaseCurrencyValue(currencyValue: String)
    fun getCurrencyRateData(): Observable<List<CurrencyRatesViewModel>>
    fun setSelectedCurrency(value: String)
    fun setSelectedCurrencyValue(value: String)
}