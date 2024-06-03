package com.example.donotlate.consumption

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.donotlate.consumption.ConsumptionActivity.Companion.addCommas
import com.example.donotlate.databinding.ItemConsumptionBinding

class ConsumptionAdapter : RecyclerView.Adapter<ConsumptionAdapter.ViewHolder>() {
    private val items = mutableListOf<ConsumptionModel>()

    inner class ViewHolder(private val binding: ItemConsumptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ConsumptionModel) {
            binding.apply {
                tvDate.text = model.date
                tvDetail.text = model.detail
                tvPrice.text = model.price.addCommas()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemConsumptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newList: List<ConsumptionModel>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}