package com.example.revtest.presenters

import android.content.Context
import com.example.revtest.R
import com.example.revtest.models.data.dto.CurrencyRates
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
    private var selectedCurrency = "EUR"
    private var selectedCurrencyValue = 1.0

    private val UNKNOWN_CURRENCY_TEXT = "-"
    private val BASE_CURRENCY_DEFAULT_VALUE = 1.0
    private val RATE_LOOP_TIME_INTERVAL = 1L

    init {
        currencyData = loadImages()
    }

    private val currencyValueSubject = BehaviorSubject.createDefault(BASE_CURRENCY_DEFAULT_VALUE)

    private var baseCurrencyValueObservable: Double by Delegates.observable(
        BASE_CURRENCY_DEFAULT_VALUE
    ) { _, oldValue, newValue ->
        if (oldValue != newValue)
            currencyValueSubject.onNext(newValue)
    }
    private val ratesLooper = Observable.interval(0, RATE_LOOP_TIME_INTERVAL, TimeUnit.SECONDS)
        .flatMap {
            if (selectedCurrency != baseCurrency)
                baseCurrency = selectedCurrency
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

                rates.map { rate ->
                    val currentCurrencyData = currencyData.firstOrNull { it.name == rate.currency }
                    val value =
                        if (baseCurrency == selectedCurrency) currency * rate.rate else selectedCurrencyValue * rate.rate

                    CurrencyRatesViewModel(
                        countryIconId = currentCurrencyData?.iconId
                            ?: R.drawable.ic_launcher_background,
                        currency = currentCurrencyData?.name ?: UNKNOWN_CURRENCY_TEXT,
                        description = currentCurrencyData?.description ?: UNKNOWN_CURRENCY_TEXT,
                        isBaseCurrency = rate.currency == baseCurrency,
                        rate = value.roundTo2()
                    )
                }
            }
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

    override fun setNewBaseCurrencyValue(currencyValue: String) {
        baseCurrencyValueObservable = try {
            currencyValue.toDouble()
        } catch (ex: NumberFormatException) {
            0.0
        }
    }

    override fun setBaseCurrency(baseCurrency: String) {
        if (baseCurrency.isEmpty()) return
        this.baseCurrency = baseCurrency
    }

    private fun getLatestRates() =
        currencyRepository.getCurrencyRate(baseCurrency.toUpperCase(Locale.getDefault()))
            .map { result: CurrencyRates ->
                baseCurrency = selectedCurrency
                baseCurrencyValueObservable = selectedCurrencyValue
                result.rates.map { item ->
                    CurrencyRatesViewModel(
                        currency = item.key,
                        rate = item.value
                    )
                }
                    .toMutableList()
                    .apply {
                        add(
                            0, CurrencyRatesViewModel(
                                currency = result.baseCurrency,
                                rate = 1.0,
                                isBaseCurrency = true
                            )
                        )
                    }.toList()
            }

    override fun setSelectedCurrency(value: String) {
        selectedCurrency = value
    }

    override fun setSelectedCurrencyValue(value: String) {
        selectedCurrencyValue = try {
            value.toDouble()
        } catch (ex: NumberFormatException) {
            0.0
        }
    }
}
