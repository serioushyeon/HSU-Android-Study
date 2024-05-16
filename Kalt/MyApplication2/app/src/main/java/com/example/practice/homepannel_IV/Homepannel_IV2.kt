package com.example.practice.homepannel_IV

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.practice.databinding.FragmentHomePannelIv2Binding
import com.example.practice.databinding.FragmentHomepannelIv1Binding

class Homepannel_IV2 : Fragment() {
    lateinit var binding : FragmentHomePannelIv2Binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePannelIv2Binding.inflate(inflater,container,false)
        return binding.root
    }
}