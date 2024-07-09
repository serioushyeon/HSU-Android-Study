package com.example.practice.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practice.ui.main.MainActivity
import com.example.practice.data.local.SongDatabase
import com.example.practice.data.entity.Result
import com.example.practice.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity(), LoginVIew {
    lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Toast.makeText(this, "로그인 실패: ${error.message}", Toast.LENGTH_SHORT).show()
            } else if (token != null) {
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                // 사용자 정보 요청
                UserApiClient.instance.me { user, meError ->
                    if (meError != null) {
                        Toast.makeText(this, "사용자 정보 요청 실패: ${meError.message}", Toast.LENGTH_SHORT).show()
                    } else if (user != null) {
                        Toast.makeText(this, "사용자 정보 요청 성공\n이름: ${user.kakaoAccount?.profile?.nickname}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignUpTv.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.loginSignInBtn.setOnClickListener{
            login()
        }
        binding.loginKakakoLoginIv.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    private fun login(){
        if (binding.loginIdEt.text.toString().isEmpty() || binding.loginDirectInputEt.text.toString().isEmpty()){
            Toast.makeText(this, "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.loginPasswordEt.text.toString().isEmpty()){
            Toast.makeText(this, "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        val email : String = binding.loginIdEt.text.toString() + "@" + binding.loginDirectInputEt.text.toString()
        val password : String = binding.loginPasswordEt.text.toString()

        val songDB = SongDatabase.getInstance(this)!!
        val user = songDB.userDao().getUser(email,password)

        user?.let{
            Log.d("LOGIN_ACT/GET_USER","userId : ${user.id},$user")
            saveJwt(user.id)
            startMainActivity()

        }
//        val authService = AuthService()
//        authService.setLoginView(this)
//
//        authService.login(User2(email,password,""))

        if(user.toString().isEmpty())
            Toast.makeText(this,"회원정보가 없습니다.",Toast.LENGTH_SHORT).show()
    }

    private fun saveJwt(jwt : Int){
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()
        editor.putInt("jwt",jwt)
        editor.apply()
    }

    private fun saveJwt2(jwt : String){
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()
        editor.putString("jwt",jwt)
        editor.apply()
    }

    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onLoginSuccess(code : Int, result: Result) {
        when(code) {
            1000 -> {
                saveJwt2(result.jwt)
                startMainActivity()
            }
        }
    }

    override fun onLoginFailure() {
        TODO("Not yet implemented")
    }
}