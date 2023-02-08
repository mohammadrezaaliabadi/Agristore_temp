package com.example.agristore.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.PointerIcon
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.agristore.R
import com.example.agristore.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {

    private val tabsArray = arrayOf(R.string.item, R.string.bill, R.string.customer)
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter =
            ViewPagerAdapter(this)
        val tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = getString(tabsArray[position])
        }
        tabLayoutMediator.attach()
        binding.tabLayout.getTabAt(0)?.icon = ContextCompat.getDrawable(requireContext(),R.drawable.ic_local_library_24)
        binding.tabLayout.getTabAt(1)?.icon = ContextCompat.getDrawable(requireContext(),R.drawable.ic_bill)
        binding.tabLayout.getTabAt(2)?.icon = ContextCompat.getDrawable(requireContext(),R.drawable.ic_person_24)


    }
}