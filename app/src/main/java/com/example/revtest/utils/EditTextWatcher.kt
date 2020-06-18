package com.example.revtest.utils

import android.text.Editable
import android.text.TextWatcher
import com.example.revtest.presenters.ICurrencyRatesPresenter

class EditTextWatcher(private val presenter: ICurrencyRatesPresenter) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        presenter.setNewBaseCurrencyValue(s.toString())
        presenter.setSelectedCurrencyValue(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }
}