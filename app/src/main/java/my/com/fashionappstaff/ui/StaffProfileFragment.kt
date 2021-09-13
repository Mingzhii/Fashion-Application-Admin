package my.com.fashionappstaff.ui

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

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.VISIBLE

        val email = email1

        val u = vm.getUserType(email)

        getImage(email)



        if (u != null) {
            if(u.userType == "Admin"){
                binding.conLayout.visibility = View.VISIBLE
                binding.conManagerUser.visibility = View.VISIBLE
            }
            else{
                binding.conLayout.visibility = View.GONE
                binding.conManagerUser.visibility = View.GONE
            }
        }

        binding.conLayLogout.setOnClickListener { logout() }
        binding.conLayout.setOnClickListener { nav.navigate(R.id.signUpFragment) }
        binding.conLayUpdateProfile.setOnClickListener { nav.navigate(R.id.updateUserProfileFragment) }
        binding.conManagerUser.setOnClickListener { nav.navigate(R.id.listUserFragment) }

        return binding.root
    }
    private fun logout():Boolean {
        // Logout -> vm.logout
        img = Blob.fromBytes(ByteArray(0))
        username = ""
        FirebaseAuth.getInstance().signOut()
        val ctx = requireActivity()
        vm.logout(ctx)
        nav.popBackStack(R.id.signInFragment, false)
        nav.navigateUp()

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