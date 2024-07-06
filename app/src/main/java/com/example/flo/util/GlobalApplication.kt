package com.example.flo.util

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "69485be8ab2b29d19f690d0843052d46")
    }
}