package com.example.revtest.views

import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.revtest.R
import com.example.revtest.models.viewModels.CurrencyRatesViewModel
import com.example.revtest.views.CurrencyRatesListAdapter.IOnRateItemClickListener

class CurrencyRatesViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.rates_recycler_view_item, parent, false)) {
    private var title: TextView? = null
    private var description: TextView? = null
    private var textField: EditText? = null
    private var layout: RelativeLayout? = null

    init {
        title = itemView.findViewById(R.id.title)
        description = itemView.findViewById(R.id.description)
        textField = itemView.findViewById(R.id.amount)
        layout = itemView.findViewById(R.id.ratesItemLayout)
    }

    fun bind(viewModel: CurrencyRatesViewModel, textWatcher: TextWatcher?, onItemClickListener: IOnRateItemClickListener) {
        title?.text = viewModel.currency
        description?.text = viewModel.description
        textField?.removeTextChangedListener(textWatcher)
        if (!viewModel.isBaseCurrency) {
            textField?.setText(viewModel.value.toString())
            textField?.isFocusable = false
        } else {
            textField?.isFocusable = true
            textField?.addTextChangedListener(textWatcher)
            textField?.setOnFocusChangeListener { v, hasFocus ->
                onItemClickListener.onFocusChanged(v, hasFocus)
            }
        }

        layout?.setOnClickListener {
            textField?.isFocusableInTouchMode = true
            textField?.requestFocus()
            textField?.isFocusableInTouchMode = false
            onItemClickListener.onItemClick(viewModel.currency)
        }
    }
}
