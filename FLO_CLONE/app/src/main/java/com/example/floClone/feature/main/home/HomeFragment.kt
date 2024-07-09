package com.example.floClone.feature.main.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.floClone.feature.main.MainActivity
import com.example.floClone.R
import com.example.floClone.databinding.FragmentHomeBinding
import com.example.floClone.core.data.model.local.entities.AlbumEntity
import com.example.floClone.core.data.model.local.database.SongDatabase
import com.example.floClone.core.ui.rv.AlbumRvAdapter
import com.example.floClone.core.ui.vp.BannerVPAdapter
import com.example.floClone.core.ui.vp.PanelVPAdapter
import com.example.floClone.feature.main.home.banner.BannerFragment
import com.google.gson.Gson

data class SongPractice(val title: String, val singerName: String)

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding // 뷰 바인딩
    private var albumDatas = ArrayList<AlbumEntity>()

    private var currentPage = 0

    private lateinit var songDB: SongDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        setAlbumRvAdapter()
        setVPAdapter()
        setIndicator()

        runTimerThread()

        return binding.root
    }

    // ViewPager2 Adapter 설정 함수
    private fun setVPAdapter() {
        // 패널
        var panelVPAdapter = PanelVPAdapter(this)
        binding.homePanelVp.adapter = panelVPAdapter
        binding.homePanelVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // 배너
        val bannerVPAdapter = BannerVPAdapter(this)
        bannerVPAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerVPAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerVPAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerVPAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerVPAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    // Panel ViewPager2 에 대한 circleIndicatior 설정
    private fun setIndicator() {
        val circleIndicatior3 =  binding.circleIndicatior3
        circleIndicatior3.setViewPager(binding.homePanelVp)
        circleIndicatior3.createIndicators(7,0)
    }

    //페이지 변경하기
    private fun setPage(){
        if(currentPage == 7)
            currentPage = 0
        binding.homePanelVp.setCurrentItem(currentPage, true)
        currentPage+=1
    }

    // 타이머 스레드 실행
    private fun runTimerThread() {
        val thread = Thread(PagerRunnable())
        thread.start()
    }

    // UI 스레드 작업 수행을 위한 핸들러
    val handler= Handler(Looper.getMainLooper()){
        setPage()
        true
    }

    // 일정 시간 마다 페이지 자동 넘기기
    inner class PagerRunnable: Runnable {
        override fun run() {
            while (true) {
                try {
                    Thread.sleep(2000)
                    handler.sendEmptyMessage(0)
                } catch (e: InterruptedException) {
                    Log.e("로그", "InterruptedException")
                }
            }
        }
    }

    private fun setAlbumRvAdapter() {

        // songDB에서 album list를 가져옴
        songDB = SongDatabase.getInstance(requireContext())!!
        albumDatas.addAll(songDB.albumDao().getAlbums())

        // 더미데이터와 어뎁터 연결
        val albumRvAdapter = AlbumRvAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRvAdapter
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        albumRvAdapter.setOnItemClickListener(object: AlbumRvAdapter.OnItemClickListener{

            override fun onItemClick(album: AlbumEntity) {
                changeAlbumFragment(album)
            }

            override fun onPlayBtnClick(item: AlbumEntity) {
                val activity = activity as MainActivity?
                activity?.updateValue(item)
            }
        })
    }

    private fun changeAlbumFragment(albumEntity: AlbumEntity) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, com.example.floClone.feature.main.album.AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(albumEntity)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }

}