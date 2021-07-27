package com.zetamp.crudtask1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.RecyclerView

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var userList: ArrayList<UserModel> = ArrayList()
    private var onClickItem: ((UserModel) -> Unit)? = null
    private var onClickDeleteItem: ((UserModel) -> Unit)? = null

    fun addItems(items: ArrayList<UserModel>) {
        this.userList = items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (UserModel) -> Unit) {
        this.onClickItem = callback
    }
    fun setOnClickDeleteItem(callback: (UserModel) -> Unit) {
        this.onClickDeleteItem = callback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.card_items, parent, false)
    )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bindView(user)
        holder.itemView.setOnClickListener { onClickItem?.invoke(user) }
        holder.btnDelete.setOnClickListener { onClickDeleteItem?.invoke(user) }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var id = view.findViewById<TextView>(R.id.textId)
        private var email = view.findViewById<TextView>(R.id.textEmail)
        private var name = view.findViewById<TextView>(R.id.textName)
        private var date = view.findViewById<TextView>(R.id.textDate)
        private var time = view.findViewById<TextView>(R.id.textTime)
        var btnDelete = view.findViewById<Button>(R.id.btnDelete)


        fun bindView(user: UserModel) {
            id.text = user.id.toString()
            email.text = user.email
            name.text = user.name
            date.text = user.date
            time.text = user.time
        }

    }

}