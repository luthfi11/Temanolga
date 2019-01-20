package com.wysiwyg.temanolga.ui.searchuser

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wysiwyg.temanolga.R
import com.wysiwyg.temanolga.ui.userdetail.UserDetailActivity
import com.wysiwyg.temanolga.data.network.FirebaseApi
import com.wysiwyg.temanolga.data.model.User
import kotlinx.android.synthetic.main.item_search_people.view.*
import org.jetbrains.anko.*

class SearchUserAdapter(private val users: MutableList<User?>) :
    RecyclerView.Adapter<SearchUserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_search_people,
                parent,
                false
            )
        )


    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bindItem(users[i])
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(user: User?) {

            FirebaseApi.getPostSender(user?.userId!!, itemView.tvUserSrc, null, itemView.imgUserSrc)

            itemView.setOnClickListener {
                itemView.context.startActivity<UserDetailActivity>("userId" to user.userId!!)
            }

        }
    }
}
