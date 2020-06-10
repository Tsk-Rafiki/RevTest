package com.example.revtest.views

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.revtest.R
import com.example.revtest.models.utils.EditTextWatcher
import com.example.revtest.presenters.CurrencyRatesPresenter
import com.example.revtest.views.CurrencyRatesListAdapter.IOnRateItemClickListener
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_main.*

class CurrencyRatesActivity : AppCompatActivity(), IOnRateItemClickListener {

    private val presenter = CurrencyRatesPresenter()
    private val textWatcher = EditTextWatcher(presenter)
    private val adapter: CurrencyRatesListAdapter =
        CurrencyRatesListAdapter(textWatcher, this).apply { setHasStableIds(true) }
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

    override fun onPause() {
        super.onPause()
        Log.d("[Activity]", "Lifecycle method: onPause")
        disposable?.dispose()
    }

    private fun showErrorDialog(title: String, text: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(text)
            .show()
    }

    override fun onItemClick(currency: String) {
        presenter.setBaseCurrency(currency)
    }

    override fun onFocusChanged(v: View, hasFocus: Boolean) {

    }
}

