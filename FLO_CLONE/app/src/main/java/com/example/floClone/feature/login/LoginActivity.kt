package com.example.floClone.feature.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.floClone.feature.main.MainActivity
import com.example.floClone.core.data.model.remote.Result
import com.example.floClone.databinding.ActivityLoginBinding
import com.example.floClone.core.data.model.local.database.SongDatabase
import com.example.floClone.core.data.model.local.entities.UserEntity
import com.example.floClone.core.data.source.remote.AuthService
import com.example.floClone.core.domain.repository.LoginView
import com.example.floClone.feature.register.RegisterActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
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

        Log.d("카카오", "keyhash : ${Utility.getKeyHash(this)}")
        kakaoLogin()

        setButton()
        setContentView(binding.root)
    }

    private fun makeToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun kakaoLogin() {
        binding.loginKakaoLoginBtnIv.setOnClickListener {

            // 카톡 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this, callback = mCallback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback)
            }

        }
    }

    private fun setButton() {
        binding.loginHomeBtn.setOnClickListener {
            UserApiClient.instance.unlink  { error ->
                if (error != null) {
                    Log.e("Hello", "회원 탈퇴 실패. SDK에서 토큰 삭제됨", error)
                } else {
                    Log.i("Hello", "회원 탈퇴 성공. SDK에서 토큰 삭제됨")
                    makeToast("연결 끊기 성공")
                }
            }
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

    // 이메일 로그인 콜백
    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            when {
                error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                    Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                    Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                    Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                    Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                    Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                    Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.ServerError.toString() -> {
                    Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                    Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                }
                else -> { // Unknown
                    Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (token != null) {
            Log.e(TAG, "로그인 성공 ${token.accessToken}")
            makeToast("로그인에 성공하였습니다")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        }
    }


}