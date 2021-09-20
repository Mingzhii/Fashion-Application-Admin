package my.com.fashionappstaff.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.Cart

class HistoryAdapter (val fn: (HistoryAdapter.ViewHolder, Cart) -> Unit = { _, _ -> })
    :  ListAdapter<Cart, HistoryAdapter.ViewHolder>(HistoryAdapter) {

    companion object DiffCallback : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(a: Cart, b: Cart)    = a.cartID == b.cartID
        override fun areContentsTheSame(a: Cart, b: Cart) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val imgPhoto     : ImageView = view.findViewById(R.id.imgHistoryProduct)
        val txtName      : TextView = view.findViewById(R.id.txtProductHistoryName)
        val txtPrice     : TextView = view.findViewById(R.id.txtProductHistoryPrice)
        val txtQuantity  : TextView = view.findViewById(R.id.txtProductHistoryQuantity)
        val txtSize      : Button = view.findViewById(R.id.btnProductHistorySize)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_payment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cart = getItem(position)

//        holder.txtId.text   = friend.id

        holder.imgPhoto.setImageBitmap(cart.cartProductPhoto.toBitmap())
        holder.txtName.text = cart.cartProductName
        holder.txtQuantity.text = cart.cartProductQuantity.toString()
        holder.txtSize.text = cart.cartProductSize
        holder.txtPrice.text  = "%.2f".format(cart.cartProductPrice)

        // TODO: Photo (blob to bitmap)a
        fn(holder, cart)
    }


}