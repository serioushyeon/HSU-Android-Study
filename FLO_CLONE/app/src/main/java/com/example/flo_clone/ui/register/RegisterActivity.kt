package com.example.flo_clone.ui.register

import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.flo_clone.R
import com.example.flo_clone.base.BaseActivity
import com.example.flo_clone.data.AuthResponse
import com.example.flo_clone.databinding.ActivityRegisterBinding
import com.example.flo_clone.module.getRetrofit
import com.example.flo_clone.retrofit.AuthRetrofitInterface
import com.example.flo_clone.room.database.SongDatabase
import com.example.flo_clone.room.entity.UserEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity: BaseActivity<ActivityRegisterBinding>(R.layout.activity_register) {

    override fun setLayout() {
        setButton()

    }

    private fun setButton() {
        binding.registerRegisterBtn.setOnClickListener {
            singUp()
            makeToast("회원가입이 완료되었습니다.")
            finish()
        }
    }

    private fun makeToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun getUser(): UserEntity {
        val email: String = binding.registerEmailEt.text.toString() + "@" + binding.registerEditDotCom.text.toString()
        val pwd: String = binding.registerPasswordEt.text.toString()
        var name: String = binding.registerNameEt.text.toString()

        return UserEntity(email, pwd, name)
    }

    private fun signUpRoom() {
        if(binding.registerEmailEt.text.toString().isEmpty() || binding.registerEditDotCom.text.isEmpty()) {
            makeToast("이메일 형식이 잘못되었습니다.")
            return
        }

        if(binding.registerNameEt.text.isEmpty()) {
            makeToast("이름 형식이 잘못되었습니다.")
            return
        }

        if(binding.registerPasswordEt.text.toString() != binding.registerPasswordCheckEt.text.toString()) {
            makeToast("비밀번호가 일치하지 않습니다.")
            return
        }

        val userDB = SongDatabase.getInstance(this)!!
        userDB.userDao().insert(getUser())

        val user = userDB.userDao().getUser()
        Log.d("SIGNUP", "user: ${user.toString()}")
    }

    private fun singUp() {

        if(binding.registerEmailEt.text.toString().isEmpty() || binding.registerEditDotCom.text.isEmpty()) {
            makeToast("이메일 형식이 잘못되었습니다.")
            return
        }

        if(binding.registerNameEt.text.isEmpty()) {
            makeToast("이름 형식이 잘못되었습니다.")
            return
        }

        if(binding.registerPasswordEt.text.toString() != binding.registerPasswordCheckEt.text.toString()) {
            makeToast("비밀번호가 일치하지 않습니다.")
            return
        }

        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        authService.signUp(getUser()).enqueue(object: Callback<AuthResponse>{
            override fun onResponse(p0: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUP/SUCCESS", response.toString())
                val res = response.body()!!
                when(res.code) {
                    1000 -> finish()
                    2016, 2018 -> {
                        //binding.signUpEmailErrorTv.visiblity = View.VISIBLE
                        //binding.signUpEmailErrorTv.text = res.message
                    }

                }
            }

            override fun onFailure(p0: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUP/FAILURE", t.message.toString())
            }
        })
        Log.d("SIGNUP", "HELLO")
    }

}