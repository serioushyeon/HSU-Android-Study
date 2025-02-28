package com.example.floClone.core.data.source.remote

import android.util.Log
import com.example.floClone.core.data.api.SongRetrofitInterface
import com.example.floClone.core.data.model.remote.SongResponse
import com.example.floClone.core.data.di.getRetrofit
import com.example.floClone.core.domain.repository.LookView
import retrofit2.Callback
import retrofit2.Call

class SongService {
    private lateinit var lookView: LookView

    fun setLookView(lookView: LookView) {
        this.lookView = lookView
    }

    fun getSongs() {
        val songService = getRetrofit().create(SongRetrofitInterface::class.java)

        lookView.onGetSongLoading()

        songService.getSongs().enqueue(object : Callback<SongResponse> {
            override fun onResponse(p0: Call<SongResponse>, response: retrofit2.Response<SongResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val songResponse: SongResponse = response.body()!!

                    Log.d("SONG-RESPONSE", songResponse.toString())

                    when (val code = songResponse.code) {
                        1000 -> {
                            lookView.onGetSongSuccess(code, songResponse.result)
                        }
                        else -> lookView.onGetSongFailure(code, songResponse.message)
                    }
                }
            }

            override fun onFailure(p0: Call<SongResponse>, t: Throwable) {
                lookView.onGetSongFailure(400, "네트워크 오류가 발생했습니다.")
            }

        })
    }

}