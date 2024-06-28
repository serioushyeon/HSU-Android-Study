package com.example.flo_clone.ui.register

import android.util.Log
import android.widget.Toast
import com.example.flo_clone.R
import com.example.flo_clone.base.BaseActivity
import com.example.flo_clone.databinding.ActivityRegisterBinding
import com.example.flo_clone.room.database.SongDatabase
import com.example.flo_clone.room.entity.UserEntity

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

        val userDB = SongDatabase.getInstance(this)!!
        userDB.userDao().insert(getUser())

        val user = userDB.userDao().getUser()
        Log.d("SIGNUP", "user: ${user.toString()}")
    }

    private fun signUp() {

    }

}