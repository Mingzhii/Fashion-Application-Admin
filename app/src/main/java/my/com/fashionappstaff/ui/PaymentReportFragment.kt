package my.com.fashionappstaff.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionapp.data.UserViewModel
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.CartViewModel
import my.com.fashionappstaff.databinding.FragmentPaymentReportBinding
import my.com.fashionappstaff.util.HistoryAdapter


class PaymentReportFragment : Fragment() {

    private lateinit var binding: FragmentPaymentReportBinding
    private val nav by lazy{ findNavController() }
    private val vm: CartViewModel by activityViewModels()
    private val vmU: UserViewModel by activityViewModels()

    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaymentReportBinding.inflate(inflater, container, false)

        vm.search("")

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        binding.imgBack.setOnClickListener { nav.navigate(R.id.action_paymentReportFragment_to_staffProfileFragment) }

        binding.edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String) = true
            override fun onQueryTextChange(name: String): Boolean {
                vm.search(name)
                return true
            }
        })

        val num = 0

        adapter = HistoryAdapter() { holder, cart ->
            // Item click
        }

        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vm.getResult().observe(viewLifecycleOwner) { list ->
            var totalprice = 0.0
            var cartArray = list.filter { c ->
                c.cartStatus == "Done The Payment"
            }

            for (i in 0..cartArray.size - 1){
                val productPrice = cartArray[i].cartProductPrice
                val productQuantity = cartArray[i].cartProductQuantity
                val subtotal = productPrice * productQuantity
                totalprice += subtotal

                binding.txtSubtotal.text = "%.2f".format(totalprice)
            }
            adapter.submitList(cartArray)
            binding.txtItem.text = "${cartArray.size} product(s)"
        }


        return binding.root
    }

}