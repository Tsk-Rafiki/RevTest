package com.example.revtest.models.network

import com.example.revtest.models.data.dto.CurrencyRates
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

// 'https://hiring.revolut.codes/api/android/latest?base=EUR'

public interface RevolutApiService {

    @Headers("Accept: application/json")
    @GET("api/android/latest")
    fun getCurrencyRates(@Query("base") base: String): Observable<CurrencyRates>

    companion object Factory {
        fun create() : RevolutApiService {
            val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build();

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://hiring.revolut.codes/")
                .client(client)
                .build()
            return retrofit.create(RevolutApiService::class.java)
        }
    }
}