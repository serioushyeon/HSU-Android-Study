package com.example.floclone

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.floclone.databinding.ActivitySongBinding
import com.google.gson.Gson

//AppCompatActivity 상속을 받아야함 안드로이드에서 액티비티 기능을 사용할 수 있게 만들어둔 클래스
// 소괄호 ()를 상속받을 때는 넣어줘야함
class SongActivity : AppCompatActivity(){
    //선언은 지금하지만 나중에 초기화 할게
    //카멜 표기식
    //전역변수

    lateinit var binding : ActivitySongBinding
    lateinit var timer : Timer //타이머 변수 초기화
    private var mediaPlayer: MediaPlayer? = null //미디어 플레이어
    private var gson: Gson = Gson() //gson선언

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0


    private var isRepeatEnabled = false // 반복 재생 활성화 상태 저장
    private var isRandomEnabled = false // 랜덤 재생 활성화 상태 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //바인딩 초기화
        //이렇게 하는 형식을 기억한다!(바인딩)
        binding = ActivitySongBinding.inflate(layoutInflater)
        //최상단 (root)
        setContentView(binding.root)

        initPlayList()
        initSong()
        initClickListener()


        //반복재생
        binding.songRepeatIv.setOnClickListener{
            setRepeatStatus()
        }
        //랜덤재생
        binding.songRandomIv.setOnClickListener {
            setRandomStatus()
        }





    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initClickListener(){
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
        //하트
        binding.songLikeIv.setOnClickListener {
            //setLikeStatus(true)
            setLike(songs[nowPos].isLike)
            if(!songs[nowPos].isLike) {
                Toast.makeText(applicationContext, "하트를 취소했습니다.", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(applicationContext, "하트를 눌렀습니다.", Toast.LENGTH_SHORT).show()
            }



        }
//        //하트
//        binding.songLikeOnIv.setOnClickListener {
//            setLikeStatus(false)
//        }
        binding.songNextIv.setOnClickListener {
            moveSong(1)
        }
        binding.songPreviousIv.setOnClickListener {
            moveSong(-1)
        }
    }

    //song에서 데이터를 받아오는 함수
    private fun initSong(){
        //sharedpreference로 값을 받아온다음 비교후 인덱스 값 반환하는 방식으로 변경
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId",0)

        nowPos = getPlayingSongPosition(songId)

        Log.d("now SongId",songs[nowPos].id.toString())

        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun moveSong(direct: Int){
        if(nowPos + direct<0){
            Toast.makeText(this, "first song",Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos + direct>=songs.size){
            Toast.makeText(this, "last song",Toast.LENGTH_SHORT).show()
            return
        }
        nowPos += direct

        timer.interrupt()
        startTimer()

        mediaPlayer?.release() //미디어 플레이어가 갖고있던 리소스 해제
        mediaPlayer = null //미디어 플레이어 해제

        setPlayer(songs[nowPos])


    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for(i in 0 until songs.size){
            if(songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun setPlayer(song:Song){
        binding.songMusicTitleTv.text=song.title
        binding.songSingerNameTv.text=song.singer
        //시간
        binding.songStartTimeTv.text = String.format("%02d:%02d",song.second/60, song.second%60)
        binding.songEndTimeTv.text = String.format("%02d:%02d",song.playTime/60, song.playTime%60)
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        binding.songProgressSb.progress = (song.second*1000/song.playTime)

        //리소스 파일에서 string값으로 찾아서 리소스를 반환 -> resorce.getIdentifier
        val music = resources.getIdentifier(song.music, "raw",this.packageName)
        mediaPlayer = MediaPlayer.create(this,music) //미디어 플레이어에 어떤 음악이 실행될 지 알림

        if(song.isLike){
            binding.songLikeIv.setImageResource((R.drawable.ic_my_like_on))
        }
        else{
            binding.songLikeIv.setImageResource((R.drawable.ic_my_like_off))
        }

        setPlayerStatus(song.isPlaying)
       // setLikeStatus(song.isLike)

    }

    //재생 버튼 변경
    private fun setPlayerStatus(isPlaying :Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            binding.songMiniplayerIv.visibility= View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        }
        else{
            binding.songMiniplayerIv.visibility= View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true){ //mediaPlayer에서는 재생중이 아닐 때 pause하면 오류 => if문으로
                mediaPlayer?.pause()
            }
        }
    }

    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike,songs[nowPos].id)

        if(!isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

//    //하트 버튼 변경
//    private fun setLikeStatus(isLike :Boolean){
//        songs[nowPos].isLike = isLike
//
//        if(isLike){
//            binding.songLikeIv.visibility= View.GONE
//            binding.songLikeOnIv.visibility = View.VISIBLE
//
//        }
//        else{
//            binding.songLikeIv.visibility= View.VISIBLE
//            binding.songLikeOnIv.visibility = View.GONE
//
//        }
//    }


    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime,songs[nowPos].isPlaying)
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

    //사용자가 포커스를 잃었을 때 음악 중지
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        //binding.songStartTimeTv.setText("00:00") --> 시간을 초기화 하는건 어떻게?
        //binding.songEndTimeTv.setText("01:00")

        songs[nowPos].second = ((binding.songProgressSb.progress*songs[nowPos].playTime/100))/1000
        //song 데이터저장
        //앱이 종료되었다가 다시 실행되어도 저장된 데이터를 꺼내서 사용하게해줌,에디터라는 것 을 사용
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE) //song은 sharedPreference의 이름
        val editor = sharedPreferences.edit() //에디터를 사용해서 하나하나 넣어도 되지만 json으로 한번에 객체로 만들어 넣는다!
        // editor.putString("title",song.title)
        // editor.putString("title",song.singer),,,, //gson을 사용 -> 자바객체를 json으로 변환을 쉽게한다.
        //val songJson = gson.toJson(songs[nowPos]) //song객체를 json포멧으로 변환
        //앱이 종료될때는 song자체를 저장하는게 아니라 songid를 저장

        editor.putInt("songId",songs[nowPos].id)
        editor.apply() //git에서 commit과 push와 같은 작용을 함

    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()

        mediaPlayer?.release() //미디어 플레이어가 갖고있던 리소스 해제
        mediaPlayer = null //미디어 플레이어 해제
    }



}