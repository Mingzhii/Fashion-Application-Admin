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
import my.com.fashionapp.data.UserViewModel
import my.com.fashionappstaff.R
import my.com.fashionappstaff.databinding.FragmentListUserBinding
import my.com.fashionappstaff.util.RewardAdapter
import my.com.fashionappstaff.util.UserAdapter

class ListUserFragment : Fragment() {

    private lateinit var binding: FragmentListUserBinding
    private val nav by lazy { findNavController() }
    private val vm: UserViewModel by activityViewModels()

    private lateinit var adapter: UserAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentListUserBinding.inflate(inflater, container, false)

        // TODO
        vm.getAll()

        binding.imgListUserBack.setOnClickListener { nav.navigate(R.id.action_listUserFragment_to_staffProfileFragment) }

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        // Recycle Holder
        adapter = UserAdapter() { holder, user ->
            // Item click
            holder.root.setOnClickListener {
                nav.navigate(R.id.updateUserFragment, bundleOf("id" to user.userId))
            }
            // Delete button click
            holder.btnDelete.setOnClickListener { delete(user.userId) }
        }

        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

       vm.getAllStaffs().observe(viewLifecycleOwner) { list ->
           adapter.submitList(list)
           binding.txtItem.text = "${list.size} Staff(s)"
        }

        return binding.root
    }

    private fun delete(id: String) {
        // TODO: Delete
        vm.delete(id)
    }

}