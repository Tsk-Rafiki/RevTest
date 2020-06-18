package com.example.revtest.views

import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.revtest.models.viewModels.CurrencyRatesViewModel

class CurrencyRatesListAdapter(
    private val textWatcher: TextWatcher,
    private val onItemClickListener: IOnRateItemClickListener
) : RecyclerView.Adapter<CurrencyRatesViewHolder>() {
    private var items = listOf<CurrencyRatesViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyRatesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CurrencyRatesViewHolder(inflater, parent)
    }

    fun setData(list: List<CurrencyRatesViewModel>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CurrencyRatesViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, textWatcher, onItemClickListener)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long = items[position].currency.hashCode().toLong()

    interface IOnRateItemClickListener {
        fun onItemClick(currency: String, currencyValue: String)
        fun onFocusChanged(v: View, hasFocus: Boolean)
    }
}
