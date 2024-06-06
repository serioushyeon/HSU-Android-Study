package com.example.practice

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.practice.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {
    lateinit var binding : ActivitySongBinding
    lateinit var timer : Timer
    private var mediaPlayer : MediaPlayer? = null
    private var gson: Gson = Gson()
    private var checking : Boolean  = false
    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initPlayList()
        initsong()
        initclicklistener()
        binding.repeatInactiveIv.setOnClickListener {
            binding.repeatInactiveIv.visibility = View.GONE
            binding.repeatInactiveIv2.visibility = View.VISIBLE
        }
        binding.repeatInactiveIv2.setOnClickListener {
            binding.repeatInactiveIv.visibility = View.VISIBLE
            binding.repeatInactiveIv2.visibility = View.GONE
        }
        binding.randdomInactiveIv.setOnClickListener {
            binding.randdomInactiveIv.visibility = View.GONE
            binding.randdomInactiveIv2.visibility = View.VISIBLE
        }
        binding.randdomInactiveIv2.setOnClickListener {
            binding.randdomInactiveIv.visibility = View.VISIBLE
            binding.randdomInactiveIv2.visibility = View.GONE
        }

    }
    private fun loadSongId(): Int {
        val sharedPreferences = getSharedPreferences("song", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("songId", -1)  // -1은 기본값으로, ID가 없을 경우를 처리
    }
    // 사용자가 포커스를 잃었을 때 음악이 중지
    //json - 자바 객체로 -> json 으로 변환을 간단하게 해준다.
    override fun onPause() {
        super.onPause()
        setplayerstatus(false)
        songs[nowPos].second = ((binding.songprogressSB.progress *songs[nowPos].playTime)/100)/1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)// 앱이 종료되도 데이터를 다시 사용할 수 있다.
        val editor = sharedPreferences.edit() //에디터

        editor.putInt("songId",songs[nowPos].id)

        editor.apply() //꼭 해줘야 한다.
    }
    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() //미디어플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initclicklistener(){
        binding.songDown.setOnClickListener {
            finish()
        }
        binding.songPlayIv.setOnClickListener {
            setplayerstatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            setplayerstatus(false)
        }
        binding.songNext.setOnClickListener{
            moveSong(1)
        }
        binding.songPriveous.setOnClickListener{
            moveSong(-1)
        }
        binding.albumLikeIv.setOnClickListener{

            setlike(songs[nowPos].islike)
        }

    }
    private fun setlike(islike : Boolean){
        songs[nowPos].islike = !islike
        songDB.songDao().updateislikeByid(!islike,songs[nowPos].id)
        if(!islike){
            Toast.makeText(this," 좋아요!!",Toast.LENGTH_SHORT).show()
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }
        else{
            Toast.makeText(this," 싫아요!!",Toast.LENGTH_SHORT).show()
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun initsong(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId",0)
        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID",songs[nowPos].id.toString())
        startTimer()
        if(checking==true){
            startTimer()
        }
        setPlayer(songs[nowPos])
    }
    private fun getPlayingSongPosition(songId : Int) : Int{
        for(i in 0 until songs.size){
            if(songs[i].id ==songId){
                return i
            }
        }
        return 0
    }
    private fun moveSong(direct : Int){
        if(nowPos +direct <0){
            Toast.makeText(this,"first song",Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos +direct>= songs.size){
            Toast.makeText(this,"last song",Toast.LENGTH_SHORT).show()
            return
        }
        nowPos += direct
        timer.interrupt()
        startTimer()
        mediaPlayer?.release() //미디어플레이어가 갖고 있던 리소스 해제 // 미디어 플레이어 재시작
        mediaPlayer = null
        setPlayer(songs[nowPos])
    }
    private fun setPlayer(song : Song){
        binding.songMusicTitleTv.text = song.title
        binding.songMusicSingerTv.text = song.singer
        binding.songStarttimeIv.text = String.format("%02d:%02d",song.second/60,song.second%60)
        binding.songEndtimeIv.text = String.format("%02d:%02d",song.playTime/60,song.playTime%60)
        binding.AlbumIv.setImageResource(song.coverImg!!)
        binding.songprogressSB.progress = ((song.second * 3000.0 / song.playTime).toInt())
        val music = resources.getIdentifier(song.music,"raw",this.packageName)
        mediaPlayer = MediaPlayer.create(this,music)
        if(song.islike){
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }
        else{
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
        setplayerstatus(song.isplaying)
    }
    private fun setplayerstatus(isplaying:Boolean){
        songs[nowPos].isplaying = isplaying
        timer.isplaying = isplaying


        if(isplaying){
            binding.songPlayIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        }
        else{
            binding.songPlayIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if(mediaPlayer?.isPlaying ==true){
                mediaPlayer?.pause()
            }

        }
    }
    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime,songs[nowPos].isplaying)
        timer.start()
    }

    inner class Timer (private val playTime: Int, var isplaying : Boolean = true) : Thread(){
        private var second : Int =0
        private var pills : Float =0f


        override fun run() {
            super.run()
            try {
                while(true){
                    if(second >=playTime){
                       // checking = true
                        startTimer()
                        break
                    }
                    if(isplaying){
                        sleep(50)
                        pills +=50
                        runOnUiThread {
                            binding.songprogressSB.progress = ((pills/ playTime)*100).toInt()
                        }
                        if(pills % 1000 ==0f){
                            runOnUiThread {
                                binding.songStarttimeIv.text = String.format("%02d:%02d",second/60,second%60)
                            }
                            second++
                        }
                    }
                }
            }catch (e : InterruptedException){
                Log.d("Song","쓰레드가 죽었습니다.")
            }

        }
    }


}
