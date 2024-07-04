package com.serioushyeon.floclone.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.serioushyeon.floclone.ui.main.home.banner.BannerFragment
import com.serioushyeon.floclone.ui.main.home.banner.BannerVPAdapter
import com.serioushyeon.floclone.ui.main.MainActivity
import com.serioushyeon.floclone.R
import com.serioushyeon.floclone.data.entities.Album
import com.serioushyeon.floclone.data.local.AlbumView
import com.serioushyeon.floclone.data.local.SongDatabase
import com.serioushyeon.floclone.data.remote.AlbumChartResult
import com.serioushyeon.floclone.data.remote.AlbumChartSongs
import com.serioushyeon.floclone.data.remote.AlbumService
import com.serioushyeon.floclone.databinding.FragmentHomeBinding
import com.serioushyeon.floclone.ui.main.album.AlbumFragment
import com.serioushyeon.floclone.ui.main.album.AlbumRVAdapter

class HomeFragment : Fragment(), AlbumView {

    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()
    private lateinit var albumAdapter: AlbumRVAdapter

    private lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        return binding.root
    }

    private fun changeAlbumFragment(albumChartSongs: AlbumChartSongs) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(albumChartSongs)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }

    override fun onStart() {
        super.onStart()
        getAlbums()
    }

    private fun initRecyclerView(result: AlbumChartResult) {
        albumAdapter = AlbumRVAdapter(requireContext(), result)

        binding.homeTodayMusicListRv.adapter = albumAdapter

        albumAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(albumChartSongs: AlbumChartSongs) {
                changeAlbumFragment(albumChartSongs)
            }

            override fun onRemoveAlbum(position: Int) {
                albumAdapter.removeItem(position)
            }
        })
    }

    private fun getAlbums() {
        val albumService = AlbumService()
        albumService.setAlbumView(this)

        albumService.getAlbums()

    }

    override fun onGetAlbumSuccess(code: Int, result: AlbumChartResult) {
        initRecyclerView(result)
    }

    override fun onGetAlbumFailure(code: Int, message: String) {
        Log.d("LOOK-FRAG/SONG-RESPONSE", message)
    }


}