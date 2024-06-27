package com.example.flo_clone.ui.register

import android.widget.Toast
import com.example.flo_clone.R
import com.example.flo_clone.base.BaseActivity
import com.example.flo_clone.databinding.ActivityRegisterBinding

class RegisterActivity: BaseActivity<ActivityRegisterBinding>(R.layout.activity_register) {

    override fun setLayout() {
        setButton()
    }

    private fun setButton() {
        binding.registerRegisterBtn.setOnClickListener {
            Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}