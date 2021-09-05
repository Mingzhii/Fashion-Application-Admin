package my.com.fashionapp.data

import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId
import java.util.*

data class Product(
    @DocumentId
    var productId      : String = "",
    var productName    : String = "",
    var productDescrip : String = "",
    var productQuan    : Int = 0,
    var productPrice   : Double = 0.0,
    var productCategory: String = "",
    var date           : Date = Date(),
    var productPhoto   : Blob = Blob.fromBytes(ByteArray(0)),

    )
