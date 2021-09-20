package my.com.fashionappstaff.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Blob
import my.com.fashionapp.data.UserViewModel
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.email1
import my.com.fashionappstaff.data.img
import my.com.fashionappstaff.data.username
import my.com.fashionappstaff.databinding.FragmentResetPasswordBinding
import my.com.fashionappstaff.util.errorDialog
import my.com.fashionappstaff.util.informationDialog

class ResetPasswordFragment : Fragment() {

    private lateinit var binding: FragmentResetPasswordBinding
    private val nav by lazy{ findNavController() }
    private val vm: UserViewModel by activityViewModels()
    private val email by lazy { requireArguments().getString("email", "N/A") }
    private val password by lazy { requireArguments().getString("password", "N/A") }
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding  = FragmentResetPasswordBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        vm.getAll()
        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        binding.btnBack.setOnClickListener { nav.navigateUp() }
        binding.btnSubmit.setOnClickListener { resetPass() }

        return binding.root
    }

    private fun resetPass() {

        val newPass = binding.edtPassword.editText?.text.toString().trim()
        val newConfirmPass = binding.edtConfirmPassword.editText?.text.toString().trim()
        val checkResult = checkIfEmpty(newPass,newConfirmPass)
        if (checkResult == false) {
            var u = vm.getEmail(email)
            if (newPass == newConfirmPass) {
                val user = auth.currentUser
                if (user != null) {
                    val credential = EmailAuthProvider
                        .getCredential(u!!.email, u!!.password)
                    user.reauthenticate(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {

                                user.updatePassword(newPass)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {

                                            informationDialog("Successful change the password")
                                            auth.signOut()
                                            logout()
                                        }
                                    }
                            } else {
                                errorDialog("Failed to change the password")
                            }
                        }
                }

                user!!.updatePassword(newPass)

                u?.password = newPass
                vm.set(u!!)
            } else {
                errorDialog("Password does not match")
            }

        }
        else{
            errorDialog("The field cannot be empty")
        }
    }

    private fun checkIfEmpty(newPass: String, newConfirmPass: String): Boolean {
        if(newPass == "" && newConfirmPass == ""){
            return true
        }
        return false
    }

    private fun logout():Boolean {
        // Logout -> vm.logout
        img = Blob.fromBytes(ByteArray(0))
        username = ""
        email1 = ""

        val sharedPref = activity?.getSharedPreferences("checkBo", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPref!!.edit()
        editor.putString("remember","false")
        editor.apply()
        val sharedPref1 = activity?.getSharedPreferences("email", Context.MODE_PRIVATE)
        val editor1 : SharedPreferences.Editor = sharedPref1!!.edit()
        editor1.putString("emailLogin","")
        editor1.apply()


        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.selectedItemId = R.id.nav_home

        FirebaseAuth.getInstance().signOut()

        nav.navigate(R.id.action_global_signInFragment)

        return true
    }

}