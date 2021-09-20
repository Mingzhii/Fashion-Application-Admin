package my.com.fashionappstaff.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionapp.data.UserViewModel
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.User
import my.com.fashionappstaff.data.email1
import my.com.fashionappstaff.data.img
import my.com.fashionappstaff.data.username
import my.com.fashionappstaff.databinding.FragmentUpdateUserProfileBinding
import my.com.fashionappstaff.util.cropToBlob
import my.com.fashionappstaff.util.errorDialog
import my.com.fashionappstaff.util.toBitmap

class UpdateUserProfileFragment : Fragment() {

    private lateinit var binding: FragmentUpdateUserProfileBinding
    private val nav by lazy { findNavController() }
    private val vm: UserViewModel by activityViewModels()

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgStaffProdilePic.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentUpdateUserProfileBinding.inflate(inflater, container, false)

        // TODO
        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        val preferences = activity?.getSharedPreferences("email", Context.MODE_PRIVATE)
        val emailLogin = preferences?.getString("emailLogin","")

        val u = emailLogin?.let { vm.getUserPhoto2(it) }
        if (u != null) {
            load(u)
        }


        binding.imgUpdateUserProfileBack.setOnClickListener { nav.navigate(R.id.action_updateUserProfileFragment_to_staffProfileFragment) }
        binding.btnUpdateUserProfileDone.setOnClickListener {
            if(u != null){
                val u = User(
                    userId = u.userId,
                    email = u.email,
                    password = u.password,
                    userName = binding.edtStaffName.editText?.text.toString(),
                    phoneNumber = binding.edtStaffPhoneNumber.editText?.text.toString(),
                    userPhoto = binding.imgStaffProdilePic.cropToBlob(300,300),
                    homeAddress = u.homeAddress,
                    userType = u.userType,
                    userPoint = u.userPoint
                )
                username = binding.edtStaffName.editText?.text.toString()
                img = binding.imgStaffProdilePic.cropToBlob(300,300)

                vm.set(u)
                nav.navigate(R.id.action_global_staffProfileFragment)


            }

        }
        binding.imgStaffProdilePic.setOnClickListener { select() }


        return binding.root
    }
    private fun select() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun load(u: User) {

        binding.imgStaffProdilePic.setImageBitmap(u.userPhoto.toBitmap())
        binding.edtStaffName.editText?.setText(u.userName)
        binding.edtStaffPhoneNumber.editText?.setText(u.phoneNumber)

    }

}