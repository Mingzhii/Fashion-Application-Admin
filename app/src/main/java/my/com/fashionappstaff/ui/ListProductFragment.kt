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
import my.com.fashionapp.data.ProductViewModel
import my.com.fashionappstaff.R
import my.com.fashionappstaff.databinding.FragmentListProductBinding
import my.com.fashionappstaff.util.ProductAdapter


class ListProductFragment : Fragment() {

    private lateinit var binding: FragmentListProductBinding
    private val nav by lazy { findNavController() }
    private val vm: ProductViewModel by activityViewModels()

    private lateinit var adapter: ProductAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentListProductBinding.inflate(inflater, container, false)

        binding.imgBack.setOnClickListener { nav.navigateUp() }

        adapter = ProductAdapter() { holder, product ->
            // Item click
            holder.root.setOnClickListener {
                nav.navigate(R.id.updateFragment, bundleOf("id" to product.productId))
            }
            // Delete button click
            holder.btnDelete.setOnClickListener { delete(product.productId) }
        }

        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vm.getAll().observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.txtItem.text = "${list.size} product(s)"
        }


        return binding.root
    }

    private fun delete(id: String) {
        // TODO: Delete
        vm.delete(id)
    }

}