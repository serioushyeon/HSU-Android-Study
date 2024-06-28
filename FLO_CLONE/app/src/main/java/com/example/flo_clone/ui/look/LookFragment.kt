package com.example.flo_clone.ui.look

import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_clone.R
import com.example.flo_clone.base.BaseFragment
import com.example.flo_clone.databinding.FragmentLookBinding
import com.example.flo_clone.dto.FloChartResult
import com.example.flo_clone.room.database.SongDatabase
import com.example.flo_clone.room.entity.SongEntity
import com.example.flo_clone.service.LookView
import com.example.flo_clone.service.SongService
import com.example.flo_clone.ui.SongRVAdapter

class LookFragment : BaseFragment<FragmentLookBinding>(R.layout.fragment_look), LookView {

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

    private lateinit var floCharAdapter: SongRVAdapter



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
        getSongs()
    }

    // 서버에서 음악 데이터 가져오는 함수
    private fun getSongs() {
        val songService = SongService()
        songService.setLookView(this)

        songService.getSongs()

    }

    // 리사이클러뷰 초기화
    private fun initRecyclerview(result: FloChartResult){
        floCharAdapter = SongRVAdapter(requireContext(), result)
        binding.lookFloChartRv.adapter = floCharAdapter
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

    // 로딩
    override fun onGetSongLoading() {
        //binding.lookLoadingPb.visibility = View.VISIBLE
    }

    // 로딩 제거
    override fun onGetSongSuccess(code: Int, result: FloChartResult) {
        //binding.lookLoadingPb.visibility = View.GONE
        initRecyclerview(result)

    }

    override fun onGetSongFailure(code: Int, message: String) {
        //binding.lookLoadingPb.visibility = View.GONE
        Log.d("LOOK-FRAG/SONG-RESPONSE", message)
    }
}