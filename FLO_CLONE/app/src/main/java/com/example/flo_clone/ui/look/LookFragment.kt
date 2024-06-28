package com.example.flo_clone.ui.look

import android.widget.Button
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_clone.R
import com.example.flo_clone.base.BaseFragment
import com.example.flo_clone.databinding.FragmentLookBinding
import com.example.flo_clone.room.database.SongDatabase
import com.example.flo_clone.room.entity.SongEntity

class LookFragment : BaseFragment<FragmentLookBinding>(R.layout.fragment_look) {

    private lateinit var songDB: SongDatabase

    private lateinit var chartBtn : Button
    private lateinit var videoBtn : Button
    private lateinit var genreBtn : Button
    private lateinit var situationBtn : Button
    private lateinit var audioBtn : Button
    private lateinit var atmosphereBtn : Button

    private lateinit var buttonList: List<Button>

    private lateinit var chartTv : TextView
    private lateinit var videoTv : TextView
    private lateinit var genreTv : TextView
    private lateinit var situationTv : TextView
    private lateinit var audioTv : TextView
    private lateinit var atmosphereTv : TextView

    private lateinit var textList: List<TextView>

    private lateinit var nestedScrollView : NestedScrollView



    override fun setLayout() {
        songDB = SongDatabase.getInstance(requireContext())!!

        // 스크롤 뷰 초기화
        nestedScrollView = binding.nestedScrollView

        // 버튼 초기화
        chartBtn = binding.lookChartBtn
        videoBtn =  binding.lookVideoBtn
        genreBtn =  binding.lookGenreBtn
        situationBtn =  binding.lookSituationBtn
        audioBtn =  binding.lookAudioBtn
        atmosphereBtn =  binding.lookAtmostphereBtn

        buttonList = listOf(chartBtn, videoBtn, genreBtn, situationBtn, audioBtn, atmosphereBtn)

        // 텍스트 초기화
        chartTv = binding.lookChartTv
        videoTv = binding.lookVideoTv
        genreTv = binding.lookGenreTv
        situationTv = binding.lookSituationTv
        audioTv = binding.lookAudioTv
        atmosphereTv = binding.lookAtmostphereTv

        textList = listOf(chartTv, videoTv, genreTv, situationTv, audioTv, atmosphereTv)

        setButtonClickListeners()

    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        val recyclerView = binding.lookChartSongRv
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val lookAlbumRVAdapter = LookRVAdapter()

        binding.lookChartSongRv.adapter = lookAlbumRVAdapter
        lookAlbumRVAdapter.addSongs(songDB.songDao().getSongs() as ArrayList<SongEntity>)
    }

    private fun setButtonClickListeners() {
        for (i in buttonList.indices) {
            val button = buttonList[i]

            button.setOnClickListener {
                initButton(i)
            }
        }
    }

    private fun initButton(idx : Int) {
        for(presentBtn : Button in buttonList) {
            if(presentBtn == buttonList[idx]) {
                presentBtn.setBackgroundResource(R.drawable.selected_button)
            } else {
                presentBtn.setBackgroundResource(R.drawable.not_selected_button)
            }
        }
        nestedScrollView.smoothScrollTo(0, textList[idx].top)
    }
}