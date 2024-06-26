package com.nomorelateness.donotlate.feature.setting.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.donotlate.databinding.ItemLicenseBinding
import com.example.donotlate.feature.setting.presentation.view.model.LicenseModel

class LicenseAdapter() :
    RecyclerView.Adapter<LicenseAdapter.Holder>() {

    var itemList = listOf<LicenseModel>()


    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemLicenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    var itemClick: ItemClick? = null

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentItem = itemList[position]
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position)
        }

        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class Holder(private val binding: ItemLicenseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LicenseModel) {
            binding.tvItemLicense.text = item.license
            binding.tvItemLicenseUri.text = item.uri
        }
    }

}