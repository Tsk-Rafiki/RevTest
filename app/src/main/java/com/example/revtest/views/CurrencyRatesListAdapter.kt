package com.example.revtest.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.revtest.models.viewModels.CurrencyRatesViewModel

class CurrencyRatesListAdapter() : RecyclerView.Adapter<CurrencyRatesViewHolder>() {

    private val items = mutableListOf<CurrencyRatesViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyRatesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CurrencyRatesViewHolder(inflater, parent)
    }

    fun setData(list: List<CurrencyRatesViewModel>) {

    }

    override fun onBindViewHolder(holder: CurrencyRatesViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long = items[position].
}
