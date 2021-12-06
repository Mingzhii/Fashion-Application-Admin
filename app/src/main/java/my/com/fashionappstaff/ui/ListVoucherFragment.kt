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
import my.com.fashionappstaff.data.RewardViewModel
import my.com.fashionappstaff.data.Voucher
import my.com.fashionappstaff.data.VoucherClaim
import my.com.fashionappstaff.data.VoucherViewModel
import my.com.fashionappstaff.databinding.FragmentListVoucherBinding
import my.com.fashionappstaff.util.VoucherAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ListVoucherFragment : Fragment() {

    private lateinit var binding: FragmentListVoucherBinding
    private val nav by lazy { findNavController() }
    private val vm: VoucherViewModel by activityViewModels()

    private lateinit var adapter: VoucherAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentListVoucherBinding.inflate(inflater, container, false)

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        binding.imgListRewardBack2.setOnClickListener { nav.navigate(R.id.staffProfileFragment) }



        adapter = VoucherAdapter() { holder, voucher ->
            // Item click


        }

        binding.rv.adapter = adapter

        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        val currentDate = LocalDate.now()
        val viewFormatter = DateTimeFormatter.ofPattern("dd MMM, YYYY")
        val formatDate = viewFormatter.format(currentDate)

        vm.getAll().observe(viewLifecycleOwner) { list ->

            adapter.submitList(list)
            binding.txtItem2.text = "${list.size} reward(s)"
        }


        return binding.root
    }

}