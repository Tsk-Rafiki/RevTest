package com.example.revtest.models.repositories

import com.example.revtest.models.dto.CurrencyRates
import com.example.revtest.models.network.RevolutApiService
import io.reactivex.Observable

object CurrencyRepositoryProvider {
    fun getCurrencyRepository() = CurrencyRepository(RevolutApiService.create())
}

class CurrencyRepository(private val apiService: RevolutApiService) : ICurrencyRepository{
    override fun getCurrencyRate(currency: String): Observable<CurrencyRates> =
        apiService.getCurrencyRates(currency)
}

interface ICurrencyRepository {
    fun getCurrencyRate(currency: String): Observable<CurrencyRates>
}