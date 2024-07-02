package com.nomorelateness.donotlate.feature.friends.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nomorelateness.donotlate.core.presentation.util.toFormattedString
import com.nomorelateness.donotlate.databinding.ItemFirendRequestBinding
import com.nomorelateness.donotlate.feature.friends.presentation.model.FriendRequestWithUserDataModel

class FriendRequestsAdapter(
    private var friendRequestList: List<FriendRequestWithUserDataModel>,
    private val onItemClick: (FriendRequestWithUserDataModel) -> Unit
) : RecyclerView.Adapter<FriendRequestsAdapter.FriendRequestsViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestsViewHolder{
        val binding = ItemFirendRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendRequestsViewHolder(binding, onItemClick)
    }

    override fun getItemCount(): Int {
        return friendRequestList.size
    }

    override fun onBindViewHolder(holder: FriendRequestsViewHolder, position: Int) {
        holder.bind(friendRequestList[position])
    }

    fun updateList(newList: List<FriendRequestWithUserDataModel>){
        friendRequestList = newList
        notifyDataSetChanged()
    }

    inner class FriendRequestsViewHolder(
        private val binding: ItemFirendRequestBinding,
        private val onItemClick: (FriendRequestWithUserDataModel) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item:FriendRequestWithUserDataModel){
            binding.tvRequestName.text = item.friendRequestModel.fromUserName
            binding.tvRequestEmail.text = item.userDataModel.email
            binding.tvRequestEmailDate.text = item.friendRequestModel.requestTime.toFormattedString()
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}