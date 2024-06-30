package com.example.practice

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practice.data.User
import com.example.practice.data.User2
import com.example.practice.databinding.ActivitySignUpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() , SignUpView{
    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpSignUpBtn.setOnClickListener {
            signUp()
            finish()
        }

    }

    private fun getUser(): User {
        val email: String =
            binding.signUpIdEt.text.toString() + "@" + binding.signUpDirectInputEt.text.toString()
        val password: String = binding.signUpPasswordEt.text.toString()
        return User(email, password)
    }


    private fun getUser2(): User2 {
        val email: String =
            binding.signUpIdEt.text.toString() + "@" + binding.signUpDirectInputEt.text.toString()
        val password: String = binding.signUpPasswordEt.text.toString()
        val name = binding.signUpNameEt.text.toString()
        return User2(email, password, name)
    }

    private fun signUp() {
        if (binding.signUpIdEt.text.toString()
                .isEmpty() || binding.signUpDirectInputEt.text.toString().isEmpty()
        ) {
            Toast.makeText(this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.signUpPasswordEt.text.toString() != binding.signUpPasswordCheckEt.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val userDB = SongDatabase.getInstance(this)!!
        userDB.userDao().insert(getUser())

        val user = userDB.userDao().getUsers()
        Log.d("SignUpACT", user.toString())
    }


    private fun signUp2() {
        if (binding.signUpIdEt.text.toString()
                .isEmpty() || binding.signUpDirectInputEt.text.toString().isEmpty()
        ) {
            Toast.makeText(this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signUpNameEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이름 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.signUpPasswordEt.text.toString() != binding.signUpPasswordCheckEt.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

//        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
//        authService.signUp(getUser2()).enqueue(object : Callback<AuthResponse> {
//            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
//                Log.d("SIGNUP/SUCCESS",response.toString())
//                val response : AuthResponse = response.body()!!
//                when(response.code){
//                    1000 -> finish()
//                    2016, 2018 -> {
//                        binding.signUpEmailErrorTv.visibility = View.VISIBLE
//                        binding.signUpEmailErrorTv.text = response.message
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
//                Log.d("SIGNUP/FAILURE", t.message.toString())
//
//            }
//        })
//        Log.d("SIGNUP/FAILURE","HELLO")
        val authService = AuthService()
        authService.setSignUpView(this)

        authService.signUp(getUser2())
    }

    override fun onSignUpSuccess() {
        finish()
    }
    override fun onSignUpFailure(){

    }
}