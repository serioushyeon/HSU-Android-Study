package com.example.floclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.floclone.databinding.FragmentHomeBinding
import com.google.gson.Gson
import java.util.Timer
import kotlin.concurrent.scheduleAtFixedRate


// 여기 HomeFragmet 클래스는 다시 해봐야함 .from 미니

//Fragment를 상속받음
class HomeFragment : Fragment() {
    //FragmentHomeBinding은 레이아웃 파일에 따라 자동으로 생성된 바인딩 클래스
    private lateinit var binding : FragmentHomeBinding // 뷰 바인딩
    private var albumDatas = ArrayList<Album>()
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
//            binding.homeTodayAlbum1Iv.setOnClickListener {
//                (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.frame_layout, AlbumFragment()).commitAllowingStateLoss()
//            }

        //데이터 리스트 생성 더미 데이터
        albumDatas.apply {
            add(Album("내일의 우리","카더가든",R.drawable.img_album_exp3))
            add(Album("Shake It Off","Tatlor Swift",R.drawable.img_album_exp))
            add(Album("우리의 사랑은","찰리빈웍스",R.drawable.img_album_exp4))
            add(Album("Ling Ling","검정치마",R.drawable.img_album_exp5))
            add(Album("Surf boy","혁오",R.drawable.img_album_exp6 ))
        }

        //어댑터 arrayList연결
        // 매개변수로 만들었던 데이터 리스트 던져줌
        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter //어떤 어댑터를 사용해야하는지 지정
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false )

        albumRVAdapter.setMyItemClickListener(object :AlbumRVAdapter.MyItemClickLitener{
            override fun onItemClick(album: Album) {
                //앨범 프레그먼트로 전환
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
               albumRVAdapter.removeItem(position)
            }

        })

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

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
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

