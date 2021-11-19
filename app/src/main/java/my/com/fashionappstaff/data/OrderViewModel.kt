package my.com.fashionapp.data

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import my.com.fashionappstaff.data.Cart
import my.com.fashionappstaff.data.Order
import my.com.fashionappstaff.data.Product
import java.util.*

class OrderViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("orders")
    private val orders = MutableLiveData<List<Order>>()
    private var searchPro = listOf<Order>()
    private var name = ""

    //init block will always run before the constructor
    init {
        col.addSnapshotListener { snap, _ -> orders.value = snap?.toObjects() }
    }

    fun get(id: String): Order? {
        return orders.value?.find { o -> o.orderId == id }
    }

    fun getAll() = orders

    fun delete(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        orders.value?.forEach { o -> delete(o.orderId) }
    }

    fun updateResult() {
        var list = searchPro

        //Search
        list = list.filter {
            it.orderStatus.contains(name, true)
        }
        orders.value = list
    }

    fun set(o: Order) {
        col.document(o.orderId).set(o)
    }

    fun calSize() = orders.value?.size ?: 0

    //Validation
    //-----------------------------------

    private fun idExists(id: String): Boolean {
        return orders.value?.any { o -> o.orderId == id } ?: false
    }

    fun validID(): String {
        var newID: String

        val getLastProduct = orders.value?.lastOrNull()?.orderId.toString()
        val num: String = getLastProduct.substringAfterLast("ORD10")
        newID = "ORD10" + (num.toIntOrNull()?.plus(1)).toString()

        if (newID == "ORD1010") {
            newID = "ORD1" + (num.toIntOrNull()?.plus(1)).toString()
            return newID
        }

        return when (calSize()) {
            0 -> {
                newID = "ORD10" + (calSize() + 1)
                newID
            }
            in 1..8 -> {
                val getLastProduct = orders.value?.lastOrNull()?.orderId.toString()
                val num: String = getLastProduct.substringAfterLast("ORD10")
                newID = "ORD10" + (num.toIntOrNull()?.plus(1)).toString()
                if (newID == "ORD10null") {
                    newID = "ORD111"
                    newID
                } else newID
            }
            else -> {
                val getLastProduct = orders.value?.lastOrNull()?.orderId.toString()
                val num: String = getLastProduct.substringAfterLast("ORD1")
                newID = "ORD1" + (num.toIntOrNull()?.plus(1)).toString()
                newID
            }
        }
    }


}