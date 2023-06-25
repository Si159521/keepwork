package com.example.a23_hf069

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class KakaoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "40b1a7e112e00e6e5cb97d6a3f4dbc8b")
    }
}