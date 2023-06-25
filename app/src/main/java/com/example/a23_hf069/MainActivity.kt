package com.example.a23_hf069

import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity() {
    lateinit var login_kakao : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val keyHash = getKeyHash(this)
        if (keyHash != null) {
            Log.d("getKeyHash", keyHash)
        } else {
            Log.d("getKeyHash", "Key hash is null")
        }

        val callback = { oAuthToken: OAuthToken?, throwable: Throwable? ->
            updateKakaoLoginUi()
            null // 반환 값은 Unit (null)입니다.
        }

        login_kakao = findViewById<Button>(R.id.btn_kakaoLogin)

        login_kakao.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    companion object {
        fun getKeyHash(context: Context): String? {
            val pm: PackageManager = context.packageManager
            try {
                val packageInfo = pm.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
                if (packageInfo == null)
                    return null

                for (signature in packageInfo.signatures) {
                    try {
                        val md: MessageDigest = MessageDigest.getInstance("SHA")
                        md.update(signature.toByteArray())
                        return Base64.encodeToString(
                            md.digest(),
                            Base64.NO_WRAP
                        )
                    } catch (e: NoSuchAlgorithmException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return null
        }
    }

    private fun updateKakaoLoginUi() {
        UserApiClient.instance.me { user: User?, throwable: Throwable? ->
            // 로그인이 되어있으면
            if (user != null) {
                // 유저의 아이디
                Log.d(TAG, "invoke: id" + user.id)
                // 유저 이메일
                Log.d(TAG, "invoke: email" + user.kakaoAccount?.email)
                // 유저 닉네임
                Log.d(TAG, "invoke: nickname" + user.kakaoAccount?.profile?.nickname)
                // 유저 성별
                Log.d(TAG, "invoke: gender" + user.kakaoAccount?.gender)
                // 유저 나이
                Log.d(TAG, "invoke: age" + user.kakaoAccount?.ageRange)
            } else {
                // 로그인이 안되어 있을 때
                Log.d(TAG, "로그인이 안되어 있넹")
            }
            return@me Unit // 반환 값은 Unit (null)입니다.
        }
    }
}
