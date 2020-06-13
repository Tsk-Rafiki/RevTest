package com.example.revtest.presenters

import android.content.Context
import android.util.Log
import com.example.revtest.R
import com.example.revtest.models.data.entities.CurrencyData
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

class CurrencyRatesPresenter(
    private val currencyRepository: ICurrencyRepository = CurrencyRepositoryProvider.getCurrencyRepository(),
    private val context: Context
) :
    ICurrencyRatesPresenter {

    var currencyData: List<CurrencyData>
    private var baseCurrency = "EUR"
    private val BASE_CURRENCY_DEFAULT_VALUE = 1.0

    init {
        currencyData = loadImages()
    }

    private val currencyValueSubject: BehaviorSubject<Double> =
        BehaviorSubject.createDefault(BASE_CURRENCY_DEFAULT_VALUE)

    private var baseCurrencyObservable: Double by Delegates.observable(1.0) { _, _, newValue ->
        currencyValueSubject.onNext(newValue)
    }
    private val ratesLooper = Observable.interval(0, 1, TimeUnit.SECONDS)
        .flatMap {
            Log.d("ratesLooper", "Current thread: ${Thread.currentThread().name}")
            getLatestRates()
        }
        .repeat()

    private fun loadImages(): List<CurrencyData> {
        val currencyName = context.resources.getStringArray(R.array.currency_names)
        val currencyDescription = context.resources.getStringArray(R.array.currency_description)
        val currencyIcons = context.resources.obtainTypedArray(R.array.currency_images)
        val currencyData = currencyName.mapIndexed { index, item ->
            CurrencyData(
                name = item,
                description = currencyDescription[index],
                iconId = currencyIcons.getResourceId(index, R.drawable.ic_launcher_background)
            )
        }
        currencyIcons.recycle()
        return currencyData
    }

    override fun getCurrencyRateData(): Observable<List<CurrencyRatesViewModel>> =
        Observable.combineLatest<List<CurrencyRatesViewModel>, Double, List<CurrencyRatesViewModel>>(
            ratesLooper,
            currencyValueSubject,
            BiFunction { rates, currency ->
                Log.d("getCurrencyRateData", "Current thread: ${Thread.currentThread().name}")
                rates.map { rate ->
                    val currentCurrency = currencyData.first { it.name == rate.currency }
                    CurrencyRatesViewModel(
                        countryIconId = currentCurrency.iconId,
                        currency = currentCurrency.name,
                        description = currentCurrency.description,
                        isBaseCurrency = rate.isBaseCurrency,
                        value = if (!rate.isBaseCurrency) (rate.value * currency).roundTo2()
                        else rate.value
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
                        countryIconId = 1,
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
                                countryIconId = -1,
                                currency = result.baseCurrency,
                                description = result.baseCurrency,
                                value = baseCurrencyObservable,
                                isBaseCurrency = true
                            )
                        add(0, baseCurrency)
                    }
                    .toList()
            }
}
