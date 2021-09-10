package my.com.fashionappstaff.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionappstaff.R
import my.com.fashionappstaff.databinding.FragmentSignInBinding
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import my.com.fashionapp.data.UserViewModel
import my.com.fashionappstaff.data.email1
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

            email1 = email
            // Sign In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { t ->
                    if(t.isSuccessful) {
                        val args = bundleOf(
                            "email" to email
                        )
                        nav.navigate(R.id.staffHomeFragment, args)
                    }
//                  else if (!t.isSuccessful){
//                        // Try Method given by teacher
//
//                    }
                    else {
                        // Validation and error
                        val err = t.exception!!.message.toString()
                        errorDialog(err)
                    }
                }
        }
        return binding.root
    }

}