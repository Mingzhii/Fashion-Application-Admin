package my.com.fashionappstaff.data

import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId
import java.util.*

var email1 : String = ""
var username : String = ""
var img : Blob = Blob.fromBytes(ByteArray(0))

data class Reward(
    @DocumentId
    var rewardID      : String = "",
    var rewardName    : String = "",
    var rewardDescrip : String = "",
    var rewardQuan    : Int = 0,
    var rewardPoint   : Double = 0.0,
    var date          : Date = Date(),
    var expiryDate    : String = "",
    var rewardPhoto   : Blob = Blob.fromBytes(ByteArray(0)),
)

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
    var userPoint   : Double = 0.0,
)

data class Product (
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

data class Cart (
    @DocumentId
    var cartID : String = "",
    var cartUsername : String = "",
    var cartProductID : String = "",
    var cartProductName : String = "",
    var cartProductQuantity : Int = 0,
    var cartProductPrice : Double = 0.0,
    var cartProductSize : String = "",
    var cartProductPhoto:  Blob = Blob.fromBytes(ByteArray(0)),
    var cartStatus : String = "",
)

data class OrderList (
    @DocumentId
    var orderProductID : String = "",
    var orderCartID    : String = "",
    var orderProductQuantity : Int = 0,
)

data class Payment (
    @DocumentId
    var paymentID : String = "",
    var userName  : String = "",
    var paymentMethod : String = "",
    var totalPrice    : Double = 0.0,
    var paymentDate   : Date = Date(),
)