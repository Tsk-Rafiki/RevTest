package com.example.revtest.presenters

import com.example.revtest.models.viewModels.CurrencyRatesViewModel
import io.reactivex.Observable

interface ICurrencyRatesPresenter {
    fun getCurrencyRateData() : Observable<List<CurrencyRatesViewModel>>
    fun onCreate() : Unit
    fun onDestroy() : Unit
}