package com.example.practice.data.remote
import android.util.Log
import com.example.practice.ui.look.LookView
import com.example.practice.ui.main.splash.getRetrofit
import com.example.practice.data.entity.iTunesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SongService() {
    private lateinit var lookView: LookView

    fun setLookView(lookView: LookView) {
        this.lookView = lookView
    }

    fun getSongs(singer : String) {
        val songService = getRetrofit().create(SongRetrofitInterfaces::class.java)

        lookView.onGetSongLoading()

        songService.getSongs(singer).enqueue(object : Callback<iTunesResponse> {
            override fun onResponse(call: Call<iTunesResponse>, response: Response<iTunesResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val songResponse: iTunesResponse = response.body()!!

                    Log.d("SONG-RESPONSE", songResponse.toString())

                    when (val code = songResponse.resultCount) {
                        50 -> {
                            lookView.onGetSongSuccess(code, songResponse)
                        }

                        else -> lookView.onGetSongFailure(code, "오류")
                    }
                }
            }

            override fun onFailure(call: Call<iTunesResponse>, t: Throwable) {
                lookView.onGetSongFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }
}