package com.example.practice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.practice.databinding.FragmentDetailBinding
import com.example.practice.databinding.FragmentMusicFileBinding

class MusicFile : Fragment() {
    private lateinit var binding : FragmentMusicFileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMusicFileBinding.inflate(inflater, container, false)
        return binding.root
    }
}
