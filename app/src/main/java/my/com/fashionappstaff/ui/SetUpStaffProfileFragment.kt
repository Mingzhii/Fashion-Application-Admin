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
import my.com.fashionapp.data.UserViewModel
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.User
import my.com.fashionappstaff.databinding.FragmentSetUpStaffProfileBinding
import my.com.fashionappstaff.util.cropToBlob
import my.com.fashionappstaff.util.errorDialog

class SetUpStaffProfileFragment : Fragment() {

    private lateinit var binding: FragmentSetUpStaffProfileBinding
    private val nav by lazy{ findNavController() }
    private val vm: UserViewModel by activityViewModels()
    private val email by lazy { requireArguments().getString("email","N/A") }
    private val password by lazy { requireArguments().getString("password","N/A") }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgUserPic.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentSetUpStaffProfileBinding.inflate(inflater, container, false)

        // TODO
        binding.imgUserPic.setOnClickListener { selectImage() }
        binding.btnDone.setOnClickListener { submit() }

        return binding.root
    }

    private fun submit() {

        val id = "USER00" + (vm.calSize() + 1).toString()
        val u = User(
            userId = id,
            email = email,
            password = password,
            userName = binding.edtUserName.editText?.text.toString().trim(),
            phoneNumber = binding.edtPhoneNumber.editText?.text.toString(),
            userPhoto = binding.imgUserPic.cropToBlob(300,300),
            homeAddress = binding.edtHomeAddress.editText?.text.toString(),
            userPoint = 0,
            userType = "Staff"
        )

        val err = vm.validate(u)
        if (err != ""){
            errorDialog(err)
            return
        }else{
            vm.set(u)
            nav.navigate(R.id.staffProfileFragment)
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

}