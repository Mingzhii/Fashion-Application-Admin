package my.com.fashionapp.data

import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId
import java.util.*

data class Cart (
    @DocumentId

    var cartUserId         : String = "",
    var cartProductId      : String = "",
    var cartProductQuan    : Int = 0,
    var cartProductPrice   : Double = 0.0,
)
