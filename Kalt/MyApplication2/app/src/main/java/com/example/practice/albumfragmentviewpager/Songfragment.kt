package com.example.practice.albumfragmentviewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.practice.R
import com.example.practice.SharedViewModel
import com.example.practice.databinding.FragmentSongBinding

class Songfragment :Fragment() {
    lateinit var binding: FragmentSongBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)

        binding.songMixoffTg.setOnClickListener {
             sharedViewModel.setImageResource(R.drawable.img_album_exp)
        }
        binding.songMixoffTg.setOnClickListener {
             binding.songMixoffTg.visibility=View.GONE
             binding.songMixonTg.visibility=View.VISIBLE
        }
        binding.songMixonTg.setOnClickListener {
            binding.songMixoffTg.visibility=View.VISIBLE
            binding.songMixonTg.visibility=View.GONE
        }
        return binding.root

    }

}
