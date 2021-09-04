package my.com.fashionapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import java.net.PasswordAuthentication

class UserViewModel : ViewModel() {

    private val col= Firebase.firestore.collection("users")
    private val users = MutableLiveData<List<User>>()

    private val userLiveData = MutableLiveData<User>()
    private var listener: ListenerRegistration? = null

    //init block will always run before the constructor
    init {
        col.addSnapshotListener { snap, _ -> users.value = snap?.toObjects()  }
    }

    // Remove snapshot listener when view model is destroyed
    override fun onCleared() {
        super.onCleared()
        listener?.remove()
    }

    // Return observable live data
    fun getUserLiveDate(): LiveData<User> {
        return userLiveData
    }

    // Return user from live data
    fun getUser(): User? {
        return userLiveData.value
    }

    fun get(id : String): User?{
        return users.value?.find { u -> u.userId == id }
    }

    fun getUserPhoto(userName : String): User?{
        return users.value?.find { u -> u.userName == userName }
    }

    fun getUserPhoto2(email: String): User?{
        return users.value?.find { u -> u.email == email }
    }

    fun getAll() = users

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