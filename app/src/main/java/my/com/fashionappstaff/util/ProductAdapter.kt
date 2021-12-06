package my.com.fashionappstaff.util

import android.location.LocationManager
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
import my.com.fashionappstaff.data.Product

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
        val txtPrice     : TextView = view.findViewById(R.id.txtProductPrice)
        val btnDelete: Button = view.findViewById(R.id.btnListProductDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)

        holder.txtName.text = product.productName
        holder.txtPrice.text  = "RM %.2f".format(product.productPrice)
        holder.imgPhoto.setImageBitmap(product.productPhoto.toBitmap())

        fn(holder, product)
    }

}