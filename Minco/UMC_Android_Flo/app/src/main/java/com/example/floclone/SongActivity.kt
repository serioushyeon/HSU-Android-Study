package com.example.floclone

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.floclone.databinding.ActivitySongBinding

//AppCompatActivity 상속을 받아야함 안드로이드에서 액티비티 기능을 사용할 수 있게 만들어둔 클래스
// 소괄호 ()를 상속받을 때는 넣어줘야함
class SongActivity : AppCompatActivity(){
    //선언은 지금하지만 나중에 초기화 할게
    //카멜 표기식
    //전역변수
    lateinit var song: Song
    lateinit var binding : ActivitySongBinding
    lateinit var timer : Timer //타이머 변수 초기화


    private var isRepeatEnabled = false // 반복 재생 활성화 상태 저장
    private var isRandomEnabled = false // 랜덤 재생 활성화 상태 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //바인딩 초기화
        //이렇게 하는 형식을 기억한다!(바인딩)
        binding = ActivitySongBinding.inflate(layoutInflater)
        //최상단 (root)
        setContentView(binding.root)
        initSong()
        setPlayer(song)

        //반복재생
        binding.songRepeatIv.setOnClickListener{
            setRepeatStatus()
        }
        //랜덤재생
        binding.songRandomIv.setOnClickListener {
            setRandomStatus()
        }


        //밑에 버튼 클릭했을 때 (오른쪽 상단, 아래 화살표)
        binding.songDownIb.setOnClickListener{
            //songActivity화면 전환 종료
            finish()
        }
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }


    }

    //song에서 데이터를 받아오는 함수
    private fun initSong(){
        if(intent.hasExtra("title")&&intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second",0)!!,
                intent.getIntExtra("playTime",0)!!,
                intent.getBooleanExtra("siplaying",false)
            )
        }
        startTimer()
    }

    private fun setPlayer(song:Song){
        binding.songMusicTitleTv.text=intent.getStringExtra("title")!!
        binding.songSingerNameTv.text=intent.getStringExtra("singer")!!
        //시간
        binding.songStartTimeTv.text = String.format("%02d:%02d",song.second/60, song.second%60)
        binding.songEndTimeTv.text = String.format("%02d:%02d",song.playTime/60, song.playTime%60)
        binding.songProgressSb.progress = (song.second*1000/song.playTime)

        setPlayerStatus(song.isPlaying)

    }

    //재생 버튼 변경
    private fun setPlayerStatus(isPlaying :Boolean){
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            binding.songMiniplayerIv.visibility= View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }
        else{
            binding.songMiniplayerIv.visibility= View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        }
    }

    private fun startTimer(){
        timer = Timer(song.playTime,song.isPlaying)
        timer.start()
    }

    //반복 재생 버튼변경
    private fun setRepeatStatus() {
        isRepeatEnabled = !isRepeatEnabled // 상태 토글
        if (isRepeatEnabled) {
            // 반복 재생 활성화: 색상을 flo색으로 변경
            val color = ContextCompat.getColor(applicationContext, R.color.flo) // 컨텍스트와 색상 지정
            binding.songRepeatIv.setColorFilter(
                color,
                android.graphics.PorterDuff.Mode.SRC_ATOP
            ) // 적용할 포터더프 모드
           // Toast.makeText(applicationContext, "반복 재생 활성화", Toast.LENGTH_SHORT).show()
        } else {
            // 반복 재생 비활성화: 색상 필터 제거
            binding.songRepeatIv.clearColorFilter()
           // Toast.makeText(applicationContext, "반복 재생 비활성화", Toast.LENGTH_SHORT).show()
        }
    }

    //랜덤 재생 버튼변경
    private fun setRandomStatus() {
        isRandomEnabled = !isRandomEnabled // 상태 토글
        if (isRandomEnabled) {
            // 반복 재생 활성화: 색상을 flo색으로 변경
            val color = ContextCompat.getColor(applicationContext, R.color.flo) // 컨텍스트와 색상 지정
            binding.songRandomIv.setColorFilter(
                color,
                android.graphics.PorterDuff.Mode.SRC_ATOP
            ) // 적용할 포터더프 모드
           // Toast.makeText(applicationContext, "랜덤 재생 활성화", Toast.LENGTH_SHORT).show()
        } else {
            // 반복 재생 비활성화: 색상 필터 제거
            binding.songRandomIv.clearColorFilter()
            //Toast.makeText(applicationContext, "랜덤 재생 비활성화", Toast.LENGTH_SHORT).show()
        }
    }

    //내부 클래스 외부 변수 접근하기 위함
    inner class Timer(private  val playTime: Int, var isPlaying: Boolean = true):Thread(){
        private var second : Int = 0
        private var mills : Float = 0f

        override fun run(){
            super.run()
            while(true){
                //노래 재생관리
                if(second >= playTime){
                    break
                }
                if(isPlaying){
                    sleep(50)
                    mills += 50

                    //뷰를 랜더링 runOnUiThead 사용(seekBar업데이트)
                    //seekBar적용
                    runOnUiThread {
                        binding.songProgressSb.progress = ((mills / playTime)*100).toInt()
                    }
                    //mills 1000단위마다 1초 증가
                    if(mills % 1000 == 0f){
                        runOnUiThread {
                            //뷰 렌더링 작업
                            binding.songStartTimeTv.text = String.format("%02d:%02d",second/60, second%60)
                        }
                        second++
                    }



                }
            }

        }
    }





}