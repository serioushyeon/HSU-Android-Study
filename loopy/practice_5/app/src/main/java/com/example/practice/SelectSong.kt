package com.example.practice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.practice.databinding.FragmentDetailBinding
import com.example.practice.databinding.FragmentSelectSongBinding

class SelectSong : Fragment() {
    private lateinit var binding : FragmentSelectSongBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectSongBinding.inflate(inflater, container, false)
        return binding.root
    }
}
