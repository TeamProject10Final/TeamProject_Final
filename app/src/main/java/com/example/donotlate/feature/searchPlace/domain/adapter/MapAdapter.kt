package com.example.donotlate.feature.searchPlace.domain.adapter

import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.donotlate.MainActivity
import com.example.donotlate.R
import com.example.donotlate.databinding.ItemRvPlaceBinding
import com.example.donotlate.feature.searchPlace.presentation.data.PlaceModel

class MapAdapter : RecyclerView.Adapter<MapAdapter.MyViewHolder>() {

    var itemList: List<PlaceModel> = listOf()
    private var activity: MainActivity? = null

    interface OnItemClickListener {
        fun onItemClick(mapData: PlaceModel)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    class MyViewHolder(
        private val binding: ItemRvPlaceBinding,
        private val onClick: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PlaceModel) {


            binding.ivItemMap.load(item.img) {
                crossfade(true)
                transformations(RoundedCornersTransformation(20f))
            }

            binding.tvItemMapTitle.text = item.name
            binding.tvItemMapAddress.text = item.address
            binding.tvItemMapNumber.text = item.phoneNumber

            if (item.rating == null) {
                binding.tvItemMapRating.text = "0.0"
            } else binding.tvItemMapRating.text = item.rating.toString()

            if (item.phoneNumber != null) {
                binding.tvItemMapNumber.text = item.phoneNumber
            } else binding.tvItemMapNumber.text = "번호가 제공되지 않습니다."

            itemView.setOnClickListener {
                onClick.onItemClick(item)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemRvPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    fun setItem(data: List<PlaceModel>) {
        this.itemList = data
        notifyDataSetChanged()
    }
}