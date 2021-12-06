package my.com.fashionappstaff.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.CartViewModel
import my.com.fashionappstaff.databinding.FragmentReportBinding
import my.com.fashionappstaff.util.ProductAdapter
import my.com.fashionappstaff.util.ReportAdapter

class ReportFragment : Fragment() {

    private lateinit var binding: FragmentReportBinding
    private val vmC: CartViewModel by activityViewModels()
    private val nav by lazy { findNavController() }

    private lateinit var adapter: ReportAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentReportBinding.inflate(inflater, container, false)

        binding.imgReportBack.setOnClickListener { nav.navigate(R.id.action_reportFragment_to_staffProfileFragment) }

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        adapter = ReportAdapter() { holder, report ->
            // Item click

        }

        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vmC.getAll().observe(viewLifecycleOwner) { list ->

            val cartArray = list.filter { c ->
                c.cartStatus == "Done The Payment"
            }
            if(cartArray.isNotEmpty()) {

                var totalPrice = 0.0

                for (i in 0..cartArray.size - 1) {
                    val productprice = cartArray[i].cartProductPrice
                    totalPrice += productprice
                }

                binding.txtTotalAll.text = "RM " + "%.2f".format(totalPrice)
            }
            adapter.submitList(cartArray)
        }


        return binding.root
    }


}