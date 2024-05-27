package com.example.floclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.floclone.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment :Fragment(){
    private lateinit var binding: FragmentAlbumBinding

    private var gson:Gson = Gson()

    //탭 레이아웃에 들어갈 칸마다의 문자
    private val information = arrayListOf("수록곡","상세정보","영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater,container,false)

        val albumJson = arguments?.getString("album")
        val album = gson.fromJson(albumJson,Album::class.java)
        setInit(album)

        binding.albumBackIv.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_layout, HomeFragment()).commitAllowingStateLoss()
        }

        //밑에 앨범 어댑터를 이용해서 붙인다.
        val albumAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumAdapter
        //탭레이아웃과 뷰페이저 연결
        TabLayoutMediator(binding.albumContentTb,binding.albumContentVp){
            //position은 람다함수 안에서 사용되는 매개변수
            tab, position ->
            tab.text=information[position]
        }.attach()

        return binding.root
    }

    private fun setInit(album:Album){
        binding.albumAlbumIv.setImageResource(album.coverImg!!)
        binding.albumMusicTitleTv.text = album.title.toString()
        binding.albumSingerNameTv.text = album.singer.toString()
    }
}