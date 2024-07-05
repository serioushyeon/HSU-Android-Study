package com.example.flo_clone.feature.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.flo_clone.feature.main.MainActivity
import com.example.flo_clone.core.data.model.remote.Result
import com.example.flo_clone.databinding.ActivityLoginBinding
import com.example.flo_clone.core.data.model.local.database.SongDatabase
import com.example.flo_clone.core.data.model.local.entities.UserEntity
import com.example.flo_clone.core.data.source.remote.AuthService
import com.example.flo_clone.core.domain.repository.LoginView
import com.example.flo_clone.feature.register.RegisterActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient

class LoginActivity: AppCompatActivity(), LoginView {

    lateinit var binding: ActivityLoginBinding
    val TAG = "카카오"
    val nativeAppKey = "40b7e7514abcc43d0f197e8e094ad944"

    override fun onCreate(savedInstanceState: Bundle?) {
        //installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        KakaoSdk.init(this, nativeAppKey)
        Log.d("카카오", "keyhash : ${Utility.getKeyHash(this)}")
        kakaoLogin()

        setButton()
        setContentView(binding.root)
    }

    private fun makeToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun kakaoLogin() {
        binding.loginKakaoLoginBtnIv. setOnClickListener {
            // 카카오톡 설치 확인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                // 카카오톡 로그인
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    // 로그인 실패 부분
                    if (error != null) {
                        Log.e(TAG, "로그인 실패 $error")
                        // 사용자가 취소
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }
                        // 다른 오류
                        else {
                            UserApiClient.instance.loginWithKakaoAccount(
                                this,
                                callback = mCallback
                            ) // 카카오 이메일 로그인
                        }
                    }
                    // 로그인 성공 부분
                    else if (token != null) {
                        Log.e(TAG, "로그인 성공 ${token.accessToken}")
                        Toast.makeText(this, "카카오 로그인 성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(
                    this,
                    callback = mCallback
                ) // 카카오 이메일 로그인
            }
        }
    }

    // 이메일 로그인 콜백
    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "로그인 실패 $error")
        } else if (token != null) {
            Log.e(TAG, "로그인 성공 ${token.accessToken}")
        }
    }



    private fun setButton() {
        binding.loginHomeBtn.setOnClickListener {
            startMainActivity()
        }

        binding.loginLoginBtn.setOnClickListener {
            loginRoom()
            //login()
        }

        binding.loginRegisterTv.setOnClickListener {
           val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginRoom() {

        if(binding.loginEditEmailEt.text.toString().isEmpty() || binding.loginEditDotCom.text.isEmpty()) {
            makeToast("이메일을 입력해주세요.")
            return
        }

        if(binding.loginEditPasswordEt.text.toString().isEmpty()) {
            makeToast("비밀번호를 입력해주세요.")
            return
        }

        val email: String = binding.loginEditEmailEt.text.toString() + "@" + binding.loginEditDotCom.text.toString()
        val pwd: String = binding.loginEditPasswordEt.text.toString()

        val songDB = SongDatabase.getInstance(this)!!
        val user = songDB.userDao().getUser(email, pwd)

        user?.let {
            Log.d("LOGIN GET USER", "userId: ${user.id}, $user")
            saveJwt(user.id)
            makeToast("로그인에 성공했습니다.")
            startMainActivity()
        } ?: run {
            // 사용자가 존재하지 않는 경우
            makeToast("회원 정보가 존재하지 않습니다.")
        }
    }

    private fun login() {

        if(binding.loginEditEmailEt.text.toString().isEmpty() || binding.loginEditDotCom.text.isEmpty()) {
            makeToast("이메일을 입력해주세요.")
            return
        }

        if(binding.loginEditPasswordEt.text.toString().isEmpty()) {
            makeToast("비밀번호를 입력해주세요.")
            return
        }

        val email: String = binding.loginEditEmailEt.text.toString() + "@" + binding.loginEditDotCom.text.toString()
        val pwd: String = binding.loginEditPasswordEt.text.toString()

        val authService = AuthService()
        authService.setLoginView(this)

        authService.login(UserEntity(email, pwd, ""))
    }

    private fun saveJwt(jwt: Int) {
        var spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putInt("jwt", jwt)
        editor.apply()
    }

    // 서버에서 받아오는 jwt
    private fun saveJwt2(jwt: String) {
        var spf = getSharedPreferences("auth2", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("jwt", jwt)
        editor.apply()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onLoginSuccess(code: Int, result: Result) {
        when(code) {
            1000 -> {
                saveJwt2(result.jwt)
                startMainActivity()
            }
        }
    }

    override fun onLoginFailure() {
        // 로그인 실패 처리
    }


}