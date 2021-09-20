package my.com.fashionappstaff.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import my.com.fashionappstaff.data.Cart
import my.com.fashionappstaff.data.Product

class CartViewModel: ViewModel() {

    private val col = Firebase.firestore.collection("carts")
    private val carts = MutableLiveData<List<Cart>>()
    private var searchPro = listOf<Cart>()
    private val result = MutableLiveData<List<Cart>>()
    private var name = "" // for searching purpose

    init {
        col.addSnapshotListener { snap, _ -> carts.value = snap?.toObjects()
            searchPro = snap!!.toObjects<Cart>()
            updateResult()}
    }

    // Search
    fun search(name: String){
        this.name = name
        updateResult()
    }

    private fun updateResult() {
        var list = searchPro

        //Search
        list = list.filter {
            it.cartProductName.contains(name, true)
        }

        result.value = list
    }

    fun getResult() = result

    fun get(id : String): Cart?{
        return carts.value?.find { c -> c.cartID == id }
    }

    fun getAll() = carts

    fun delete(id : String){
        col.document(id).delete()
    }

    fun deleteAll(){
        carts.value?.forEach{ c -> delete(c.cartID)}
    }

    fun set(c: Cart){
        col.document(c.cartID).set(c)
    }

    fun calSize() = carts.value?.size ?: 0

    //-------------------------------------------------------------------
    // Validation

    private fun idExists(id: String): Boolean {
        return carts.value?.any{ c -> c.cartID == id } ?: false
    }

    fun validID(id: String): String {
        val newID: String
        val getLastCart = carts.value?.lastOrNull()?.cartID.toString()

        return if (idExists(id)) {
            val num: String = getLastCart.substringAfterLast("CART")
            newID = "CART" + (num.toIntOrNull()?.plus(1)).toString()
            newID
        } else {
            newID = "CART" + (calSize() + 1).toString()
            newID
        }
    }
    fun validID(): String {
        var newID: String

        val getLastCart = carts.value?.lastOrNull()?.cartID.toString()
        val num: String = getLastCart.substringAfterLast("CART10")
        newID = "CART10" + (num.toIntOrNull()?.plus(1)).toString()

        if (newID == "CART1010") {
            newID = "CART1" + (num.toIntOrNull()?.plus(1)).toString()
            return newID
        }

        return when (calSize()) {
            0 -> {
                newID = "CART10" + (calSize() + 1)
                newID
            }
            in 1..8 -> {
                val getLastCart = carts.value?.lastOrNull()?.cartID.toString()
                val num: String = getLastCart.substringAfterLast("CART10")
                newID = "CART10" + (num.toIntOrNull()?.plus(1)).toString()
                if (newID == "CART10null") {
                    newID = "CART111"
                    newID
                } else newID
            }
            else -> {
                val getLastCart = carts.value?.lastOrNull()?.cartID.toString()
                val num: String = getLastCart.substringAfterLast("CART1")
                newID = "CART1" + (num.toIntOrNull()?.plus(1)).toString()
                newID
            }
        }
    }

}