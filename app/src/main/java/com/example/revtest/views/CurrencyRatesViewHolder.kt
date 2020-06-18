package com.example.revtest.views

import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.revtest.R
import com.example.revtest.models.utils.setCursorToEnd
import com.example.revtest.models.viewModels.CurrencyRatesViewModel
import com.example.revtest.views.CurrencyRatesListAdapter.IOnRateItemClickListener

class CurrencyRatesViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.rates_recycler_view_item, parent, false)) {
    private var title: TextView? = null
    private var description: TextView? = null
    private var textField: EditText? = null
    private var layout: RelativeLayout? = null
    private var countryFlag: ImageView? = null

    init {
        title = itemView.findViewById(R.id.title)
        description = itemView.findViewById(R.id.description)
        textField = itemView.findViewById(R.id.amount)
        layout = itemView.findViewById(R.id.ratesItemLayout)
        countryFlag = itemView.findViewById(R.id.image)
    }

    fun bind(
        viewModel: CurrencyRatesViewModel,
        textWatcher: TextWatcher?,
        onItemClickListener: IOnRateItemClickListener
    ) {
        title?.text = viewModel.currency
        description?.text = viewModel.description
        countryFlag?.setImageResource(viewModel.countryIconId)
        configureTextField(viewModel, textWatcher, onItemClickListener)
        layout?.setOnClickListener {
            setFocusOnTextField()
            onItemClickListener.onItemClick(viewModel.currency, viewModel.rate.toString())
        }
    }

    private fun configureTextField(viewModel: CurrencyRatesViewModel, textWatcher: TextWatcher?, onItemClickListener: IOnRateItemClickListener) =
        textField?.let {
            it.removeTextChangedListener(textWatcher)
            if (!it.isFocused)
                it.setText(viewModel.rate.toString())
            if (viewModel.isBaseCurrency) {
                it.addTextChangedListener(textWatcher)
                it.isFocusable = true
            } else {
                it.isFocusable = false
                it.setOnClickListener {
                    setFocusOnTextField()
                    onItemClickListener.onItemClick(viewModel.currency, viewModel.rate.toString())
                }
            }
            it.setCursorToEnd()

            it.setOnEditorActionListener { _, actionId, _ ->
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    textField?.clearFocus()
                    true
                } else false
            }
            it.setOnFocusChangeListener { v, hasFocus ->
                it.setCursorToEnd()
                onItemClickListener.onFocusChanged(v, hasFocus, viewModel.currency, viewModel.rate.toString())
            }
        }

    private fun setFocusOnTextField() {
        textField?.isFocusableInTouchMode = true
        textField?.requestFocus()
        textField?.isFocusableInTouchMode = false

    }
}
