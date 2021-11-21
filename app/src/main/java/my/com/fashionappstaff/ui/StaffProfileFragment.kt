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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Blob
import my.com.fashionapp.data.UserViewModel
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.User
import my.com.fashionappstaff.data.email1
import my.com.fashionappstaff.data.img
import my.com.fashionappstaff.data.username
import my.com.fashionappstaff.databinding.FragmentStaffProfileBinding
import my.com.fashionappstaff.util.toBitmap

class StaffProfileFragment : Fragment() {

    private lateinit var binding: FragmentStaffProfileBinding
    private val nav by lazy{ findNavController() }
    private val vm: UserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentStaffProfileBinding.inflate(inflater, container, false)

        //TODO
        
        val preferences = activity?.getSharedPreferences("email", Context.MODE_PRIVATE)
        val emailLogin = preferences?.getString("emailLogin","")

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.VISIBLE

        val u = emailLogin?.let { vm.getUserType(it) }

        if (emailLogin != null) {
            getImage(emailLogin)
        }

        if (u != null) {
            if(u.userType == "Admin"){
                binding.conLayout.visibility = View.VISIBLE
                binding.conManagerUser.visibility = View.VISIBLE
                binding.conLayReport.visibility = View.VISIBLE
            }
            else{
                binding.conLayout.visibility = View.GONE
                binding.conManagerUser.visibility = View.GONE
                binding.conLayReport.visibility = View.GONE
            }
        }

        binding.conLayLogout.setOnClickListener { logout() }
        binding.conLayout.setOnClickListener { nav.navigate(R.id.signUpFragment) }
        binding.conLayUpdateProfile.setOnClickListener { nav.navigate(R.id.updateUserProfileFragment) }
        binding.conManagerUser.setOnClickListener { nav.navigate(R.id.listUserFragment) }
        binding.conLayReport.setOnClickListener { nav.navigate(R.id.paymentReportFragment) }

        return binding.root
    }

    private fun logout():Boolean {
        // Logout -> vm.logout
        img = Blob.fromBytes(ByteArray(0))
        username = ""
        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.selectedItemId = R.id.nav_home

        val sharedPref = activity?.getSharedPreferences("checkBo", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPref!!.edit()
        editor.putString("remember","false")
        editor.apply()
        val sharedPref1 = activity?.getSharedPreferences("email", Context.MODE_PRIVATE)
        val editor1 : SharedPreferences.Editor = sharedPref1!!.edit()
        editor1.putString("emailLogin","")
        editor1.apply()

        FirebaseAuth.getInstance().signOut()

        nav.navigate(R.id.action_global_signInFragment)

        return true
    }

    private fun getImage(email : String) {
        val u = vm.getUserPhoto2(email)
        if (u != null){
            load(u)
        }
    }

    private fun load (u: User){

        if (img == Blob.fromBytes(ByteArray(0)) && username == ""){
            if(u != null){
                binding.imgUserPic.setImageBitmap(u.userPhoto.toBitmap())
                binding.txtUsername.text = u.userName
                binding.txtEditProfile.text = u.userType
            }
        }else if(img == Blob.fromBytes(ByteArray(0))){
            if(u != null){
                binding.imgUserPic.setImageBitmap(u.userPhoto.toBitmap())
                binding.txtUsername.text = username
                binding.txtEditProfile.text = u.userType
            }
        }else{
            binding.imgUserPic.setImageBitmap(img.toBitmap())
            binding.txtUsername.text = username
            binding.txtEditProfile.text = u.userType
        }
    }

}