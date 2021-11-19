package my.com.fashionapp.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import my.com.fashionapp.data.OrderViewModel
import my.com.fashionapp.data.PaymentViewModel
import my.com.fashionapp.data.ProductViewModel
import my.com.fashionapp.data.UserViewModel
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.Order
import my.com.fashionappstaff.data.OrderList
import my.com.fashionappstaff.databinding.FragmentOrderDetailBinding
import my.com.fashionappstaff.util.toBitmap


class OrderDetailFragment : Fragment() {

    private lateinit var binding: FragmentOrderDetailBinding
    private val vmU : UserViewModel by activityViewModels()
    private val vm: ProductViewModel by activityViewModels()
    private val vmO: OrderViewModel by activityViewModels()
    private val vmP: PaymentViewModel by activityViewModels()
    private val nav by lazy{ findNavController() }

    private val id by lazy { requireArguments().getString("id", "N/A") }
    private val id1 by lazy { requireArguments().getString("id1", "N/A") }
    private val id2 by lazy { requireArguments().getString("id2", "N/A") }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderDetailBinding.inflate(inflater, container, false)

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        orderDetail()
        binding.imgOrderDetailsBack.setOnClickListener { nav.navigate(R.id.staffHomeFragment) }

        val p1 = vm.get(id1)


        binding.btnPaid.setOnClickListener {
            updateStatusToShip()
        }

        binding.imageView8.setOnClickListener {
            if (p1 != null) {
//                nav.navigate(R.id.deliveryProductDetailFragment, bundleOf("id" to id1))
            }else{
                snackbar("Fail")
            }
        }

        return binding.root
    }

    private fun updateStatusToShip() {

//        val o = Order(
//            orderId = id,
//            orderStatus = "To Ship"
//        )
//        vmO.set(o)
//        nav.navigateUp()
    }

    private fun orderDetail() {

        val o   = vmO.get(id)
        val p   = vm.get(id1)
        val pay = vmP.get(id2)

        if(o != null){
            binding.txtShippingInfo.setText(o.orderId)
            binding.txtOrderDate.setText(o.orderDate)
            binding.txtAddress.setText(o.orderShipping)
            binding.txtOrderDetailsPrice.setText("RM" + o.orderProductTotalPrice)
            binding.txtOrderDetailsQuant.setText("X" + o.orderProductQuantity)
            binding.txtRecipientName.setText(o.orderUserName)
            binding.txtPhoneNo.setText(o.orderUserPhone)
            binding.txtOrderDetailStatus.setText(o.orderStatus)
        }
        if(p != null){
            binding.txtOrderDetailsName.setText(p.productName)
            binding.imageView8.setImageBitmap(p.productPhoto.toBitmap())
        }
        if(pay != null){
            binding.txtPaymentMethod.setText(pay.paymentMethod)
        }

    }
    private fun snackbar(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT).show()
    }
}