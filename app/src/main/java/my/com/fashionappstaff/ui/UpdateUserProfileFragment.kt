package my.com.fashionappstaff.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionapp.data.UserViewModel
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.User
import my.com.fashionappstaff.data.email1
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

        reset()
        binding.imgUpdateUserProfileBack.setOnClickListener { nav.navigate(R.id.action_updateUserProfileFragment_to_staffProfileFragment) }
        binding.btnUpdateUserProfileDone.setOnClickListener { updateProfile() }
        binding.imgStaffProdilePic.setOnClickListener { select() }


        return binding.root
    }
    private fun select() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun reset() {
        val email = email1

        val u = vm.getUserPhoto2(email)
        if (u == null){
            nav.navigateUp()
            return
        }
        load(u)
    }

    private fun load(u: User) {

        binding.imgStaffProdilePic.setImageBitmap(u.userPhoto.toBitmap())
        binding.edtStaffName.editText?.setText(u.userName)
        binding.edtStaffPhoneNumber.editText?.setText(u.phoneNumber)

    }
    private fun updateProfile() {

        val u = User(
            userName = binding.edtStaffName.editText?.text.toString(),
            phoneNumber = binding.edtStaffPhoneNumber.editText?.text.toString(),
            userPhoto = binding.imgStaffProdilePic.cropToBlob(300,300),
        )

        val err = vm.validate(u)

        if (err != "") {
            errorDialog(err)
            return
        } else {
            vm.set(u)
            nav.navigate(R.id.action_global_staffProfileFragment)
        }
    }

}