package my.com.fashionappstaff.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionappstaff.R
import my.com.fashionappstaff.data.RewardViewModel
import my.com.fashionappstaff.databinding.FragmentListRewardBinding
import my.com.fashionappstaff.util.ProductAdapter
import my.com.fashionappstaff.util.RewardAdapter


class ListRewardFragment : Fragment() {

    private lateinit var binding: FragmentListRewardBinding
    private val nav by lazy { findNavController() }
    private val vm: RewardViewModel by activityViewModels()

    private lateinit var adapter: RewardAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentListRewardBinding.inflate(inflater, container, false)

        vm.search("")

        binding.imgListRewardBack.setOnClickListener { nav.navigate(R.id.action_listRewardFragment_to_rewardFragment) }

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        binding.edtSearchReward.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String) = true
            override fun onQueryTextChange(name: String): Boolean {
                vm.search(name)
                return true
            }
        })

        adapter = RewardAdapter() { holder, reward ->
            // Item click
            holder.root.setOnClickListener {
                nav.navigate(R.id.updateRewardFragment, bundleOf("id" to reward.rewardID))
            }
            // Delete button click
            holder.btnDelete.setOnClickListener { delete(reward.rewardID) }
        }

        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vm.getResult().observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.txtItem.text = "${list.size} reward(s)"
        }


        return binding.root
    }

    private fun delete(id: String) {
        // TODO: Delete
        vm.delete(id)
    }

}