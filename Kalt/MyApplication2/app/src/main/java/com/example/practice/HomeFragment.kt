package com.example.practice

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.practice.databinding.ActivityHomeFragmentBinding


class HomeFragment : Fragment() {
    private lateinit var homePannelAdapter: HomepannelVpAdapter
    private val handler: Handler =  Handler()
    private var currentPage = 0
    private val DELAY_MS: Long = 3000  // 3초마다 페이지 변경
    lateinit var binding : ActivityHomeFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityHomeFragmentBinding.inflate(inflater,container,false)

        binding.homePannelTodayMusicAlbum1.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm,AlbumFragment())
                .commitAllowingStateLoss()
        }

        val bannerAdapter = bannerVpAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeViewpager.adapter = bannerAdapter
        binding.homeViewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        return binding.root
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