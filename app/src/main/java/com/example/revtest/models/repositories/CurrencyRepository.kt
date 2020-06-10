package com.example.revtest.models.repositories

import com.example.revtest.models.dto.CurrencyRates
import com.example.revtest.models.network.RevolutApiService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object CurrencyRepositoryProvider {
    fun getCurrencyRepository() = CurrencyRepository(RevolutApiService.create())
}

class CurrencyRepository(private val apiService: RevolutApiService) : ICurrencyRepository{
    override fun getCurrencyRate(currency: String): Observable<CurrencyRates> =
        apiService.getCurrencyRates(currency)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
}

interface ICurrencyRepository {
    fun getCurrencyRate(currency: String): Observable<CurrencyRates>
}