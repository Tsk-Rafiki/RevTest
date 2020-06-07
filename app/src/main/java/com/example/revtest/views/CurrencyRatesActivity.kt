package com.example.revtest.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.revtest.R
import com.example.revtest.models.viewModels.CurrencyRatesViewModel
import com.example.revtest.presenters.CurrencyRatesPresenter
import kotlinx.android.synthetic.main.activity_main.*

class CurrencyRatesActivity : AppCompatActivity() {

    private val presenter = CurrencyRatesPresenter()
    private val adapter: CurrencyRatesListAdapter = CurrencyRatesListAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = adapter
        presenter.getCurrencyRateData().subscribe {
            adapter
        }
    }
}

