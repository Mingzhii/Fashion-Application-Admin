package my.com.fashionapp.data

import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId
import java.util.*

data class Payment(
    @DocumentId
    var paymentId     : String = "",
    var userId        : String = "",
    var paymentMethod : String = "",
    var totalPrice    : Double = 0.0,
    var paymentDate   : Date = Date(),

)