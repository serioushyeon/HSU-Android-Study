package com.example.floclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.floclone.databinding.FragmentAlbumBinding
import com.example.floclone.databinding.FragmentDetailBinding
import com.example.floclone.databinding.FragmentSongBinding

class SongFragment : Fragment(){
    lateinit var binding: FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSongBinding.inflate(inflater,container,false)

        binding.songLalacLayout.setOnClickListener {
            Toast.makeText(activity,"LILAC",Toast.LENGTH_SHORT).show()
        }
        binding.songMixoffTg.setOnClickListener {
            setSongMixToggle(false)
        }
        binding.songMixonTg.setOnClickListener {
            setSongMixToggle(true)
        }

        return binding.root
    }

    private fun setSongMixToggle(isToggleOn :Boolean){
        if(isToggleOn){
            binding.songMixoffTg.visibility= View.VISIBLE
            binding.songMixonTg.visibility = View.GONE
        }
        else{
            binding.songMixoffTg.visibility= View.GONE
            binding.songMixonTg.visibility = View.VISIBLE
        }
    }

}