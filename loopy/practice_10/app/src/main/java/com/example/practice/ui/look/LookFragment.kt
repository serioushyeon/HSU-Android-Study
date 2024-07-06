package com.example.practice.ui.look

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.practice.ui.song.SingerRVAdapter
import com.example.practice.data.entity.Track
import com.example.practice.data.entity.iTunesResponse
import com.example.practice.data.remote.SongService
import com.example.practice.databinding.FragmentLookBinding


class LookFragment : Fragment(), LookView {
    private lateinit var binding: FragmentLookBinding
    private lateinit var floCharAdapter: SingerRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLookBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getSongs()
    }

    private fun initRecyclerView(result: List<Track>) {
        floCharAdapter = SingerRVAdapter(requireContext(), result)

        binding.lookFloChartRv.adapter = floCharAdapter
    }

    private fun getSongs() {
        val songService = SongService()
        songService.setLookView(this)

        binding.btSingerSearch.setOnClickListener {
            songService.getSongs(binding.etSingerSearch.text.toString())
        }

    }

    override fun onGetSongLoading() {
        binding.lookLoadingPb.visibility = View.VISIBLE
    }

    override fun onGetSongSuccess(code: Int, result: iTunesResponse) {
        binding.lookLoadingPb.visibility = View.GONE
        initRecyclerView(result.results)
    }

    override fun onGetSongFailure(code: Int, message: String) {
        binding.lookLoadingPb.visibility = View.GONE
        Log.d("LOOK-FRAG/SONG-RESPONSE", message)
    }
}