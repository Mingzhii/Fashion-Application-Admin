package my.com.fashionapp.data

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import java.util.*

class ProductViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("products")
    private val products = MutableLiveData<List<Product>>()

    data class Category (
        var id: String,
        var name: String,
    )

    init {
        col.addSnapshotListener { snap, _ -> products.value = snap?.toObjects()  }
    }

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

    private fun nameExists(name: String): Boolean {
        return products.value?.any{ p -> p.productName == name } ?: false
    }

    fun validate(p: Product, insert: Boolean = true): String {
//        val regexId = Regex("""^[0-9A-Z]{4}$""")
        var e = ""

        //id
//        if (insert) {
//            e += if (f.id == "") "- Id is required.\n"
//            else if (!f.id.matches(regexId)) "- Id format is invalid.\n"
//            else if (idExists(f.id)) "- Id is duplicated.\n"
//            else ""
//        }

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