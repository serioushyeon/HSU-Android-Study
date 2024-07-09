package com.example.practice.ui.locker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.practice.ui.login.LoginActivity
import com.example.practice.ui.main.MainActivity
import com.example.practice.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment : Fragment() {
    private lateinit var lockerAdapter: LockerAdapter
    private lateinit var binding: FragmentLockerBinding
    private val lockerTabList = arrayListOf("지정한 곡", "음악파일", "저장앨범")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)
        setLockerViewPager()
        setLockerTab()
        return binding.root
    }

    private fun getJwt():Int{
        val spf = activity?.getSharedPreferences("auth",AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt",0)
    }

    private fun initView(){
        val jwt : Int = getJwt()

        if (jwt == 0) {
            binding.loginBtnLocker.text = "로그인"
            binding.loginBtnLocker.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
        else{
            binding.loginBtnLocker.text = "로그아웃"
            binding.loginBtnLocker.setOnClickListener{
                logout()
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        initView()
    }

    private fun logout(){
        val spf = activity?.getSharedPreferences("auth",AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()
        editor.remove("jwt")
        editor.apply()
    }
    private fun setLockerViewPager() {
        lockerAdapter = LockerAdapter(this)
        binding.lockerVp.adapter = lockerAdapter
    }

    private fun setLockerTab() {

        TabLayoutMediator(binding.lockerTp, binding.lockerVp) { tab, position ->
            tab.text = lockerTabList[position]
        }.attach()

        binding.loginBtnLocker.setOnClickListener{
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }

}
