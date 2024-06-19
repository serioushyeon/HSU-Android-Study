package com.serioushyeon.floclone

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.serioushyeon.floclone.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()
    private lateinit var songDB: SongDatabase
    var currentPage = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!
        albumDatas.addAll(songDB.albumDao().getAlbums()) // songDB에서 album list를 가져옵니다.
        Log.d("albumlist", albumDatas.toString())

        // 더미데이터랑 Adapter 연결
        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        // 리사이클러뷰에 어댑터를 연결
        binding.homeTodayMusicListRv.adapter = albumRVAdapter
        binding.homeTodayMusicListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }
            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }
        })
        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val pannelAdapter = PannelVPAdapter(this)

        val song1 = Song("숲", "최유리", R.drawable.img_pannel_album1, 0, 60, false, "forest", false, false)
        val song2 = Song("Lucky Girl Syndrome", "아일릿(ILLIT)", R.drawable.img_pannel_album2, 0, 60, false, "luckygirlsyndrome", false, false)
        val song3 = Song("bad idea right?", "Olivia Rodrigo", R.drawable.img_pannel_album3, 0, 60, false, "badidearight", false, false)
        val song4 = Song("These Tears", "Andy Grammer", R.drawable.img_pannel_album4, 0, 60, false, "thesetears", false, false)
        val song5 = Song("ETA", "NewJeans", R.drawable.img_pannel_album5, 0, 60, false, "eta", false, false)
        val song6 = Song("네가 좋은 사람일 수는 없을까", "윤지영", R.drawable.img_pannel_album6, 0, 60, false, "goodperson", false, false)

        var pannelData1 = PannelData("싱그러운 봄비가 내리고", "총 80곡 2024.04.01", R.drawable.img_pannel_background1, song1, song1 )
        var pannelData2 = PannelData("브런치 카페에서", "총 54곡 2024.04.02", R.drawable.img_pannel_background2, song2, song2 )
        var pannelData3 = PannelData("생각이 많은 오후", "총 27곡 2024.04.03", R.drawable.img_pannel_background3, song3, song3 )
        var pannelData4 = PannelData("무기력한 하루, 근데 우울을 곁들인", "총 80곡 2024.04.04", R.drawable.img_pannel_background4, song4, song4 )
        var pannelData5 = PannelData("달리기 가장 좋은 날", "총 34곡 2024.04.05", R.drawable.img_pannel_background5, song5, song5 )
        var pannelData6 = PannelData("비 오는 날 감성적인", "총 72곡 2024.04.06", R.drawable.img_pannel_background6, song6, song6 )

        pannelAdapter.addFragment(PannelFragment(pannelData1))
        pannelAdapter.addFragment(PannelFragment(pannelData2))
        pannelAdapter.addFragment(PannelFragment(pannelData3))
        pannelAdapter.addFragment(PannelFragment(pannelData4))
        pannelAdapter.addFragment(PannelFragment(pannelData5))
        pannelAdapter.addFragment(PannelFragment(pannelData6))
        binding.homePannelBackgroundVp.adapter = pannelAdapter
        binding.homePannelBackgroundVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val pannelIndicator = binding.homePannelIndicator
        pannelIndicator.setViewPager(binding.homePannelBackgroundVp)

        val thread = Thread(PagerRunnable())
        thread.start()

        return binding.root
    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }

    inner class PagerRunnable:Runnable{
        override fun run() {
            while(true){
                try {
                    Thread.sleep(2000)
                    handler.sendEmptyMessage(0)
                } catch (e : InterruptedException){
                    Log.d("interupt", "interupt발생")
                }
            }
        }
    }

    val handler = Handler(Looper.getMainLooper()){
        setPage()
        true
    }

    fun setPage(){
        if(currentPage == 6)
            currentPage = 0
        binding.homePannelBackgroundVp.setCurrentItem(currentPage, true)
        currentPage += 1
    }


}