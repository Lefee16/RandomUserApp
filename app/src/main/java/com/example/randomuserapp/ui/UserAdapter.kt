package com.example.randomuserapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.randomuserapp.R
import com.example.randomuserapp.room.UserEntity

class UserAdapter(private val userList: MutableList<UserEntity>): RecyclerView.Adapter<UserAdapter.UserViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        val view = LayoutInflater.from(
            parent.context).inflate(
            R.layout.user_item,
            parent,
            false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]

        holder.tvName.text = user.name
        holder.tvPhone.text = user.phone
        holder.tvLocation.text = user.location
        Glide
            .with(holder.ivPicture.context)
            .load(user.picture)
            .into(holder.ivPicture)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, UserDetailActivity::class.java)
            intent.putExtra("name", user.name)
            intent.putExtra("phone", user.phone)
            intent.putExtra("location", user.location)
            intent.putExtra("picture", user.picture)
            holder.itemView.context.startActivity(intent)
        }

        holder.tvPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, ("tel:${user.phone}").toUri())
            holder.itemView.context.startActivity(intent)
        }

        holder.tvLocation.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, ("geo:0,0?q=${user.location}").toUri())
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivPicture: ImageView = itemView.findViewById(R.id.ivPicture)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvPhone: TextView = itemView.findViewById(R.id.tvPhone)
        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<UserEntity>){
        userList.clear()
        userList.addAll(newList)
        notifyDataSetChanged()
    }
}