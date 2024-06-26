package com.nomorelateness.donotlate.feature.friends.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.donotlate.databinding.ItemRvFriendBinding
import com.example.donotlate.feature.friends.presentation.model.FriendsUserModel

class SearchUserAdapter(
    private val onItemClick: (FriendsUserModel) -> Unit
) : ListAdapter<FriendsUserModel, SearchUserAdapter.SearchUserViewHolder>(object :
    DiffUtil.ItemCallback<FriendsUserModel>() {
    override fun areItemsTheSame(oldItem: FriendsUserModel, newItem: FriendsUserModel): Boolean {
        return oldItem.uid == newItem.uid // 고유 ID 비교
    }

    override fun areContentsTheSame(oldItem: FriendsUserModel, newItem: FriendsUserModel): Boolean {
        return oldItem == newItem // 전체 항목 비교

    }
}) {

    inner class SearchUserViewHolder(
        private val binding: ItemRvFriendBinding,
        private val onItemClick: (FriendsUserModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val user = getItem(position)
                    onItemClick(user)
                }
            }
        }


        fun bind(user: FriendsUserModel) {
            binding.tvItemFriend.text = user.name

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        val binding =
            ItemRvFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchUserViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) {
        val currentUser = getItem(position)
        holder.bind(currentUser)
    }
}