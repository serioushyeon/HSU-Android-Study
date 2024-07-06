package com.example.practice.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.practice.ui.main.MainActivity
import com.example.practice.ui.etc.PanelAdapter
import com.example.practice.ui.etc.PanelFragment
import com.example.practice.ui.etc.PanelItem
import com.example.practice.R
import com.example.practice.data.local.SongDatabase
import com.example.practice.data.entity.Album
import com.example.practice.databinding.FragmentHomeBinding
import com.example.practice.ui.album.AlbumFragment
import com.example.practice.ui.album.AlbumRVAdapter
import com.example.practice.ui.banner.BannerAdapter
import com.example.practice.ui.banner.BannerFragment
import com.example.practice.utils.ForegroundService
import com.google.gson.Gson

class HomeFragment : Fragment() {
    lateinit var albumDB: SongDatabase
    private var albumDatas = ArrayList<Album>()

    private lateinit var binding: FragmentHomeBinding
    private lateinit var bannerAdapter : BannerAdapter
    private lateinit var panelAdapter: PanelAdapter
    private lateinit var handler: Handler
    private val nextTime = 3000L // 2초
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        initAlbumDatabase()
        onButtonClick()
        viewPager2Adapter()
        return binding.root
    }

    private fun initAlbumDatabase(){
        albumDB = SongDatabase.getInstance(requireContext())!!
        albumDatas.addAll(albumDB.albumDao().getAlbums())
    }

    private fun onButtonClick() {
        binding.service.setOnClickListener{
            val startIntent = Intent(requireActivity(), ForegroundService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requireActivity().startForegroundService(startIntent)
            } else {
                requireActivity().startService(startIntent)
            }

        }


//        albumDatas.apply {
//            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
//            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
//            add(Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3))
//            add(Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4))
//            add(Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5))
//            add(Album("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6))
//        }
        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.hsv.adapter = albumRVAdapter
        binding.hsv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        with(binding) {
            val fragment = AlbumFragment()
            val bundle = Bundle()


            albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener{
                override fun onItemClick(album: Album) {
                  putBundle(fragment,bundle,album)
                }

                override fun onRemoveAlbum(position: Int) {
                    TODO("Not yet implemented")
                }


                override fun onItemClick2(position: Int) {
                    val activity = activity as MainActivity?
                    activity?.updateValue(position)
                }
            })
//            ivAlbum1.setOnClickListener {
//                putBundle(fragment,bundle,tvAlbumName1.text.toString(), tvAlbumTitle1.text.toString())
//            }
//            ivAlbum2.setOnClickListener {
//                putBundle(fragment,bundle,tvAlbumName2.text.toString(), tvAlbumTitle2.text.toString())
//            }
//            ivAlbum3.setOnClickListener {
//                putBundle(fragment,bundle,tvAlbumName3.text.toString(), tvAlbumTitle3.text.toString())
//            }
//            ivAlbum4.setOnClickListener {
//                putBundle(fragment,bundle,tvAlbumName4.text.toString(), tvAlbumTitle4.text.toString())
//            }
//            ivAlbum5.setOnClickListener {
//                putBundle(fragment, bundle,tvAlbumName5.text.toString(), tvAlbumTitle5.text.toString())
//            }
        }



    }
    private fun goNextPage(fr : Fragment, bundle: Bundle, album: Album){
        fr.arguments = bundle
        activity?.supportFragmentManager!!.beginTransaction()
            .replace(R.id.fr_main, fr.apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album",albumJson)
                }
            })
            .commitAllowingStateLoss()
    }


    private fun viewPager2Adapter(){

        bannerAdapter = BannerAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        panelAdapter = PanelAdapter(this)
        panelAdapter.addFragment(
            PanelFragment(
                PanelItem("포근하게 덮어주는 \n꿈의 목소리","잠이 안온다","젠(zen)",
                    R.drawable.img_album_exp,
                    R.drawable.img_first_album_default
                )
            )
        )
        panelAdapter.addFragment(
            PanelFragment(
                PanelItem("포근하게 덮어주는 \n아이유 목소리","랴일락","아이유(IU)",
                    R.drawable.img_album_exp2,
                    R.drawable.img_second
                )
            )
        )
        panelAdapter.addFragment(
            PanelFragment(
                PanelItem("포근하게 덮어주는 \n에스파 목소리","NEXT LEVEL","에스파(aespa)",
                    R.drawable.img_album_exp3,
                    R.drawable.img_third
                )
            )
        )
        panelAdapter.addFragment(
            PanelFragment(
                PanelItem("포근하게 덮어주는 \n방탄소년단 목소리","Map Of The Seoul Persona","방탄소년단(BTS)",
                    R.drawable.img_album_exp4,
                    R.drawable.img_four
                )
            )
        )
        panelAdapter.addFragment(
            PanelFragment(
                PanelItem("포근하게 덮어주는 \n모모랜드 목소리","BAAM","모모랜드",
                    R.drawable.img_album_exp5,
                    R.drawable.img_five
                )
            )
        )
        panelAdapter.addFragment(
            PanelFragment(
                PanelItem("포근하게 덮어주는 \n태연 목소리","Weekend","태연",
                    R.drawable.img_album_exp6,
                    R.drawable.img_six
                )
            )
        )
        binding.homePanelBackgroundIv.adapter = panelAdapter
        binding.panelIdc.setViewPager(binding.homePanelBackgroundIv)
        panelAdapter.registerAdapterDataObserver(binding.panelIdc.adapterDataObserver)



        

//         마지막 페이지에서 다음으로 스와이프한 경우 첫 번째 페이지로 이동
        binding.homePanelBackgroundIv.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                //현재포지셩이 배열의 마지막 요소이면
                if (position == panelAdapter.itemCount - 1) {
                    binding.homePanelBackgroundIv.setCurrentItem(0, true)
                }
            }
        })


        handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val nextPosition = (binding.homePanelBackgroundIv.currentItem + 1) % panelAdapter.itemCount
                binding.homePanelBackgroundIv.setCurrentItem(nextPosition, true)
                handler.postDelayed(this, nextTime)
            }
        }
        handler.postDelayed(runnable, nextTime)
    }

    private fun putBundle(fr : Fragment ,bundle : Bundle ,  album: Album){
        goNextPage(fr,bundle,album)
    }

}