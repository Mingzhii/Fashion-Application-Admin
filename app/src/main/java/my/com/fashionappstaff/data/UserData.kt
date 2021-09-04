package my.com.fashionapp.data

import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId
import java.util.*

data class User (
    @DocumentId
    var userId      : String = "",
    var email       : String = "",
    var password    : String = "",
    var userName    : String = "",
    var phoneNumber : String = "",
    var userPhoto   : Blob = Blob.fromBytes(ByteArray(0)),
    var homeAddress : String = "",
    var userType    : String = "",
)