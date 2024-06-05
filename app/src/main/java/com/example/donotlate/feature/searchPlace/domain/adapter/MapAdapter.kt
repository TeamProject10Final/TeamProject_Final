package com.example.donotlate.feature.searchPlace.domain.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.donotlate.MainActivity
import com.example.donotlate.databinding.ItemRvMapBinding
import com.example.donotlate.feature.searchPlace.presentation.PlaceDetailFragment
import com.example.donotlate.feature.searchPlace.presentation.SearchModel

class MapAdapter : RecyclerView.Adapter<MapAdapter.MyViewHolder>() {

    var itemList: List<SearchModel> = listOf()
    private var activity: MainActivity? = null

    interface OnItemClickListener {
        fun onItemClick(mapData: SearchModel)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    class MyViewHolder(
        private val binding: ItemRvMapBinding,
        private val onClick: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
            val title = binding.tvItemMapTitle
            val rating = binding.tvItemMapRating
            val address = binding.tvItemMapAddress
        fun bind(item: SearchModel) {

            with(binding.ivItemMap) {
                load(item.img) {
                    crossfade(true)
                }
                clipToOutline = true
            }
            binding.tvItemMapTitle.text = item.name
            binding.tvItemMapRating.text = item.rating
            binding.tvItemMapAddress.text = item.address
            binding.tvItemMapLat.text = item.lat
            binding.tvItemMapLng.text = item.lng

            itemView.setOnClickListener {
                onClick.onItemClick(item)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemRvMapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, listener!!)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(itemList[position])
        holder.itemView.setOnClickListener {
            listener?.onItemClick(itemList[position])
        }
    }

    fun setItem(data: List<SearchModel>) {
        this.itemList = data
        notifyDataSetChanged()
    }
}