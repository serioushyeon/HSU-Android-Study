package com.example.floClone.core.domain.repository

import com.example.floClone.core.data.model.remote.FloChartResult

interface LookView {
    fun onGetSongLoading()
    fun onGetSongSuccess(code: Int, result: FloChartResult)
    fun onGetSongFailure(code: Int, message: String)
}