package com.example.revtest.presenters

import android.util.Log
import com.example.revtest.models.repositories.CurrencyRepositoryProvider
import com.example.revtest.models.repositories.ICurrencyRepository
import com.example.revtest.models.utils.roundTo2
import io.reactivex.Observable
import com.example.revtest.models.viewModels.CurrencyRatesViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class CurrencyRatesPresenter(private val currencyRepository: ICurrencyRepository = CurrencyRepositoryProvider.getCurrencyRepository()) :
    ICurrencyRatesPresenter {

    private var baseCurrency = "EUR"
    private val BASE_CURRENCY_DEFAULT_VALUE = 1.0
    private val currencyValueSubject: BehaviorSubject<Double> = BehaviorSubject.createDefault(BASE_CURRENCY_DEFAULT_VALUE)

    private var baseCurrencyObservable: Double by Delegates.observable(1.0) { _, _, newValue ->
        currencyValueSubject.onNext(newValue)
    }
    private val ratesLooper = Observable.interval(0, 1, TimeUnit.SECONDS)
        .flatMap {
            Log.d("ratesLooper", "Current thread: ${Thread.currentThread().name}")
            getLatestRates()
        }
        .repeat()

    override fun getCurrencyRateData(): Observable<List<CurrencyRatesViewModel>> =
        Observable.combineLatest<List<CurrencyRatesViewModel>, Double, List<CurrencyRatesViewModel>>(
            ratesLooper,
            currencyValueSubject,
            BiFunction { rates, currency ->
                Log.d("getCurrencyRateData", "Current thread: ${Thread.currentThread().name}")
                rates.map {
                    CurrencyRatesViewModel(
                        countryIcon = it.countryIcon,
                        currency = it.currency,
                        description = it.description,
                        isBaseCurrency = it.isBaseCurrency,
                        value = if (!it.isBaseCurrency) (it.value * currency).roundTo2()
                        else it.value
                    )
                }
            }
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())


    override fun setNewBaseCurrencyValue(currencyValue: String) {
        baseCurrencyObservable = try {
            currencyValue.toDouble()
        } catch (ex: NumberFormatException) {
            1.0
        }.roundTo2()
    }

    override fun setBaseCurrency(baseCurrency: String) {
        this.baseCurrency = baseCurrency
    }

    private fun getLatestRates() =
        currencyRepository.getCurrencyRate(baseCurrency.toUpperCase(Locale.getDefault()))
            .map { result ->
                Log.d("getLatestRates", "Current thread: ${Thread.currentThread().name}")
                result.rates.map { item ->
                    CurrencyRatesViewModel(
                        countryIcon = 1,
                        currency = item.key,
                        description = item.key,
                        value = item.value,
                        isBaseCurrency = false
                    )
                }
                    .toMutableList()
                    .apply {
                        val baseCurrency =
                            CurrencyRatesViewModel(
                                1,
                                result.baseCurrency,
                                result.baseCurrency,
                                baseCurrencyObservable,
                                isBaseCurrency = true
                            )
                        add(0, baseCurrency)
                    }
                    .toList()
            }
}
