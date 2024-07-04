package com.serioushyeon.floclone.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.serioushyeon.floclone.ui.main.MainActivity
import com.serioushyeon.floclone.R
import com.serioushyeon.floclone.databinding.ActivitySplashBinding
import com.serioushyeon.floclone.ui.signin.LoginActivity

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_FloClone)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            initViews()
//            startActivity(Intent(this, MainActivity::class.java))
        }, 1000)
    }


    private fun initViews() {
        val jwt: String? = getJwt()

        if (jwt.equals("")){
            startActivity(Intent(this, LoginActivity::class.java))
        }
        else{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun getJwt(): String? {
        val spf = getSharedPreferences("auth2" , AppCompatActivity.MODE_PRIVATE)

        return spf!!.getString("jwt", "")
    }
}