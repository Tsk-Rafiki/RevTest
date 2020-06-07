package com.example.revtest.views

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.revtest.R
import com.example.revtest.models.viewModels.CurrencyRatesViewModel

class CurrencyRatesViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.rates_recycler_view_item, parent, false)) {
    private var title: TextView? = null
    private var description: TextView? = null
    private var textField: EditText? = null

    init {
        title = itemView.findViewById(R.id.title)
        description = itemView.findViewById(R.id.description)
        textField = itemView.findViewById(R.id.amount)
    }

    fun bind(viewModel: CurrencyRatesViewModel) {
        title?.text = viewModel.currency
        description?.text = viewModel.description
        if (!viewModel.isBaseCurrency) textField?.setText(viewModel.value.toString())
    }
}
