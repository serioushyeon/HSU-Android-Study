package com.example.floclone

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.floclone.databinding.ActivityLoginBinding
import com.example.floclone.databinding.ItemSongBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //회원가입 버튼을 눌렀을 때
        binding.loginSignUpTv.setOnClickListener{
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        //로그인 버튼을 눌렀을 때
        binding.loginSignInBtn.setOnClickListener {
            login()
        }
    }
    private fun login(){
        if(binding.loginIdEt.text.toString().isEmpty()|| binding.loginDirectInputEt.text.toString().isEmpty()){
            Toast.makeText(this,"이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.loginPasswordEt.text.toString().isEmpty()){
            Toast.makeText(this,"비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        val loginName : String = binding.loginIdEt.text.toString()
        val email : String = binding.loginIdEt.text.toString()+binding.loginDirectInputEt.text.toString()
        val pwd : String = binding.loginPasswordEt.text.toString()

        val songDB = SongDatabase.getInstance(this)!!
        val user = songDB.UserDao().getUser(email,pwd)

        if (user != null) {
            Log.d("LOGIN_ACT/GET_USER", "userId: ${user.id}, $user")
            saveJwt(user.id)
            Toast.makeText(this, "$loginName 님 로그인하셨습니다.", Toast.LENGTH_SHORT).show() // loginName을 포함한 토스트 메시지
            startMainActivity()
        } else {
            Toast.makeText(this, "회원정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    //jwt를 저장하는 함수
    private fun saveJwt(jwt:Int){
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putInt("jwt",jwt)
        editor.apply()
    }
    //메인화면으로 넘어가게 하는 함수
    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}