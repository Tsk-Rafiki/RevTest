package com.example.revtest.presenters

import com.example.revtest.models.viewModels.CurrencyRatesViewModel
import io.reactivex.Observable

interface ICurrencyRatesPresenter {
    fun getCurrencyRateData(currency: String = "EUR" ): Observable<List<CurrencyRatesViewModel>>
    fun onResume() : Unit
    fun onPause() : Unit
}