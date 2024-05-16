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

class AlbumFragment : Fragment() {
    lateinit var binding: FragmentAlbumBinding
    private val information = arrayListOf("수록곡", "상세정보","영상")
    private val sharedViewModel: SharedViewModel by activityViewModels()
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
        /*binding.songLalacLayout.setOnClickListener{
            Toast.makeText(activity,"LILAC",Toast.LENGTH_SHORT).show()
        }*/

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

}