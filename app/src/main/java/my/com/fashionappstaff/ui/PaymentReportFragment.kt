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

        binding.edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String) = true
            override fun onQueryTextChange(name: String): Boolean {
                vm.search(name)
                return true
            }
        })


        adapter = HistoryAdapter() { holder, cart ->
            // Item click
            var totalprice = 0.0
            val productPrice = holder.txtPrice.text.toString().toDouble()
            val productQuantity = holder.txtQuantity.text.toString().toInt()
            val subtotal = productPrice * productQuantity.toString().toDouble()
            totalprice += subtotal

            binding.txtSubtotal.text = "%.2f".format(totalprice)
        }

        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vm.getResult().observe(viewLifecycleOwner) { list ->

            var cartArray = list.filter { c ->
                c.cartStatus == "Done The Payment"
            }
            adapter.submitList(cartArray)
        }


        return binding.root
    }

}