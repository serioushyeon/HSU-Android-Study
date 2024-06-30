package com.example.floclone

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.floclone.databinding.FragmentLockerBinding
import com.example.floclone.databinding.FragmentLookBinding
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment: Fragment() {
    lateinit var binding:FragmentLockerBinding
    //탭 레이아웃에 들어갈 칸마다의 문자
    private val information = arrayListOf("저장한 곡","음악파일","저장앨범")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater,container,false)

        val lockerAdapter = LockerVPAdapter(this)
        binding.lockerContentVp.adapter = lockerAdapter
        //탭 레이아웃과 뷰 페이저 연결
        TabLayoutMediator(binding.lockerContentTb,binding.lockerContentVp){
            tab,position->
            tab.text=information[position]
        }.attach()

        binding.lockerLoginTv.setOnClickListener{
            startActivity(Intent(activity,LoginActivity::class.java))
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initViews()
    }

    //현재 jwt값이 있는지 확인하는 함수,jwt의 값을 가져온다.
    private fun getJwt(): Int{
        //앞에서 했던 auth로 받아오고, ?는 프래그먼트에서 사용할 때 사용하는 문법이라고 생각
        val spf = activity?.getSharedPreferences("auth",AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt",0) //sharedPreference에서 가져온 값이 없다면 0을 반환
    }
    //뷰의 텍스트를 로그인으로 할지 로그아웃으로 할지 결정을해서 뷰를 초기화
    private fun initViews(){
        val jwt : Int = getJwt()
        if (jwt == 0){ //jwt의 값이 없는 경우
            binding.lockerLoginTv.text = "로그인"
            binding.lockerLoginTv.setOnClickListener{
                startActivity(Intent(activity,LoginActivity::class.java))
            }
        }else{
            binding.lockerLoginTv.text = "로그아웃"
            binding.lockerLoginTv.setOnClickListener {
                //로그아웃 진행
                logout()
                startActivity(Intent(activity,MainActivity::class.java))
            }
        }
    }
    // JWT를 0인 상태로 만들어 로그아웃 처리하는 함수
    private fun logout() {
        // SharedPreferences 객체를 가져온다. (auth라는 이름으로)
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        // SharedPreferences를 수정할 수 있는 Editor 객체를 가져온다.
        val editor = spf!!.edit()
        // "jwt" 키에 해당하는 값을 제거한다.
        editor.remove("jwt")
        // 변경 사항을 적용한다.
        editor.apply()
    }


}


