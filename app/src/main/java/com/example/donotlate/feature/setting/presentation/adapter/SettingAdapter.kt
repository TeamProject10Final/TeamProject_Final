package com.example.donotlate.feature.setting.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import com.example.donotlate.databinding.ItemSetting2Binding
import com.example.donotlate.databinding.ItemSettingBinding
import com.example.donotlate.feature.setting.model.ListType
import com.example.donotlate.feature.setting.model.SettingListItemType1
import com.example.donotlate.feature.setting.model.SettingListItemType2

class SettingAdapter(private val settingItem: MutableList<ListType>) :

    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var itemClick: ItemClick? = null

//    private val vm: SettingViewModel by viewModels()

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val binding = ItemSettingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SettingListItemType1 -> {
                val binding = ItemSettingBinding.inflate(inflater, parent, false)
                MultiViewHolder1(binding)
            }
            SettingListItemType2 -> {
                val binding = ItemSetting2Binding.inflate(inflater, parent, false)
                MultiViewHolder2(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position)
        }
        when (settingItem[position].type) {
            SettingListItemType1 -> {
                (holder as MultiViewHolder1).bind(settingItem[position])
                holder.setIsRecyclable(false)
            }

            SettingListItemType2 -> {
                (holder as MultiViewHolder2).bind(settingItem[position])
                holder.setIsRecyclable(false)


            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return settingItem[position].type
    }

//    override fun onBindViewHolder(holder: Holder, position: Int) {
//        holder.itemView.setOnClickListener {
//            itemClick?.onClick(it,position)
//        }
//        holder.tvItem.text = settingItem[position]
//
//    }

    override fun getItemCount(): Int {
        return settingItem.size
    }

    inner class MultiViewHolder1(private val binding: ItemSettingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListType) {
            binding.tvItem.text = item.title
            //binding.imageView.setImageResource(R.drawable.back)
        }
    }

    inner class MultiViewHolder2(private val binding: ItemSetting2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListType) {
            binding.tvItem.text = item.title

            /*다크모드
             모드 변경이 될 때 뷰가 새로고침이 되어서 스위치가 제대로 움직이지 않음 -> 뷰모델에 스위치 상태 변수를 만들고 변수를 옵저빙하여 다크모드 변경*/
            binding.switch1.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked){

                    //스위치 on
                    Log.d("스위치 동작","스위치 on 다크모드")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }else{
                    //스위치 off
                    Log.d("스위치 동작","스위치 off 라이트모드")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }

    inner class Holder(binding: ItemSettingBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvItem = binding.tvItem
    }
}