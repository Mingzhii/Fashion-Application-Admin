package my.com.fashionappstaff.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import my.com.fashionappstaff.R
import my.com.fashionappstaff.databinding.FragmentProductBinding

class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private val nav by lazy { findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductBinding.inflate(inflater, container, false)

        // TODO

        binding.btnAddProduct.setOnClickListener { nav.navigate(R.id.insertProductFragment) }

        binding.btnListProduct.setOnClickListener { nav.navigate(R.id.listProductFragment) }


        return binding.root
    }

}