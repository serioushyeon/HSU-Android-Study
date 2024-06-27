package com.example.flo_clone.ui.album

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_clone.MainActivity
import com.example.flo_clone.R
import com.example.flo_clone.databinding.FragmentAlbumBinding
import com.example.flo_clone.room.database.SongDatabase
import com.example.flo_clone.room.entity.AlbumEntity
import com.example.flo_clone.room.entity.LikeEntity
import com.example.flo_clone.ui.home.HomeFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding
    private var gson: Gson = Gson()

    private val information = arrayListOf("수록곡", "상세정보", "영상")

    private var isLiked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(layoutInflater)

        setLayout()
        setButton()
        setVPAdapter()

        return binding.root
    }

    private fun setLayout() {
        val albumJson = arguments?.getString("album")
        val album = gson.fromJson(albumJson, AlbumEntity::class.java)
        // Home에서 넘어온 데이터 반영
        isLiked = isLikedAlbum(album.id)
        setInit(album)
        setonClickListeners(album)

        when (binding.albumTitleTv.text.toString()) {
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

    private fun setAlbumImage(drawable: Int) {
        binding.imgAlbumExpIv.setImageResource(drawable)
    }

    private fun setButton() {

        // 뒤로가기 버튼 클릭
        binding.btnAlbumBackIb.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, HomeFragment()).commitAllowingStateLoss()
        }
    }

    private fun setVPAdapter() {
        val albumVPAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumVPAdapter

        // TabLayout + ViewPager2 연결
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) {
            tab, postion ->
            tab.text = information[postion]
        }.attach()
    }

    private fun getJwt(): Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)
    }

    // 앨범 좋아요 처리
    private fun likeAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = LikeEntity(userId, albumId)

        songDB.albumDao().likeAlbum(like)
    }

    // 앨범이 좋아요 상태인지 확인
    private fun isLikedAlbum(albumId: Int): Boolean {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        val likeId: Int? = songDB.albumDao().isLikedAlbum(userId, albumId)

        return likeId != null
    }

    // 앨범의 좋아요를 취소
    private fun disLikedAlbum(albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        songDB.albumDao().disLikedAlbum(userId, albumId)
    }

    private fun setInit(album: AlbumEntity) {
        binding.imgAlbumExpIv.setImageResource(album.coverImg!!)
        binding.albumTitleTv.text = album.title.toString()
        binding.singerNameTv.text = album.singer.toString()

        if(isLiked) {
            binding.albumLikeIb.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.albumLikeIb.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun setonClickListeners(album: AlbumEntity) {
        val userId = getJwt()
        binding.albumLikeIb.setOnClickListener {
            if(isLiked) {
                binding.albumLikeIb.setImageResource(R.drawable.ic_my_like_off)
                disLikedAlbum(album.id)
            } else {
                binding.albumLikeIb.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.id)
            }
            // 버튼을 한번 누르고 나면 isLike 상태 업데이트
            isLiked = !isLiked
        }

    }

}