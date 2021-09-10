package my.com.fashionappstaff.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionappstaff.R
import my.com.fashionappstaff.databinding.FragmentSignUpBinding
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuth
import my.com.fashionapp.data.UserViewModel
import my.com.fashionappstaff.MainActivity
import my.com.fashionappstaff.util.errorDialog

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val nav by lazy{ findNavController() }
    private val vm: UserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // TODO
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        vm.getAll()

        // Hide Navigation
        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        binding.btnRegisterBack.setOnClickListener { nav.navigateUp() }

        binding.btnRegister.setOnClickListener {

            val email = binding.edtRegisterEmail.editText?.text.toString().trim()
            val password = binding.edtRegisterPassword.editText?.text.toString().trim()

            val err = vm.validation(email,password)
            if (err != ""){
                errorDialog(err)
            } else {

                val args = bundleOf(
                    "email" to email,
                    "password" to password,
                )
                nav.navigate(R.id.setUpStaffProfileFragment, args)
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        OnCompleteListener<AuthResult> { t ->
                            if (t.isSuccessful) {
                                val firebaseUser: FirebaseUser = t.result!!.user!!
                                val err = "Done"
                                errorDialog(err)
                                nav.navigate(R.id.setUpStaffProfileFragment, args)
                            } else {
                                val err = t.exception!!.message.toString()
                                errorDialog(err)
                            }
                        }
                    )
                nav.navigate(R.id.setUpStaffProfileFragment, args)

            }

        }



        return binding.root
    }

}