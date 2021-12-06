package my.com.fashionappstaff.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class VoucherViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("vouchers")
    private val voucher = MutableLiveData<List<Voucher>>()
    private var searchVoucher = listOf<Voucher>()
    private val result = MutableLiveData<List<Voucher>>()
    private var name = "" // for searching purpose

    init {
        col.addSnapshotListener { snap, _ -> voucher.value = snap?.toObjects()
            searchVoucher = snap!!.toObjects<Voucher>()
            updateResult() }
    }

    // Search
    fun search(name: String){
        this.name = name
        updateResult()
    }

    private fun updateResult() {
        var list = searchVoucher

        //Search
        list = list.filter {
            it.voucherName.contains(name, true)
        }

        result.value = list
    }

    fun getResult() = result

    fun get(id : String): Voucher?{
        return voucher.value?.find { v -> v.voucherId == id }
    }

    fun getAll() = voucher

    fun delete(id : String){
        col.document(id).delete()
    }

    fun set(v: Voucher){
        col.document(v.voucherId).set(v)
    }

    // Calculate the size of products list
    fun calSize() = voucher.value?.size ?: 0

    fun validID(): String {
        var newID: String

        val getLastReward = voucher.value?.lastOrNull()?.voucherId.toString()
        val num: String = getLastReward.substringAfterLast("VOU10")
        newID = "VOU10" + (num.toIntOrNull()?.plus(1)).toString()

        if (newID == "VOU1010") {
            newID = "VOU1" + (num.toIntOrNull()?.plus(1)).toString()
            return newID
        }

        return when (calSize()) {
            0 -> {
                newID = "VOU10" + (calSize() + 1)
                newID
            }
            in 1..8 -> {
                val getLastReward = voucher.value?.lastOrNull()?.voucherId.toString()
                val num: String = getLastReward.substringAfterLast("VOU10")
                newID = "VOU10" + (num.toIntOrNull()?.plus(1)).toString()
                if (newID == "VOU10null") {
                    newID = "VOU111"
                    newID
                } else newID
            }
            else -> {
                val getLastReward = voucher.value?.lastOrNull()?.voucherId.toString()
                val num: String = getLastReward.substringAfterLast("VOU1")
                newID = "VOU1" + (num.toIntOrNull()?.plus(1)).toString()
                newID
            }
        }
    }

    fun validate(v: Voucher, insert: Boolean = true): String {
        val currentDate = LocalDate.now()
        val viewFormatter = DateTimeFormatter.ofPattern("dd MMM, YYYY")
        val formatDate = viewFormatter.format(currentDate)
        var e = ""
        val Date = Date()
        //name
        e += if (v.voucherName == "") "- Voucher Name is required.\n"
        else if (v.voucherName.length < 3) "- Voucher Name is too short.\n"
        else ""
        //Description
        e += if (v.voucherDescription == "") "- Description is required.\n"
        else if (v.voucherDescription.length < 3) "- Description is to short.\n"
        else ""
        //Quantity
        e += if (v.voucherQuantity == 0) "- Quantity cannot be 0. \n"
        else ""
        //Point
        e += if (v.voucherValue == 0.0 ) "- Point cannot be 0. \n"
        else ""
        //Expiry Date
        e += if (v.voucherExpiryDate == formatDate) "- Expiry date cannot be today. \n"
        else if (v.voucherExpiryDate == "") "- Please Select Expiry Date. \n"
        else ""
        //Photo
        e += if (v.voucherImg.toBytes().isEmpty()) "- Voucher Photo is required.\n"
        else ""
        return e
    }

}