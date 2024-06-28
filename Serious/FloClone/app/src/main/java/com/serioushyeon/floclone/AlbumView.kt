package com.serioushyeon.floclone

interface AlbumView {
    fun onGetAlbumSuccess(code: Int, result: AlbumChartResult)
    fun onGetAlbumFailure(code: Int, message: String)
}