package my.com.fashionappstaff.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.Voucher

class VoucherAdapter (val fn: (VoucherAdapter.ViewHolder, Voucher) -> Unit = { _, _ -> })
    :  ListAdapter<Voucher, VoucherAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Voucher>() {
        override fun areItemsTheSame(a: Voucher, b: Voucher)    = a.voucherId == b.voucherId
        override fun areContentsTheSame(a: Voucher, b: Voucher) = a.voucherId == b.voucherId
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val imgPhoto     : ImageView = view.findViewById(R.id.imgVoucher)
        val txtName      : TextView = view.findViewById(R.id.voucherName)
        val txtExpiry    : TextView = view.findViewById(R.id.txtExpiryDate)
        val btnClaim     : Button = view.findViewById(R.id.btnClaim)
        val txtTerm      : TextView = view.findViewById(R.id.txtTerm)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_voucher, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val voucher = getItem(position)

        holder.imgPhoto.setImageBitmap(voucher.voucherImg.toBitmap())
        holder.txtName.text = voucher.voucherName
        holder.txtExpiry.text = "Valid " + voucher.voucherExpiryDate

        // TODO: Photo (blob to bitmap)a
        fn(holder, voucher)
    }
}