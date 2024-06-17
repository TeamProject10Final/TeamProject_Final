package com.example.donotlate.feature.mypromise.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.donotlate.databinding.ItemRvMyPromiseBinding
import com.example.donotlate.feature.mypromise.presentation.model.PromiseModel

class MyPromiseAdapter(private val onItemClick: (PromiseModel) -> Unit) :
    ListAdapter<PromiseModel, MyPromiseAdapter.MyPromiseViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPromiseViewHolder {
        val binding =
            ItemRvMyPromiseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyPromiseViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: MyPromiseViewHolder, position: Int) {
        val promiseRoom = getItem(position)
        holder.bind(promiseRoom)
    }

    class MyPromiseViewHolder(
        private val binding: ItemRvMyPromiseBinding,
        private val onItemClick: (PromiseModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PromiseModel) {
            binding.tvPromisTitle.text = item.roomTitle
            binding.tvPromiseDate.text = item.promiseDate
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }


    }

    class DiffCallback : DiffUtil.ItemCallback<PromiseModel>() {
        override fun areItemsTheSame(oldItem: PromiseModel, newItem: PromiseModel): Boolean {
            return oldItem.roomTitle == newItem.roomTitle
        }

        override fun areContentsTheSame(oldItem: PromiseModel, newItem: PromiseModel): Boolean {
            return oldItem == newItem
        }
    }
}