package my.com.fashionappstaff.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionappstaff.R
import my.com.fashionappstaff.databinding.FragmentRewardBinding
import my.com.fashionappstaff.databinding.FragmentVoucherBinding


class VoucherFragment : Fragment() {

    private lateinit var binding: FragmentVoucherBinding
    private val nav by lazy { findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding =FragmentVoucherBinding.inflate(inflater,container,false)

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.VISIBLE

        binding.btnVoucherAdd.setOnClickListener { nav.navigate(R.id.insertVoucherFragment) }

        binding.btnList.setOnClickListener { nav.navigate(R.id.listVoucherFragment) }


        return binding.root
    }


}