package com.nomorelateness.donotlate.feature.mypromise.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nomorelateness.donotlate.databinding.ItemRvMyPromiseBinding
import com.nomorelateness.donotlate.feature.mypromise.presentation.model.PromiseModel

class MyPromiseAdapter(
    private val onItemClick: (PromiseModel) -> Unit,
    private val onItemLongClick: (PromiseModel) -> Unit
) :
    ListAdapter<PromiseModel, MyPromiseAdapter.MyPromiseViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPromiseViewHolder {
        val binding =
            ItemRvMyPromiseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyPromiseViewHolder(binding, onItemClick, onItemLongClick)
    }

    override fun onBindViewHolder(holder: MyPromiseViewHolder, position: Int) {
        val promiseRoom = getItem(position)
        holder.bind(promiseRoom)
    }

    class MyPromiseViewHolder(
        private val binding: ItemRvMyPromiseBinding,
        private val onItemClick: (PromiseModel) -> Unit,
        private val onItemLongClick: (PromiseModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PromiseModel) {
            binding.tvPromisTitle.text = item.roomTitle
            binding.tvPromiseDate.text = item.promiseDate.toString()
            binding.root.setOnClickListener {
                onItemClick(item)
            }
            binding.root.setOnLongClickListener {
                onItemLongClick(item)
                true // true를 반환하여 이벤트 소비된거 알리기
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