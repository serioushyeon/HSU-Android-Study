package com.example.practice

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.practice.databinding.ActivityHomeFragmentBinding
import com.google.gson.Gson


class HomeFragment : Fragment() {
    private lateinit var homePannelAdapter: HomepannelVpAdapter
    private val handler: Handler =  Handler()
    private var currentPage = 0
    private val DELAY_MS: Long = 3000  // 3초마다 페이지 변경
    lateinit var binding : ActivityHomeFragmentBinding
    private var albumdatas = ArrayList<Album>()
    private var currentPlayingPosition: Int = -1
    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying = false
    private lateinit var albumRVAdapter : AlbumRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityHomeFragmentBinding.inflate(inflater,container,false)

        /*binding.homePannelTodayMusicAlbum1.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm,AlbumFragment())
                .commitAllowingStateLoss()
        }*/
        // 데이터 리스트 생성 더미 데이터
        albumdatas.apply {
            add(Album("Butter","방탄소년단",R.drawable.img_album_exp , arrayListOf(Song("lsl","아이유")),arrayListOf(Music("lsl",R.raw.lilac))))
            add(Album("헤어지자 말해요","박재정",R.drawable.ballad_image1))
            add(Album("라일락","아이유",R.drawable.img_album_exp2))
        }


        binding.serviceStartButton.setOnClickListener{
            serviceStart(it)
         }
        binding.serviceEndButton.setOnClickListener{
            serviceStop(it)
        }
         albumRVAdapter = AlbumRecyclerViewAdapter(albumdatas, object : AlbumRecyclerViewAdapter.MyItemClickListener {
            override fun onitemClick(album: Album) {
                chagedeAlbimFragement(album)
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }
            override fun onPlaySong(song: Song, position: Int) {
                playSong(song, position)
            }
        },requireContext())
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        val bannerAdapter = bannerVpAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeViewpager.adapter = bannerAdapter
        binding.homeViewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        return binding.root
    }
    fun serviceStart(view: View){
        val intent = Intent(activity,Service::class.java)
        ContextCompat.startForegroundService(requireActivity(),intent)
    }
    fun serviceStop(view: View){
        val intent = Intent(activity,Service::class.java)
        requireActivity().stopService(intent)
    }
    private fun chagedeAlbimFragement(album : Album){
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm,AlbumFragment().apply{
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumjson = gson.toJson(album)
                    putString("album",albumjson)
                }
            })
            .commitAllowingStateLoss()
    }
    private fun playSong(song: Song, position: Int) {
        if (mediaPlayer != null) {
            mediaPlayer?.release()
        }

        mediaPlayer = MediaPlayer.create(context, R.raw.lilac)
        mediaPlayer?.start()
        isPlaying = true


        currentPlayingPosition = position
    }


    private val update: Runnable = object : Runnable {
        override fun run() {
            val totalpage = binding.homePannelVp.adapter?.itemCount ?:0
            if(currentPage>=totalpage){
                currentPage =0
            }
            binding.homePannelVp.setCurrentItem(currentPage++,true)
            binding.homePannelVp.postDelayed(this,DELAY_MS)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homepanneladapter = HomepannelVpAdapter(requireActivity())
        val viewpager2 = binding.homePannelVp
        val indicator = binding.homePannelIndicator
        val totalpage = binding.homePannelVp.adapter?.itemCount ?:0
        if(currentPage>=totalpage){
            currentPage = 0
        }
        viewpager2.adapter = homepanneladapter
        indicator.setViewPager(viewpager2)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(update, DELAY_MS)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(update)
    }

}