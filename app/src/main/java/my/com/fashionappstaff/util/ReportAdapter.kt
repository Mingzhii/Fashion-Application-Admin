package my.com.fashionappstaff.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.Cart

class ReportAdapter(val fn: (ViewHolder, Cart) -> Unit = { _, _ -> })
    :  ListAdapter<Cart, ReportAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(a: Cart, b: Cart)    = a.cartID == b.cartID
        override fun areContentsTheSame(a: Cart, b: Cart) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val txtProductID : TextView = view.findViewById(R.id.txtProductID)
        val txtName      : TextView = view.findViewById(R.id.txtProductName)
        val txtQuantity  : TextView = view.findViewById(R.id.txtProductQuantity)
        val txtPrice     : TextView = view.findViewById(R.id.txtTotalPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_report, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cart = getItem(position)

        holder.txtProductID.text   = cart.cartProductID
        holder.txtName.text = cart.cartProductName
        holder.txtQuantity.text = cart.cartProductQuantity.toString()
        holder.txtPrice.text = "RM " + "%.2f".format(cart.cartProductPrice)


        fn(holder, cart)
    }
}