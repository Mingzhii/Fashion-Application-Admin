package my.com.fashionapp.data

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import my.com.fashionappstaff.data.Cart
import my.com.fashionappstaff.data.Product
import java.util.*

class ProductViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("products")
    private val products = MutableLiveData<List<Product>>()
    private var searchPro = listOf<Product>()
    private val result = MutableLiveData<List<Product>>()
    private var name = "" // for searching purpose

    data class Category (
        var id: String,
        var name: String,
    )
    init {
        col.addSnapshotListener { snap, _ -> products.value = snap?.toObjects()
            searchPro = snap!!.toObjects<Product>()
            updateResult() }
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
            it.productName.contains(name, true)
        }
        result.value = list
    }

    fun getResult() = result

    fun get(id : String): Product?{
        return products.value?.find { p -> p.productId == id }
    }

    fun getAll() = products

    fun delete(id : String){
        col.document(id).delete()
    }

    fun deleteAll(){
        products.value?.forEach{ p -> delete(p.productId)}
    }

    fun set(p: Product){
        col.document(p.productId).set(p)
    }

    // Calculate the size of products list
    fun calSize() = products.value?.size ?: 0

    val category = listOf(
        Category("Women", "women"),
        Category("Men", "men"),
    )



    //-------------------------------------------------------------------
    // Validation

    private fun idExists(id: String): Boolean {
        return products.value?.any{ p -> p.productId == id } ?: false
    }

    fun validID(): String {
        var newID: String

        val getLastProduct = products.value?.lastOrNull()?.productId.toString()
        val num: String = getLastProduct.substringAfterLast("PROD10")
        newID = "PROD10" + (num.toIntOrNull()?.plus(1)).toString()

        if (newID == "PROD1010") {
            newID = "PROD1" + (num.toIntOrNull()?.plus(1)).toString()
            return newID
        }

        return when (calSize()) {
            0 -> {
                newID = "PROD10" + (calSize() + 1)
                newID
            }
            in 1..8 -> {
                val getLastProduct = products.value?.lastOrNull()?.productId.toString()
                val num: String = getLastProduct.substringAfterLast("PROD10")
                newID = "PROD10" + (num.toIntOrNull()?.plus(1)).toString()
                if (newID == "PROD10null") {
                    newID = "PROD111"
                    newID
                } else newID
            }
            else -> {
                val getLastProduct = products.value?.lastOrNull()?.productId.toString()
                val num: String = getLastProduct.substringAfterLast("PROD1")
                newID = "PROD1" + (num.toIntOrNull()?.plus(1)).toString()
                newID
            }
        }
    }

    private fun nameExists(name: String): Boolean {
        return products.value?.any{ p -> p.productName == name } ?: false
    }

    fun validate(p: Product, insert: Boolean = true): String {
        var e = ""
        //name
        e += if (p.productName == "") "- Product Name is required.\n"
        else if (p.productName.length < 3) "- Product Name is too short.\n"
        else if (nameExists(p.productName)) "- Product Name is duplicated.\n"
        else ""
        //Description
        e += if (p.productDescrip == "") "- Description is required.\n"
        else if (p.productDescrip.length < 3) "- Description is to short.\n"
        else ""
        //Quantity
        e += if (p.productQuan == 0) "- Quantity cannot be 0. \n"
        else ""
        //Price
        e += if (p.productPrice == 0.0 ) "- Price cannot be 0.0. \n"
        else ""
        //Photo
        e += if (p.productPhoto.toBytes().isEmpty()) "- Photo is required.\n"
        else ""
        return e
    }



}