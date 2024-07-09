package com.serioushyeon.floclone.data.remote

import android.util.Log
import com.serioushyeon.floclone.data.entities.User
import com.serioushyeon.floclone.data.local.LoginView
import com.serioushyeon.floclone.data.local.SignUpView
import com.serioushyeon.floclone.utils.getRetrofit
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

    fun signUp(user: User) {

        val signUpService = getRetrofit().create(AuthRetrofitInterface::class.java)

        signUpService.signUp(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val signUpResponse: AuthResponse = response.body()!!

                    Log.d("SIGNUP-RESPONSE", signUpResponse.toString())

                    when (val code = signUpResponse.code) {
                        1000 -> signUpView.onSignUpSuccess()
                        else -> {
                            signUpView.onSignUpFailure(signUpResponse)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUP-RESPONSE", t.message.toString())
            }
        })
        Log.d("signUp", "signUp")
    }

    fun login(user: User) {
        val loginService = getRetrofit().create(AuthRetrofitInterface::class.java)


        loginService.login(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUP-RESPONSE", response.toString())
                val resp: AuthResponse = response.body()!!

                when (val code = resp.code) {
                    1000 -> loginView.onLoginSuccess(code,resp.result!! )
                    else -> loginView.onLoginFailure()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("LOGIN-RESPONSE", t.message.toString())
            }
        })
        Log.d("login", "login")
    }
}