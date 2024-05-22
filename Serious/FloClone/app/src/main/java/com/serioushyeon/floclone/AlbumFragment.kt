package com.serioushyeon.floclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.serioushyeon.floclone.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {
    lateinit var binding : FragmentAlbumBinding
    private var gson: Gson = Gson()
    private val information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater,container,false)

        // Home 에서 넘어온 데이터 받아오기
        val albumJson = arguments?.getString("album")
        val album = gson.fromJson(albumJson, Song::class.java)
        // Home에서 넘어온 데이터를 반영
        setInit(album)
        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        val albumAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp){
                tab, position ->
            tab.text = information[position]
        }.attach()

        binding.albumLikeIv.setOnClickListener {
            setLikeStatus(true)
        }
        binding.albumLikeOnIv.setOnClickListener {
            setLikeStatus(false)
        }
        return binding.root
    }
    private fun setInit(album: Song) {
        binding.albumAlbumIv.setImageResource(album.img)
        binding.albumMusicTitleTv.text = album.title
        binding.albumSingerNameTv.text = album.singer
    }

    fun setLikeStatus(isLike : Boolean){
        if(isLike){
            binding.albumLikeOnIv.visibility = View.VISIBLE
            binding.albumLikeIv.visibility = View.GONE
        }
        else {
            binding.albumLikeOnIv.visibility = View.GONE
            binding.albumLikeIv.visibility = View.VISIBLE
        }
    }
}