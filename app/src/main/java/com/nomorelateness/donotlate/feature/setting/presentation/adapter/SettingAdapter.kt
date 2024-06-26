package com.nomorelateness.donotlate.feature.setting.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.donotlate.databinding.ItemSettingBinding

class SettingAdapter(private val settingItem: ArrayList<String>, private val dateItem: String?) :
    RecyclerView.Adapter<SettingAdapter.Holder>() {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemSettingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    var itemClick: ItemClick? = null

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position)
        }
        holder.tvItem.text = settingItem[position]

        if (dateItem.isNullOrEmpty()) {
            holder.tvDate.isVisible = false
        } else {
            holder.tvDate.isVisible = true
            holder.tvDate.text = dateItem
        }
    }

    override fun getItemCount(): Int {
        return settingItem.size
    }

    class Holder(binding: ItemSettingBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvItem = binding.tvItem
        val tvDate = binding.tvDate
    }
}