package com.nomorelateness.donotlate.feature.consumption.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nomorelateness.donotlate.databinding.ItemConsumptionBinding
import com.nomorelateness.donotlate.feature.consumption.presentation.ConsumptionActivity.Companion.addCommas

class ConsumptionAdapter(private val onItemClick: (ConsumptionModel)-> Unit) : RecyclerView.Adapter<ConsumptionAdapter.ViewHolder>() {
    private val items = mutableListOf<ConsumptionModel>()

    inner class ViewHolder(private val binding: ItemConsumptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ConsumptionModel) {
            binding.apply {
                tvDate.text = model.date
                tvDetail.text = model.detail
                tvPrice.text = model.price.addCommas()

                root.setOnClickListener{onItemClick(model)}
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemConsumptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newList: List<ConsumptionModel>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
    fun getItemAtPosition(position: Int): ConsumptionModel {
        return items[position]
    }
}