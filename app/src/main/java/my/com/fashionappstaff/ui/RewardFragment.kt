package my.com.fashionappstaff.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import my.com.fashionappstaff.R
import my.com.fashionappstaff.databinding.FragmentRewardBinding


class RewardFragment : Fragment() {

    private lateinit var binding: FragmentRewardBinding
    private val nav by lazy { findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentRewardBinding.inflate(inflater, container, false)

        // TODO

        binding.btnAddReward.setOnClickListener { nav.navigate(R.id.insertRewardFragment) }

        binding.btnListReward.setOnClickListener { nav.navigate(R.id.listRewardFragment) }


        return binding.root
    }

}