package com.example.donotlate.feature.directionRoute.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.donotlate.databinding.ItemDirectionBinding

class DirectionsAdapter() : RecyclerView.Adapter<DirectionsAdapter.MyViewHolder>() {

    var itemList: List<String> = listOf()

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    var itemClick: OnItemClickListener? = null

    class MyViewHolder(
        private val binding: ItemDirectionBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.tvDirection1.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemDirectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(itemList[position])
        holder.itemView.setOnClickListener {
            itemClick?.onItemClick(it, position)
        }
    }

    fun setItem(data: List<String>) {
        this.itemList = data
        notifyDataSetChanged()
    }
}