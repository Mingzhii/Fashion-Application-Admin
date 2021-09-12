package my.com.fashionappstaff.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionapp.data.UserViewModel
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.Product
import my.com.fashionappstaff.data.Reward
import my.com.fashionappstaff.data.User
import my.com.fashionappstaff.databinding.FragmentUpdateUserBinding
import my.com.fashionappstaff.util.cropToBlob
import my.com.fashionappstaff.util.errorDialog
import my.com.fashionappstaff.util.toBitmap

class UpdateUserFragment : Fragment() {

    private lateinit var binding: FragmentUpdateUserBinding
    private val nav by lazy { findNavController() }
    private val vm: UserViewModel by activityViewModels()

    private val id by lazy { requireArguments().getString("id","N/A") }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateUserBinding.inflate(inflater, container, false)

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        // TODO
        reset()
        binding.imgUpdateUserBack.setOnClickListener { nav.navigate(R.id.action_updateUserFragment_to_listUserFragment) }
        binding.btnUpdateStaffDone.setOnClickListener { updateStaff() }

        return binding.root
    }

    private fun updateStaff() {
        // TODO: Update (set)
        val u = User(
            userId = id,
            email = binding.txtStaffEmail.text.toString(),
            userName = binding.txtUpdateStaffName.text.toString(),
            phoneNumber = binding.txtPhoneNumber.text.toString(),
            userPhoto = binding.imgUpdateUser.cropToBlob(300,300),
            userType = binding.spRole.selectedItem.toString(),
        )

        vm.set(u)
        nav.navigateUp()


    }
    private fun reset() {
        val u = vm.get(id)
        if (u == null){
            nav.navigateUp()
            return
        }
        load(u)
    }

    private fun check(userType: String): Int {

        if (userType == "Admin"){
            return 0
        }else {
            return 1
        }
    }

    private fun load(u: User) {

        binding.imgUpdateUser.setImageBitmap(u.userPhoto.toBitmap())
        binding.txtUpdateStaffName.setText(u.userName)
        binding.txtPhoneNumber.setText(u.phoneNumber)
        binding.txtStaffEmail.setText(u.email)

        val position = check(u.userType)
        binding.spRole.setSelection(position)

        binding.spRole.requestFocus()
    }

}