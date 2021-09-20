package my.com.fashionappstaff.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import my.com.fashionapp.data.UserViewModel
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.email1
import my.com.fashionappstaff.data.username
import my.com.fashionappstaff.databinding.FragmentForgetPasswordBinding
import my.com.fashionappstaff.util.SimpleEmail
import my.com.fashionappstaff.util.errorDialog
import my.com.fashionappstaff.util.hideKeyboard
import java.text.DecimalFormat

class ForgetPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgetPasswordBinding
    private val nav by lazy{ findNavController() }
    private val vm: UserViewModel by activityViewModels()
    private val email by lazy { requireArguments().getString("email","N/A") }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)

        // TODO
        vm.getAll()

        binding.edtOTP.isEnabled = false

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        val n  = (0..999999).random()
        val fmt = DecimalFormat("000000")
        val OTP = fmt.format(n)

        binding.btnBack.setOnClickListener { nav.navigate(R.id.action_forgetPasswordFragment_to_signInFragment) }


        binding.btnOTP.setOnClickListener { sendOTP(OTP) }

        binding.btnSubmit.setOnClickListener { resetPassword(OTP) }

        return binding.root
    }

    private fun resetPassword(OTP: String) {

        val email = binding.edtEmail.editText?.text.toString().trim()
        val otp = binding.edtOTP.editText?.text.toString()
        val u = vm.getEmail(email)
        val password = u!!.password


        if (email.isEmpty()){

            snackbar("Please enter email address")

        } else{

            if(otp == OTP)
            {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { t ->
                        if(t.isSuccessful){
                            nav.navigate(R.id.resetPasswordFragment, bundleOf("email" to email, "password" to password))
                        }
                    }
            }

        }
    }

    private fun sendOTP(OTP: String) {

        val email = binding.edtEmail.editText?.text.toString().trim()

        val u = vm.getEmail(email)

        if (u == null){
            errorDialog("No account found in the system that associate with the email given")
        }
        else {
            send(email, OTP)
        }

    }
    private fun send(email: String, OTP: String) {

        hideKeyboard()


        if (!isEmail(email)) {
            snackbar("Invalid Email")
            binding.edtEmail.requestFocus()
            return
        }

        val subject = "Your OTP to reset your password"
        val content = """
            <p>Your <b>OTP<b> is:</p>
            <h1 style="color: Blue">$OTP</h1>
            <p>Thank you very much.</p>
        """.trimIndent()

        SimpleEmail()
            .to(email)
            .subject(subject)
            .content(content)
            .isHtml()
            .send() {
                snackbar("Sent")
                binding.edtOTP.isEnabled = true
                binding.edtEmail.requestFocus()
            }

        snackbar("Sending...")
        binding.edtOTP.isEnabled = false

    }
//
    private fun isEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun snackbar(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT).show()
    }

}