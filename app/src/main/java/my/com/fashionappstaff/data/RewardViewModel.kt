package my.com.fashionappstaff.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import java.util.*
import my.com.fashionappstaff.data.Reward

class RewardViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("rewards")
    private val rewards = MutableLiveData<List<Reward>>()

    init {
        col.addSnapshotListener { snap, _ -> rewards.value = snap?.toObjects()  }
    }

    fun get(id : String): Reward?{
        return rewards.value?.find { r -> r.rewardID == id }
    }

    fun getAll() = rewards

    fun delete(id : String){
        col.document(id).delete()
    }

    fun deleteAll(){
        rewards.value?.forEach{ r -> delete(r.rewardID)}
    }

    fun set(r: Reward){
        col.document(r.rewardID).set(r)
    }

    // Calculate the size of products list
    fun calSize() = rewards.value?.size ?: 0

    //-------------------------------------------------------------------
    // Validation

    private fun nameExists(name: String): Boolean {
        return rewards.value?.any{ r -> r.rewardName == name } ?: false
    }

    fun validate(r: Reward, insert: Boolean = true): String {

        var e = ""
        val Date = Date()

        //name
        e += if (r.rewardName == "") "- Reward Name is required.\n"
        else if (r.rewardName.length < 3) "- Reward Name is too short.\n"
        else if (nameExists(r.rewardName)) "- Reward Name is duplicated.\n"
        else ""

        //Description
        e += if (r.rewardDescrip == "") "- Description is required.\n"
        else if (r.rewardDescrip.length < 3) "- Description is to short.\n"
        else ""

        //Quantity
        e += if (r.rewardQuan == 0) "- Quantity cannot be 0. \n"
        else ""

        //Point
        e += if (r.rewardPoint == 0 ) "- Point cannot be 0. \n"
        else ""

        //Expiry Date
        e += if (r.expiryDate == Date.toString()) "- Expiry date cannot be today. \n"
        else if (r.expiryDate == "") "- Please Select Expiry Date. \n"
        else ""

        //Photo
        e += if (r.rewardPhoto.toBytes().isEmpty()) "- Reward Photo is required.\n"
        else ""

        return e
    }
}