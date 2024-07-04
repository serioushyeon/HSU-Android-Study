package com.serioushyeon.floclone.ui.signup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import android.view.View
import com.serioushyeon.floclone.data.entities.User
import com.serioushyeon.floclone.data.local.SignUpView
import com.serioushyeon.floclone.data.remote.AuthResponse
import com.serioushyeon.floclone.data.remote.AuthService
import com.serioushyeon.floclone.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity(), SignUpView {

    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpSignUpBtn.setOnClickListener {
            signUp()
        }
    }

    private fun getUser(): User {
        val email: String = binding.signUpIdEt.text.toString() + "@" + binding.signUpDirectInputEt.text.toString()
        val pwd: String = binding.signUpPasswordEt.text.toString()
        val name: String = binding.signUpNameEt.text.toString()

        return User(email, pwd, name)
    }

    private fun signUp() {
        if (binding.signUpIdEt.text.toString().isEmpty() || binding.signUpDirectInputEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signUpNameEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이름 형식이 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signUpPasswordEt.text.toString() != binding.signUpPasswordCheckEt.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val authService = AuthService()
        authService.setSignUpView(this)

        authService.signUp(getUser())
    }

    override fun onSignUpSuccess() {
        finish()
    }

    override fun onSignUpFailure(resp: AuthResponse) {
        binding.signUpEmailErrorTv.text = resp.message
        binding.signUpEmailErrorTv.visibility = View.VISIBLE
    }
}