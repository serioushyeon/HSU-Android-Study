package com.example.practice

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.practice.data.Album
import com.example.practice.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {
    private lateinit var binding: FragmentAlbumBinding
    private var isState = false
    private var isSelected = false
    private var isLike = false
    private lateinit var albumAdapter : AlbumAdapter
    private val infoTab = arrayListOf("수록곡","상세정보","영상")
    private var gson = Gson()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        setAlbumAdapter()
        setTabLayout()
        with(binding) {

            val albumJson = arguments?.getString("album")
            val album = gson.fromJson(albumJson, Album::class.java)
            setInit(album)
            Log.d("값", "arguments: $arguments")
            Log.d("값", "${arguments?.getString("title")} ${arguments?.getString("singer")}")
            albumTitleTv.text = arguments?.getString("title")
            albumSingerTv.text = arguments?.getString("singer")
            when (albumTitleTv.text.toString()) {
                "LILAC" -> {
                    setAlbumImage(R.drawable.img_album_exp2)
                }

                "NEXT LEVEL" -> {
                    setAlbumImage(R.drawable.img_album_exp3)
                }

                "Map Of The Soul" -> {
                    setAlbumImage(R.drawable.img_album_exp4)
                }

                "BAAM" -> {
                    setAlbumImage(R.drawable.img_album_exp5)
                }

                "Weekend" -> {
                    setAlbumImage(R.drawable.img_album_exp6)
                }
            }
        }
        onClickBackButton()
        return binding.root
    }

    private fun setInit(album: Album) {
        binding.albumPreviewIv.setImageResource(album.coverImg!!)
        binding.albumTitleTv.text = album.title.toString()
        binding.albumSingerTv.text = album.singer.toString()
    }

    private fun setAlbumImage(drawable: Int) {
        binding.albumPreviewIv.setImageResource(drawable)
    }

    private fun setAlbumAdapter(){
        albumAdapter = AlbumAdapter(this)
        binding.albumContentVp.adapter = albumAdapter
    }

    private fun setTabLayout(){
        TabLayoutMediator(binding.albumInfoTb, binding.albumContentVp){
            tab, position ->
            tab.text = infoTab[position]
        }.attach()
    }

    private fun onClickBackButton() {
        with(binding) {
            albumArrowBackIb.setOnClickListener {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fr_main, HomeFragment()).commitAllowingStateLoss()
            }


            albumMyLike.setOnClickListener {
                if (isLike) {
                    isLike = false
                    it.setBackgroundResource(R.drawable.ic_my_like_off)
                } else {
                    it.setBackgroundResource(R.drawable.ic_my_like_on)
                }
            }

        }
    }
}