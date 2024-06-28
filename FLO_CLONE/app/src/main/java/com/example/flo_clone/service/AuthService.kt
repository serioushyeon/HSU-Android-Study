package com.example.flo_clone.service

import android.util.Log
import com.example.flo_clone.data.AuthResponse
import com.example.flo_clone.module.getRetrofit
import com.example.flo_clone.room.entity.UserEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private lateinit var signUpView: SignUpView

    fun setSignUpView(signUpView: SignUpView) {
        this.signUpView = signUpView
    }

    fun signUp(userEntity: UserEntity) {

        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        authService.signUp(userEntity).enqueue(object: Callback<AuthResponse> {
            override fun onResponse(p0: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUP/SUCCESS", response.toString())
                val res = response.body()!!
                when(res.code) {
                    1000 -> signUpView.onSignUpSuccess()
                    else -> signUpView.onSignUpFailure()

                }
            }

            override fun onFailure(p0: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUP/FAILURE", t.message.toString())
            }
        })
        Log.d("SIGNUP", "HELLO")
    }
}