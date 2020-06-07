package com.example.revtest.presenters

import android.util.Log
import com.example.revtest.models.repositories.CurrencyRepositoryProvider
import io.reactivex.Observable
import com.example.revtest.models.viewModels.CurrencyRatesViewModel
import java.util.*
import java.util.concurrent.TimeUnit

class CurrencyRatesPresenter : ICurrencyRatesPresenter {
    private val currencyRepository = CurrencyRepositoryProvider.getCurrencyRepository()

    override fun onResume() {

    }

    override fun getCurrencyRateData(currency: String): Observable<List<CurrencyRatesViewModel>> =
        Observable.interval(1, TimeUnit.SECONDS)
            .flatMap { getLatestRates(currency) }
            .repeat()


    private fun getLatestRates(currency: String) =
        currencyRepository.getCurrencyRate(currency.toUpperCase(Locale.getDefault()))
            .map { result ->
                result.rates.map { item ->
                    CurrencyRatesViewModel(
                        countryIcon = 1,
                        currency = item.key,
                        description = item.key,
                        value = item.value
                    )
                }
                    .toMutableList()
                    .apply {
                        val baseCurrency =
                            CurrencyRatesViewModel(
                                1,
                                result.baseCurrency,
                                result.baseCurrency,
                                0.0,
                                isBaseCurrency = true
                            )
                        add(0, baseCurrency)
                    }
                    .toList()


            }

    override fun onPause() {

    }

}