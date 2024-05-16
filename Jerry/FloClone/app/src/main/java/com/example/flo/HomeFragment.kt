package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var currentPage = 0
    lateinit var thread:Thread

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thread = Thread(PagerRunnable())
        thread.start()
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homePannelAlbumImgIv1.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm,AlbumFragment())
                .commitAllowingStateLoss()
        }

        binding.homePannelAlbumImgIv2.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm,AlbumFragment())
                .commitAllowingStateLoss()
        }

        binding.homePannelAlbumImgIv3.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm,AlbumFragment())
                .commitAllowingStateLoss()
        }

        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val pannelAdapter = PannelVPAdapter(this)
        pannelAdapter.addFragment(PannelFragment(R.drawable.img_first_album_default))
        pannelAdapter.addFragment(PannelFragment(R.drawable.img_album_exp))
        pannelAdapter.addFragment(PannelFragment(R.drawable.img_album_exp3))
        pannelAdapter.addFragment(PannelFragment(R.drawable.img_album_exp4))
        pannelAdapter.addFragment(PannelFragment(R.drawable.img_album_exp5))
        pannelAdapter.addFragment(PannelFragment(R.drawable.img_album_exp6))
        binding.homePannelVp.adapter = pannelAdapter
        binding.homePannelVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.indicator.setViewPager(binding.homePannelVp)

        return binding.root
    }
    var handler=Handler(Looper.getMainLooper()){
        setPage()
        true
    }

    fun setPage(){
        if(currentPage == 6)
            currentPage = 0
        binding.homePannelVp.setCurrentItem(currentPage, true)
        currentPage+=1
    }
    inner class PagerRunnable():Runnable{
        override fun run() {
            while(true){
                try{
                    Thread.sleep(3000)
                    handler.sendEmptyMessage(0)
                } catch (e : InterruptedException){
                    Log.d("interupt", "interupt발생")
                }
            }
        }

    }
}