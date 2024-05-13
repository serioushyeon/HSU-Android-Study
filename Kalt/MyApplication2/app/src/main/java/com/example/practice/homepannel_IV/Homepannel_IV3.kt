package com.example.practice.homepannel_IV

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.practice.databinding.FragmentHomePannelIv3Binding
import com.example.practice.databinding.FragmentHomepannelIv1Binding

class Homepannel_IV3 : Fragment() {
    lateinit var binding : FragmentHomePannelIv3Binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePannelIv3Binding.inflate(inflater,container,false)
        return binding.root
    }
}