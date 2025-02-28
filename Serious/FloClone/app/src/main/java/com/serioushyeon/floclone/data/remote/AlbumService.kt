package com.serioushyeon.floclone.data.remote

import android.util.Log
import com.serioushyeon.floclone.data.local.AlbumView
import com.serioushyeon.floclone.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumService {
    private lateinit var albumView: AlbumView

    fun setAlbumView(albumView: AlbumView) {
        this.albumView = albumView
    }

    fun getAlbums() {
        val albumService = getRetrofit().create(AlbumRetrofitInterfaces::class.java)

//        albumView.onGetAlbumLoading()

        albumService.getAlbums().enqueue(object : Callback<AlbumResponse> {
            override fun onResponse(call: Call<AlbumResponse>, response: Response<AlbumResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val albumResponse: AlbumResponse = response.body()!!

                    Log.d("ALBUM-RESPONSE", albumResponse.toString())

                    when (val code = albumResponse.code) {
                        1000 -> {
                            albumView.onGetAlbumSuccess(code, albumResponse.result!!)
                        }

                        else -> albumView.onGetAlbumFailure(code, albumResponse.message)
                    }
                }
            }

            override fun onFailure(call: Call<AlbumResponse>, t: Throwable) {
                albumView.onGetAlbumFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
        Log.d("getAlbums", "getAlbums")
    }
}