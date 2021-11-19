package my.com.fashionapp.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import my.com.fashionappstaff.data.Payment
import my.com.fashionappstaff.data.User

class PaymentViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("payments")
    private val payments = MutableLiveData<List<Payment>>()

    //init block will always run before the constructor
    init {
        col.addSnapshotListener { snap, _ -> payments.value = snap?.toObjects() }
    }

    fun get(id: String): Payment? {
        return payments.value?.find { p -> p.paymentID == id }
    }

    fun getAll() = payments

    fun delete(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        payments.value?.forEach { p -> delete(p.paymentID) }
    }

    fun set(p: Payment) {
        col.document(p.paymentID).set(p)
    }

    fun calSize() = payments.value?.size ?: 0

    //Validation
    //-----------------------------------

    private fun idExists(id: String): Boolean {
        return payments.value?.any { p -> p.paymentID == id } ?: false
    }

    fun validID(): String {
        var newID: String

        val getLastPayment = payments.value?.lastOrNull()?.paymentID.toString()
        val num: String = getLastPayment.substringAfterLast("PAY10")
        newID = "PAY10" + (num.toIntOrNull()?.plus(1)).toString()

        if (newID == "PAY1010") {
            newID = "PAY1" + (num.toIntOrNull()?.plus(1)).toString()
            return newID
        }

        return when (calSize()) {
            0 -> {
                newID = "PAY10" + (calSize() + 1)
                newID
            }
            in 1..8 -> {
                val getLastPayment = payments.value?.lastOrNull()?.paymentID.toString()
                val num: String = getLastPayment.substringAfterLast("PAY10")
                newID = "PAY10" + (num.toIntOrNull()?.plus(1)).toString()
                if (newID == "PAY10null") {
                    newID = "PAY111"
                    newID
                } else newID
            }
            else -> {
                val getLastPayment = payments.value?.lastOrNull()?.paymentID.toString()
                val num: String = getLastPayment.substringAfterLast("PAY1")
                newID = "PAY1" + (num.toIntOrNull()?.plus(1)).toString()
                newID
            }
        }
    }
}
