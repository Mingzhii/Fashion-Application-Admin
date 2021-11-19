package my.com.fashionapp.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.Order
import my.com.fashionappstaff.data.Product

class DeliveryAdapter (val fn: (ViewHolder, Order) -> Unit = { _, _ -> })
    :  ListAdapter<Order, DeliveryAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(a: Order, b: Order)    = a.orderId == b.orderId
        override fun areContentsTheSame(a: Order, b: Order) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val imgPhoto      : ImageView = view.findViewById(R.id.imageView3)
        val txtProductName: TextView = view.findViewById(R.id.txtOrderProductName)
        val txtPrice      : TextView = view.findViewById(R.id.txtOrderProductPrice)
        val txtQuantity   : TextView = view.findViewById(R.id.txtOrderProductQuantity)
        val txtStatus     : TextView = view.findViewById(R.id.txtOrderStatus)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = getItem(position)

        // TODO: Photo (blob to bitmap)a
//        holder.imgPhoto.setImageBitmap(order..toBitmap())

        holder.txtQuantity.text = "X " + order.orderProductQuantity
        holder.txtStatus.text = order.orderStatus
        holder.txtPrice.text = "RM " + order.orderProductTotalPrice

        fn(holder, order)
    }
}