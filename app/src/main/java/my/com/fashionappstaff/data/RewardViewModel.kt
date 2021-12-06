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
    private var searchPro = listOf<Reward>()
    private val result = MutableLiveData<List<Reward>>()
    private var name = "" // for searching purpose

    init {
        col.addSnapshotListener { snap, _ -> rewards.value = snap?.toObjects()
            searchPro = snap!!.toObjects<Reward>()
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
            it.rewardName.contains(name, true)
        }

        result.value = list
    }

    fun getResult() = result

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

    private fun idExists(id: String): Boolean {
        return rewards.value?.any{ r -> r.rewardID == id } ?: false
    }

    fun validID(): String {
        var newID: String

        val getLastReward = rewards.value?.lastOrNull()?.rewardID.toString()
        val num: String = getLastReward.substringAfterLast("REW10")
        newID = "REW10" + (num.toIntOrNull()?.plus(1)).toString()

        if (newID == "REW1010") {
            newID = "REW1" + (num.toIntOrNull()?.plus(1)).toString()
            return newID
        }

        return when (calSize()) {
            0 -> {
                newID = "REW10" + (calSize() + 1)
                newID
            }
            in 1..8 -> {
                val getLastReward = rewards.value?.lastOrNull()?.rewardID.toString()
                val num: String = getLastReward.substringAfterLast("REW10")
                newID = "REW10" + (num.toIntOrNull()?.plus(1)).toString()
                if (newID == "REW10null") {
                    newID = "REW111"
                    newID
                } else newID
            }
            else -> {
                val getLastReward = rewards.value?.lastOrNull()?.rewardID.toString()
                val num: String = getLastReward.substringAfterLast("REW1")
                newID = "REW1" + (num.toIntOrNull()?.plus(1)).toString()
                newID
            }
        }
    }

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
        e += if (r.rewardPoint == 0.0 ) "- Point cannot be 0. \n"
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