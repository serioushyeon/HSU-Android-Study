package com.example.floclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.floclone.databinding.FragmentHomeBinding
import java.util.Timer
import kotlin.concurrent.scheduleAtFixedRate


// 여기 HomeFragmet 클래스는 다시 해봐야함 .from 미니

//Fragment를 상속받음
class HomeFragment : Fragment() {
    //FragmentHomeBinding은 레이아웃 파일에 따라 자동으로 생성된 바인딩 클래스
    private lateinit var binding : FragmentHomeBinding // 뷰 바인딩
    private val timer = Timer()
    private val handler = Handler(Looper.getMainLooper())


    //onViewCreated() 메서드는 Fragment의 뷰가 생성된 후 호출
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    //onCreateView() 메서드는 Fragment의 UI를 생성하고 반환
    // 이 메서드를 오버라이드하여 Fragment의 뷰를 초기화하고 뷰 바인딩을 설정
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
            binding = FragmentHomeBinding.inflate(layoutInflater)
            binding.homeTodayAlbum1Iv.setOnClickListener {
                (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_layout, AlbumFragment()).commitAllowingStateLoss()
            }



            //------------------ 상단 추천 부분 ----------------------------//
            val homeRecommendAdapter = HomeRecommendVPAdapter(this)
            binding.homeRecommendVp.adapter=homeRecommendAdapter
            binding.homeRecommendVp.orientation=ViewPager2.ORIENTATION_HORIZONTAL
            startAutoSlide(homeRecommendAdapter)
            //인디케이터 연결(indicator)
            binding.homePannelIndicator.setViewPager(binding.homeRecommendVp)


            //------------------ 배너 부분 ----------------------------//
            val bannerAdapter= BannerVPAdapter(this)
            //이미지도 함께 넣어줌 -> BannerFragment.kt에서 인자 값을 int형으로 받음
            bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
            bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
            //뷰페이저와 어댑터 연결
            binding.homeBannerVp.adapter = bannerAdapter
            binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL //좌우로 스크롤 되게 함
            //배너와 인디케이터 연결
            binding.homeBannerIndicator.setViewPager((binding.homeBannerVp))
            return binding.root
    }

    private fun startAutoSlide(adpater : HomeRecommendVPAdapter) {
        // 일정 간격으로 슬라이드 변경 (3초마다)
        timer.scheduleAtFixedRate(3000, 3000) {
            handler.post {
                val nextItem = binding.homeRecommendVp.currentItem + 1
                if (nextItem < adpater.itemCount) {
                    binding.homeRecommendVp.currentItem = nextItem
                } else {
                    binding.homeRecommendVp.currentItem = 0 // 마지막 페이지에서 첫 페이지로 순환
                }
            }
        }
    }


}