package com.example.fundapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fundapp.databinding.FundItemBinding
import com.example.fundapp.databinding.StockItemBinding
import com.example.fundapp.model.FundWithAmount
import com.example.fundapp.model.Stock

class FundAdapter(
    private val onDeleteClick: (String) -> Unit,
    private val onClearAllClick: () -> Unit
) : ListAdapter<FundWithAmount, FundAdapter.FundViewHolder>(FundDiffCallback()) {

    inner class FundViewHolder(val binding: FundItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private var stockAdapter: StockAdapter? = null

        init {
            binding.btnExpand.setOnClickListener {
                val isExpanded = binding.holdingsLayout.visibility == View.VISIBLE
                binding.holdingsLayout.visibility = if (isExpanded) View.GONE else View.VISIBLE
                binding.btnExpand.setImageResource(
                    if (isExpanded) android.R.drawable.arrow_down_float 
                    else android.R.drawable.arrow_up_float
                )
            }

            binding.btnDelete.setOnClickListener {
                val fund = getItem(adapterPosition)
                onDeleteClick(fund.fund.code)
            }
        }

        fun bind(item: FundWithAmount) {
            binding.tvFundCode.text = item.fund.code
            binding.tvFundName.text = item.fund.name
            binding.tvNetValue.text = String.format("%.4f", item.fund.netValue)
            
            val changePercentStr = String.format("%.2f%%", item.fund.changePercent)
            binding.tvChangePercent.text = if (item.fund.changePercent >= 0) "+$changePercentStr" else changePercentStr
            
            val changeStr = String.format("%.4f", item.fund.change)
            binding.tvChange.text = if (item.fund.change >= 0) "+$changeStr" else changeStr

            val earningsStr = String.format("%.2f", item.estimatedEarnings)
            binding.tvEarnings.text = if (item.estimatedEarnings >= 0) "+$earningsStr" else earningsStr

            val colorRes = if (item.fund.changePercent >= 0) {
                android.R.color.holo_red_dark
            } else {
                android.R.color.holo_green_dark
            }
            binding.tvChangePercent.setTextColor(itemView.context.getColor(colorRes))
            binding.tvChange.setTextColor(itemView.context.getColor(colorRes))
            binding.tvEarnings.setTextColor(itemView.context.getColor(colorRes))

            binding.tvAmount.text = String.format("持有: %.2f元", item.amount)
            binding.tvUpdateTime.text = item.fund.updateTime

            stockAdapter = StockAdapter()
            binding.rvHoldings.adapter = stockAdapter
            stockAdapter?.submitList(item.fund.holdings)
        }
    }

    inner class StockAdapter : ListAdapter<Stock, StockViewHolder>(StockDiffCallback()) {
        inner class StockViewHolder(val binding: StockItemBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(item: Stock) {
                binding.tvStockName.text = item.name
                binding.tvStockCode.text = item.code
                binding.tvProportion.text = String.format("%.2f%%", item.proportion)
                
                val changeStr = String.format("%.2f%%", item.change)
                binding.tvChange.text = if (item.change >= 0) "+$changeStr" else changeStr
                
                val colorRes = if (item.change >= 0) {
                    android.R.color.holo_red_dark
                } else {
                    android.R.color.holo_green_dark
                }
                binding.tvChange.setTextColor(itemView.context.getColor(colorRes))
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
            val binding = StockItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return StockViewHolder(binding)
        }

        override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FundViewHolder {
        val binding = FundItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FundViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FundViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FundDiffCallback : DiffUtil.ItemCallback<FundWithAmount>() {
        override fun areItemsTheSame(oldItem: FundWithAmount, newItem: FundWithAmount): Boolean {
            return oldItem.fund.code == newItem.fund.code
        }

        override fun areContentsTheSame(oldItem: FundWithAmount, newItem: FundWithAmount): Boolean {
            return oldItem == newItem
        }
    }

    class StockDiffCallback : DiffUtil.ItemCallback<Stock>() {
        override fun areItemsTheSame(oldItem: Stock, newItem: Stock): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: Stock, newItem: Stock): Boolean {
            return oldItem == newItem
        }
    }
}