package com.example.flo_clone.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_clone.MainActivity
import com.example.flo_clone.databinding.ActivityLoginBinding
import com.example.flo_clone.room.database.SongDatabase
import com.example.flo_clone.ui.register.RegisterActivity

class LoginActivity: AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setButton()
        setContentView(binding.root)
    }

    private fun makeToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun setButton() {
        binding.loginHomeBtn.setOnClickListener {
            startMainActivity()
        }

        binding.loginLoginBtn.setOnClickListener {
            login()
            makeToast("로그인에 성공했습니다.")
        }

        binding.loginRegisterTv.setOnClickListener {
           val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
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

        val songDB = SongDatabase.getInstance(this)!!
        val user = songDB.userDao().getUser(email, pwd)

        user?.let {
            Log.d("LOGIN GET USER", "userId: ${user.id}, $user")
            saveJwt(user.id)
            startMainActivity()
        }
        //makeToast("회원 정보가 존재하지 않습니다.")
    }

    private fun saveJwt(jwt: Int) {
        var spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putInt("jwt", jwt)
        editor.apply()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}