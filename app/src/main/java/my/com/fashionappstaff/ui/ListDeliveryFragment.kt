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
import my.com.fashionapp.data.OrderViewModel
import my.com.fashionapp.data.ProductViewModel
import my.com.fashionapp.util.DeliveryAdapter
import my.com.fashionappstaff.R
import my.com.fashionappstaff.databinding.FragmentListDeliveryBinding
import my.com.fashionappstaff.util.toBitmap

class ListDeliveryFragment : Fragment() {

    private lateinit var binding: FragmentListDeliveryBinding
    private val nav by lazy { findNavController() }
    private val vmO: OrderViewModel by activityViewModels()
    private val vmP: ProductViewModel by activityViewModels()
    private lateinit var adapter: DeliveryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentListDeliveryBinding.inflate(inflater, container, false)
        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        vmO.getAll()
        vmP.getAll()

        binding.imgDeliveryBack.setOnClickListener { nav.navigate(R.id.staffHomeFragment) }

        adapter = DeliveryAdapter() { holder, product ->

            val p = vmP.get(product.orderProduct)
            val o = vmO.get(product.orderId)

            if (p != null) {
                holder.imgPhoto.setImageBitmap(p.productPhoto.toBitmap())
                holder.txtProductName.setText(p.productName)
            }

            holder.root.setOnClickListener {

                if (o != null && p != null) {
                    nav.navigate(R.id.orderDetailFragment, bundleOf("id" to product.orderId, "id1" to product.orderProduct,"id2" to product.orderPaymentId ))
                }
            }
        }

        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vmO.getAll().observe(viewLifecycleOwner){list ->
            adapter.submitList(list)
        }

        return binding.root
    }

}