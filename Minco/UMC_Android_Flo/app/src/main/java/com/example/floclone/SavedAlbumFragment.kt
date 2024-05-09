package com.example.floclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.floclone.databinding.FragmentAlbumBinding
import com.example.floclone.databinding.FragmentDetailBinding
import com.example.floclone.databinding.FragmentLockerSavedalbumBinding
import com.example.floclone.databinding.FragmentVideoBinding

class SavedAlbumFragment : Fragment(){
    lateinit var binding: FragmentLockerSavedalbumBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentLockerSavedalbumBinding.inflate(inflater,container,false)
        return binding.root
    }
}