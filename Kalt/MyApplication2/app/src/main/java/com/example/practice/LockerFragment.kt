package com.example.practice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.practice.databinding.ActivityLockerFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {
    lateinit var binding : ActivityLockerFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityLockerFragmentBinding.inflate(inflater,container,false)
        val adapter = lockerVpAdapter(this)
        binding.lockerViewpager.adapter = adapter
        TabLayoutMediator(binding.lockerTab, binding.lockerViewpager){ tab,position->
            //val tabView = LayoutInflater.from(context).inflate(R.layout.custom_tab, null) as TextView
            when(position){
                0-> tab.text = "저장한곡"
                1-> tab.text = "음악파일"
                //else->null
            }
            //tab.customView =tabView
        }.attach()

        return binding.root
    }
}