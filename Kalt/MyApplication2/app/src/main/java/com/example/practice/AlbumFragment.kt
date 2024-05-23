package com.example.practice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.practice.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {
    lateinit var binding: FragmentAlbumBinding
    private val information = arrayListOf("수록곡", "상세정보","영상")
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val gson : Gson = Gson()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater,container,false)
        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        val albumJson = arguments?.getString("album")
        val album = gson.fromJson(albumJson,Album::class.java)
        setinit(album)

        val albumVpAdapter = AlbumVpAdapter(this)
        binding.albumContentVp.adapter = albumVpAdapter
        TabLayoutMediator(binding.albumContentTb,binding.albumContentVp){
            tab,position ->
            tab.text = information[position]
        }.attach()
        sharedViewModel.imageResource.observe(viewLifecycleOwner) { resourceId ->
            binding.albumAlbumIv.setImageResource(resourceId)
        }


        return binding.root
    }
    private fun setinit(album : Album){
        binding.albumMusicTitleTv.text = album.title
        binding.albumSingerNameTv.text = album.singer
        binding.albumAlbumIv.setImageResource(album.covering!!)
    }

}