package com.example.revtest.presenters
import io.reactivex.Observable
import com.example.revtest.models.viewModels.CurrencyRatesViewModel

class CurrencyRatesPresenter : ICurrencyRatesPresenter {
    private val data = listOf(
        CurrencyRatesViewModel(
            1,
            "USD",
            "US Dollar"
        ), CurrencyRatesViewModel(1, "EUR", "Euro")
    )
    override fun onCreate() {

    }

    override fun getCurrencyRateData() : Observable<List<CurrencyRatesViewModel>> {
        return Observable.fromArray(data)
    }

    override fun onDestroy() {

    }

}