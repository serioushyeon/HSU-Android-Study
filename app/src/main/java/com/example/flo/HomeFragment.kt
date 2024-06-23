package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.google.gson.Gson
import me.relex.circleindicator.CircleIndicator3

class HomeFragment : Fragment(), CommunicationInterface {

    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()

    //핸들러 설정
    var currentPosition = 0

    //ui 변경하기
//    val handler= Handler(Looper.getMainLooper()){
//        setPage()
//        true
//    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        /*        binding.homePannelAlbumTodayIv1.setOnClickListener() {
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm,AlbumFragment()).commitAllowingStateLoss()
        }*/
        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        albumRVAdapter.setItemClickListener(object : AlbumRVAdapter.OnItemClickListener {
            override fun onItemClick(album: Album) {
                /*(context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, AlbumFragment())
                    .commitAllowingStateLoss()*/
                changeToAlbumFragment(album)
            }

            override fun onPlayAlbum(album: Album) {
                sendData(album)
            }

        })
        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val homeAdapter = HomeVPAdapter(this)
        val homevp: ViewPager2 = binding.homeContentVp
        homeAdapter.addPannelFragment(PannelFragment1())
        homeAdapter.addPannelFragment(PannelFragment2())
        binding.homeContentVp.adapter = homeAdapter

        val indicator: CircleIndicator3 = binding.indicator
        indicator.setViewPager(homevp)


        albumDatas.apply {
            add(Album("Magnetic", "아일릿(ILLIT)", R.drawable.img_album_exp3))
            add(Album("Supernova", "에스파(aespa)", R.drawable.song_supernova))
            add(Album("소나기", "이클립스", R.drawable.song_eclipse))
//            add(Album("Run Run", "이클립스", R.drawable.song_eclipse))
//            add(Album("You&I", "이클립스", R.drawable.song_eclipse))

        }


        return binding.root
    }

    private fun changeToAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumToJson = gson.toJson(album)
                    putString("album", albumToJson)
                }
            })
            .commitAllowingStateLoss()

        //페이지 변경하기
//    fun setPage(){
//        if(currentPosition==5) currentPosition=0
//        pager.setCurrentItem(currentPosition,true)
//        currentPosition+=1
//    }
//    //2초 마다 페이지 넘기기
//    inner class PagerRunnable:Runnable{
//        override fun run() {
//            while(true){
//                Thread.sleep(2000)
//                handler.sendEmptyMessage(0)
//            }
//        }
//    }

    }

    override fun sendData(album: Album) {
        if (activity is MainActivity) {
            val activity = activity as MainActivity
            activity.updateMainPlayerCl(album)
        }
    }
}