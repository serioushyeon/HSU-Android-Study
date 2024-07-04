package com.serioushyeon.floclone.data.local

import com.serioushyeon.floclone.data.remote.AlbumChartResult

interface AlbumView {
    fun onGetAlbumSuccess(code: Int, result: AlbumChartResult)
    fun onGetAlbumFailure(code: Int, message: String)
}