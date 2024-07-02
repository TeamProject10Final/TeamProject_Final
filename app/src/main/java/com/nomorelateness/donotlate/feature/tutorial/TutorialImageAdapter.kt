package com.nomorelateness.donotlate.feature.tutorial

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.databinding.ItemTutorialBinding

class TutorialImageAdapter :
    RecyclerView.Adapter<TutorialImageAdapter.PagerViewHolder>() {

        val imageList = arrayOf(
            R.drawable.tutorial_1,
            R.drawable.tutorial_2,
            R.drawable.tutorial_3,
            R.drawable.tutorial_4,
            R.drawable.tutorial_5,
            R.drawable.tutorial_6,
            R.drawable.tutorial_7,
            R.drawable.tutorial_8,
        )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TutorialImageAdapter.PagerViewHolder {
        val binding = ItemTutorialBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TutorialImageAdapter.PagerViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class PagerViewHolder(
        private val binding: ItemTutorialBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Int) {
            binding.ivTutorial.setImageResource(item)
        }
    }

}