package my.com.fashionappstaff.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import my.com.fashionapp.data.UserViewModel
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.email1
import my.com.fashionappstaff.databinding.FragmentSignInBinding
import my.com.fashionappstaff.util.errorDialog

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val nav by lazy{ findNavController() }
    private val vm: UserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // TODO
        vm.getAll()

        binding = FragmentSignInBinding.inflate(inflater, container, false)

        //hide bottom navigation bar
        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        binding.btnLogin.setOnClickListener {
            val email = binding.edtLoginEmail.editText?.text.toString().trim()
            val password = binding.edtLoginPassword.editText?.text.toString().trim()

//            val err = vm.validation(email, password)
//            email1 = email
//            if (err != ""){
//                errorDialog(err)
//            }
                // Sign In using FirebaseAuth
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { t ->
                        if (t.isSuccessful) {
                            email1 = email
                            val args = bundleOf(
                                "email" to email
                            )
                            nav.navigate(R.id.staffHomeFragment, args)
                        } else {
                            // Validation and error
                            val err = t.exception!!.message.toString()
                            errorDialog(err)
                        }

            }
        }
        return binding.root
    }

}