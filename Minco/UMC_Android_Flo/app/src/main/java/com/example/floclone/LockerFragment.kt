package com.example.floclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.floclone.databinding.FragmentLockerBinding
import com.example.floclone.databinding.FragmentLookBinding
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment: Fragment() {
    lateinit var binding:FragmentLockerBinding
    //탭 레이아웃에 들어갈 칸마다의 문자
    private val information = arrayListOf("저장한 곡","음악파일")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater,container,false)

        val lockerAdapter = LockerVPAdapter(this)
        binding.lockerContentVp.adapter = lockerAdapter
        //탭 레이아웃과 뷰 페이저 연결
        TabLayoutMediator(binding.lockerContentTb,binding.lockerContentVp){
            tab,position->
            tab.text=information[position]
        }.attach()

        return binding.root
    }

}


