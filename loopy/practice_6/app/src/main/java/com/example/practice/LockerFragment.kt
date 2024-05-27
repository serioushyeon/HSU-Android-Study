package com.example.practice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.practice.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment : Fragment() {
    private lateinit var lockerAdapter: LockerAdapter
    private lateinit var binding: FragmentLockerBinding
    private val lockerTabList = arrayListOf("지정한 곡", "음악파일")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)
        setLockerViewPager()
        setLockerTab()
        return binding.root
    }

    private fun setLockerViewPager() {
        lockerAdapter = LockerAdapter(this)
        binding.lockerVp.adapter = lockerAdapter
    }

    private fun setLockerTab() {

        TabLayoutMediator(binding.lockerTp, binding.lockerVp) { tab, position ->
            tab.text = lockerTabList[position]
        }.attach()
    }

}
