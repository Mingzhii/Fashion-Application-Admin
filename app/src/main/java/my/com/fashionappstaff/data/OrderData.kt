package my.com.fashionapp.data

import com.google.firebase.firestore.DocumentId


data class Order (
    // For Order Product just Put Product ID
    @DocumentId
    var orderID      : String = "",
    var userID       : String = "",
    var orderProduct : String = "",
    var orderProQuan : Int = 0,
    var orderPrice   : Double = 0.0,

)