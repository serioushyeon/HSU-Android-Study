package com.serioushyeon.floclone.ui.signin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Toast
import com.serioushyeon.floclone.ui.main.MainActivity
import com.serioushyeon.floclone.ui.signup.SignUpActivity
import com.serioushyeon.floclone.data.entities.User
import com.serioushyeon.floclone.data.local.LoginView
import com.serioushyeon.floclone.data.remote.AuthService
import com.serioushyeon.floclone.data.remote.Result
import com.serioushyeon.floclone.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity(), LoginView {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginSignInBtn.setOnClickListener {
            login()
        }
    }

    private fun login() {
        if (binding.loginIdEt.text.toString().isEmpty() || binding.loginDirectInputEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.loginPasswordEt.text.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email = binding.loginIdEt.text.toString() + "@" + binding.loginDirectInputEt.text.toString()
        val password = binding.loginPasswordEt.text.toString()

        val authService = AuthService()
        authService.setLoginView(this)

        authService.login(User(email, password, ""))

        //Toast.makeText(this, "회원 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun saveJwt2(jwt: String) {
        val spf = getSharedPreferences("auth2" , MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("jwt", jwt)
        editor.apply()
    }

    override fun onLoginSuccess(code : Int , result: Result) {
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