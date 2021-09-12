package my.com.fashionappstaff.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.User

class UserAdapter (val fn: (ViewHolder, User) -> Unit = { _, _ -> } )
    :  ListAdapter<User, UserAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(a: User, b: User)    = a.userId == b.userId
        override fun areContentsTheSame(a: User, b: User) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val txtStaffName : TextView = view.findViewById(R.id.txtStaffName)
        val txtRole : TextView = view.findViewById(R.id.txtRole)
        val btnDelete: Button = view.findViewById(R.id.btnListUserDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)

        holder.txtStaffName.text = user.userName
        holder.txtRole.text = user.userType

        fn(holder, user)
    }


}