package com.example.revtest.views

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.revtest.R

class CurrencyRatesViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.rates_recycler_view_item, parent, false)) {
    private var mTitleView: TextView? = null
    private var mYearView: TextView? = null

    init {
        mTitleView = itemView.findViewById(R.id.title)
        mYearView = itemView.findViewById(R.id.description)
    }

    fun bind(viewModel: CurrencyRatesViewModel) {
        mTitleView?.text = viewModel.countryName
        mYearView?.text = viewModel.description
    }
}
