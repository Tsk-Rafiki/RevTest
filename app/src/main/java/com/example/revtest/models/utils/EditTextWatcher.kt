package com.example.revtest.models.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.example.revtest.presenters.ICurrencyRatesPresenter

class EditTextWatcher(private val presenter: ICurrencyRatesPresenter) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        Log.d("EditTextWatcher", s.toString())
        presenter.setNewBaseCurrencyValue(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }
}