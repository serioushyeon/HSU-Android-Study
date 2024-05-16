package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import me.relex.circleindicator.CircleIndicator3

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    //핸들러 설정
    var currentPosition=0
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

        binding.homePannelAlbumTodayIv1.setOnClickListener() {
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm,AlbumFragment()).commitAllowingStateLoss()
        }

        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val homeAdapter = HomeVPAdapter(this)
        val homevp : ViewPager2 = binding.homeContentVp
        homeAdapter.addPannelFragment(PannelFragment1())
        homeAdapter.addPannelFragment(PannelFragment2())
        binding.homeContentVp.adapter = homeAdapter

        val indicator:CircleIndicator3 = binding.indicator
        indicator.setViewPager(homevp)



        return binding.root
    }
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