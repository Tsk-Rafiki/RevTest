package com.example.revtest.views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.revtest.R
import com.example.revtest.presenters.CurrencyRatesPresenter
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*


class CurrencyRatesActivity : AppCompatActivity() {

    private val presenter = CurrencyRatesPresenter()
    private val adapter: CurrencyRatesListAdapter =
        CurrencyRatesListAdapter().apply { setHasStableIds(true) }
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        Log.d("[Activity]", "Lifecycle method: onResume")
        getCurrencyRateData()
    }

    private fun getCurrencyRateData() {
        disposable = presenter.getCurrencyRateData("aud").subscribe {
            adapter.setData(it)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("[Activity]", "Lifecycle method: onPause")
        disposable?.dispose()
    }
}

