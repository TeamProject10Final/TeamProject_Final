package com.example.donotlate.feature.setting.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.donotlate.databinding.ItemSettingBinding

class SettingAdapter(private val settingItem:ArrayList<String>):RecyclerView.Adapter<SettingAdapter.Holder>() {

    interface ItemClick{
        fun onClick(view : View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemSettingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    var itemClick : ItemClick? = null

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it,position)
        }
        holder.tvItem.text = settingItem[position]

    }

    override fun getItemCount(): Int {
        return settingItem.size
    }

    inner class Holder(binding: ItemSettingBinding):RecyclerView.ViewHolder(binding.root){
        val tvItem = binding.tvItem
    }
}