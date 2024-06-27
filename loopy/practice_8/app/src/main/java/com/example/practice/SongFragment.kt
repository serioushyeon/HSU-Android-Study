package com.example.practice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.practice.databinding.FragmentDetailBinding
import com.example.practice.databinding.FragmentSongBinding

class SongFragment : Fragment() {
    private lateinit var binding : FragmentSongBinding

    private var isState = false
    private var isSelected = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)
        setOnClick()
        return binding.root
    }

    private fun setOnClick(){
        with(binding){
            albumMyTasteToggleIv.setOnClickListener {
                if (isState) {
                    isState = false
                    it.setBackgroundResource(R.drawable.btn_toggle_on)
                } else {
                    isState = true
                    it.setBackgroundResource(R.drawable.btn_toggle_off)
                }
            }
            albumSelectToggleIv.setOnClickListener {
                if (isSelected) {
                    isSelected = false
                    it.setBackgroundResource(R.drawable.btn_playlist_select_off)
                } else {
                    isSelected = true
                    it.setBackgroundResource(R.drawable.btn_playlist_select_on)
                }
            }
        }
    }
}
