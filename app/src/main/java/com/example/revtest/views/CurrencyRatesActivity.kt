package com.example.revtest.views

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.revtest.R
import com.example.revtest.models.utils.EditTextWatcher
import com.example.revtest.presenters.CurrencyRatesPresenter
import com.example.revtest.presenters.ICurrencyRatesPresenter
import com.example.revtest.views.CurrencyRatesListAdapter.IOnRateItemClickListener
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_main.*


class CurrencyRatesActivity : AppCompatActivity(), IOnRateItemClickListener {

    private lateinit var presenter: ICurrencyRatesPresenter
    private lateinit var textWatcher: EditTextWatcher
    private lateinit var adapter: CurrencyRatesListAdapter
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        presenter = CurrencyRatesPresenter(context = this)
        textWatcher = EditTextWatcher(presenter)
        adapter = CurrencyRatesListAdapter(textWatcher, this).apply { setHasStableIds(true) }
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        getCurrencyRateData()
    }

    private fun getCurrencyRateData() {
        disposable = presenter.getCurrencyRateData().subscribeBy(
            onNext = {
                adapter.setData(it)
            }, onError = { exception ->
                print(exception)
                exception.message?.let { message ->
                    showErrorDialog("Error!", message)
                }
            })
    }

    private fun showErrorDialog(title: String, text: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(text)
            .show()
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    override fun onItemClick(currency: String, currencyValue: String) {
        recyclerView?.smoothScrollToPosition(0)
        presenter.setSelectedCurrency(currency)
        presenter.setSelectedCurrencyValue(currencyValue)
    }

    override fun onFocusChanged(
        v: View,
        hasFocus: Boolean,
        currency: String,
        currencyValue: String
    ) {
        if (hasFocus) {
            onItemClick(currency, currencyValue)
//            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
//                InputMethodManager.SHOW_FORCED,
//                0
//            )
        } else {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                v.windowToken,
                0
            )
        }
    }
}

