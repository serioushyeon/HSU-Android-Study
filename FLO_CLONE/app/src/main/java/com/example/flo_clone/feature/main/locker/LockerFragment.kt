package com.example.flo_clone.feature.main.locker

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_clone.feature.main.MainActivity
import com.example.flo_clone.databinding.FragmentLockerBinding
import com.example.flo_clone.feature.login.LoginActivity
import com.example.flo_clone.core.ui.vp.LockerVPAdapter
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding

    private val information = arrayListOf("저장한 곡", "음악파일", "저장 앨범")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(layoutInflater)

        setVPAdapter()
        setButton()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initViews()
    }

    private fun setVPAdapter() {
        val lockerVPAdapter = LockerVPAdapter(this)
        binding.lockerContentVp.adapter = lockerVPAdapter

        // TabLayout + ViewPager2 연결
        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) {
            tab, positon ->
            tab.text = information[positon]
        }.attach()
    }

    private fun setButton() {
        binding.lockerLoginTv.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }

    private fun getJwt(): Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)
    }

    // 로그인 여부에 따라 로그인/로그아웃 진행
    private fun initViews() {
        val jwt: Int = getJwt()
        if (jwt ==0) {
            binding.lockerLoginTv.text = "로그인"
            binding.lockerLoginTv.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        } else {
            binding.lockerLoginTv.text = "로그아웃"
            binding.lockerLoginTv.setOnClickListener {
                logout()
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }

    private fun logout() {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()
        editor.remove("jwt")
        editor.apply()
    }

}