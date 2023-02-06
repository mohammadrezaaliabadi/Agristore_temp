package com.example.agristore.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.agristore.databinding.FragmentAddBillHomeBinding
import com.google.android.material.tabs.TabLayoutMediator


class AddBillHomeFragment : Fragment() {
    private val navigationArgs:AddBillHomeFragmentArgs by navArgs()
    private val tabsArray = arrayOf("Buy", "Card")
    private var _binding: FragmentAddBillHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddBillHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPagerAddBill.adapter = AddBillViewPagerAdapter(this, navigationArgs.customerId)
        TabLayoutMediator(
            binding.tabLayoutAddBill,
            binding.viewPagerAddBill
        ) { tab, positon -> tab.text = tabsArray[positon] }.attach()
    }

}