package com.example.flo_clone.core.data.source.remote

import android.util.Log
import com.example.flo_clone.core.data.api.AuthRetrofitInterface
import com.example.flo_clone.core.data.model.remote.AuthResponse
import com.example.flo_clone.core.data.di.getRetrofit
import com.example.flo_clone.core.data.model.local.entities.UserEntity
import com.example.flo_clone.core.domain.repository.LoginView
import com.example.flo_clone.core.domain.repository.SignUpView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView

    fun setSignUpView(signUpView: SignUpView) {
        this.signUpView = signUpView
    }

    fun setLoginView(loginView: LoginView) {
        this.loginView = loginView
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

    fun login(userEntity: UserEntity) {

        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        authService.login(userEntity).enqueue(object: Callback<AuthResponse> {
            override fun onResponse(p0: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("LOGIN/SUCCESS", response.toString())

                val res = response.body()!!
                when(val code = res.code) {
                    1000 -> loginView.onLoginSuccess(code, res.result!!)
                    else -> loginView.onLoginFailure()

                }
            }

            override fun onFailure(p0: Call<AuthResponse>, t: Throwable) {
                Log.d("LOGIN/FAILURE", t.message.toString())
            }
        })
        Log.d("LOGIN", "HELLO")
    }
}