package my.com.fashionappstaff.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.fashionapp.data.Product
import my.com.fashionappstaff.R

class ProductAdapter (val fn: (ViewHolder, Product) -> Unit = { _, _ -> })
    :  ListAdapter<Product, ProductAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(a: Product, b: Product)    = a.productId == b.productId
        override fun areContentsTheSame(a: Product, b: Product) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val imgPhoto     : ImageView = view.findViewById(R.id.imgProductPic)
        val txtName      : TextView = view.findViewById(R.id.txtProductName)
        val txtDescrip   : TextView = view.findViewById(R.id.txtDesciprion)
        val txtPrice     : TextView = view.findViewById(R.id.txtProductPrice)
//        val txtId      : TextView = view.findViewById(R.id.txtId)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)

//        holder.txtId.text   = friend.id
        holder.txtName.text = product.productName
        holder.txtDescrip.text  = product.productDescrip
        holder.txtPrice.text  = "RM %.2f".format(product.productPrice)

        // TODO: Photo (blob to bitmap)a
        holder.imgPhoto.setImageBitmap(product.productPhoto.toBitmap())

        fn(holder, product)
    }

}