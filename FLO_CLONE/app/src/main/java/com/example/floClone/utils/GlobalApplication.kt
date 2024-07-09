package com.example.floClone.utils

import android.app.Application
import com.example.floClone.R
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // 카카오 SDK 초기화
        KakaoSdk.init(this, getString(R.string.kakao_native_app_key))
    }
}