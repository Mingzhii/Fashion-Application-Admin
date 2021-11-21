package my.com.fashionapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import my.com.fashionappstaff.data.Reward
import my.com.fashionappstaff.data.User
import java.net.PasswordAuthentication

class UserViewModel : ViewModel() {

    private val col= Firebase.firestore.collection("users")
    private val users = MutableLiveData<List<User>>()
    private val staffs = MutableLiveData<List<User>>()
    private var searchPro = listOf<User>()
    private val result = MutableLiveData<List<User>>()
    private var name = "" // for searching purpose

    private val userLiveData = MutableLiveData<User?>()
    private var listener: ListenerRegistration? = null

    //init block will always run before the constructor
    init {
        col.addSnapshotListener { snap, _ -> users.value = snap?.toObjects()
        staffs.value = users.value?.filter { t -> t.userType == "Staff" }
            searchPro = snap!!.toObjects<User>()
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
            it.userName.contains(name, true) && it.userType != "User"
        }

        result.value = list
    }

    fun getResult() = result

    // Remove snapshot listener when view model is destroyed
    override fun onCleared() {
        super.onCleared()
        listener?.remove()
    }

    // Return observable live data
    fun getUserLiveData(): LiveData<User?> {
        return userLiveData
    }

    // Return user from live data
    fun getUser(): User? {
        return userLiveData.value
    }

    fun getUserType(email: String): User?{
        return users.value?.find { u -> u.email == email }
    }

    //Get Staffs
    fun getStaff(id : String): User?{
        return staffs.value?.find { u -> u.userId == id }
    }

    fun getAllStaffs() = staffs

    //Get Users
    fun getAll() = users

    fun get(id : String): User?{
        return users.value?.find { u -> u.userId == id }
    }

    fun getEmail(email: String): User?{
        return users.value?.find { u -> u.email == email }
    }

    fun getUserPhoto(userName : String): User?{
        return users.value?.find { u -> u.userName == userName }
    }

    fun getID(email: String): User?{
        return users.value?.find { u -> u.email == email }
    }

    fun getUserPhoto2(email: String): User?{
        return users.value?.find { u -> u.email == email }
    }

    fun delete(id : String){
        col.document(id).delete()
    }

    fun deleteAll(){
        users.value?.forEach{ u -> delete(u.userId)}
    }

    fun set(u: User){
        col.document(u.userId).set(u)
    }

    fun calSize() = users.value?.size ?: 0

    //-------------------------------------------------------------------
    // Validation

    private fun nameExists(name: String): Boolean {
        return users.value?.any{ u -> u.userName == name } ?: false
    }

    private fun phoneExists(phone: String): Boolean {
        return users.value?.any { u -> u.phoneNumber == phone } ?: false
    }

    private fun emailExists(email: String): Boolean {
        return users.value?.any{ u -> u.email == email } ?: false
    }

    private fun idExists(id: String): Boolean {
        return users.value?.any{ u -> u.userId == id } ?: false
    }

    fun validID(): String {
        var newID: String

        val getLastUser = users.value?.lastOrNull()?.userId.toString()
        val num: String = getLastUser.substringAfterLast("USER10")
        newID = "USER10" + (num.toIntOrNull()?.plus(1)).toString()

        if (newID == "USER1010") {
            newID = "USER1" + (num.toIntOrNull()?.plus(1)).toString()
            return newID
        }

        return when (calSize()) {
            0 -> {
                newID = "USER10" + (calSize() + 1)
                newID
            }
            in 1..8 -> {
                val getLastUser = users.value?.lastOrNull()?.userId.toString()
                val num: String = getLastUser.substringAfterLast("USER10")
                newID = "USER10" + (num.toIntOrNull()?.plus(1)).toString()
                if (newID == "USER10null") {
                    newID = "USER111"
                    newID
                } else newID
            }
            else -> {
                val getLastUser = users.value?.lastOrNull()?.userId.toString()
                val num: String = getLastUser.substringAfterLast("USER1")
                newID = "USER1" + (num.toIntOrNull()?.plus(1)).toString()
                newID
            }
        }
    }
    fun validation(email: String, password: String): String {
        var e = ""

        //Email
        e += if (email == "") "- Email Address is required. \n"
        else if (emailExists(email)) "- Email Address is Duplicated. \n"
        else ""

        //Password
        e += if (password == "") "- Password is required. \n"
        else ""

        return e
    }


    fun validate(u: User, insert: Boolean = true): String {

        var e = ""

        //name
        e += if (u.userName == "") "- Username is required.\n"
        else if (u.userName.length < 3) "- Username is too short.\n"
        else if (nameExists(u.userName)) "- Username is duplicated.\n"
        else ""

        //Phone Number
        e += if (u.phoneNumber.toString() == "") "- Phone Number is required.\n"
        else if (phoneExists(u.phoneNumber)) "- Phone Number is used.\n"
        else ""

        //Photo
        e += if (u.userPhoto.toBytes().isEmpty()) "- Photo is required.\n"
        else ""

        return e
    }


}