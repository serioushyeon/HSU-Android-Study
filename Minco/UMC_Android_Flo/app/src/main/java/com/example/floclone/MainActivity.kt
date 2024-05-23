package com.example.floclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
//뷰바인딩을 위해 임포트 함수를 사용하기 위해?
import com.example.floclone.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson


class MainActivity : AppCompatActivity(){
    //lateinit은 나중에 초기화 될것을 나타냄
    // ActivityMainBinding은 레이아웃 파일이름에 Binding을 추가한이름
    lateinit var binding : ActivityMainBinding // 뷰 바인딩 함수
    //전역변수
    private var song:Song = Song()
    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //앱이 로드되는 동안에는 splash화면이 나오지만 -> onCreate가 호출되면 >>
        setTheme(R.style.Theme_FloClone) // 원래 처음 실행 화면으로 돌려줌
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(binding.root)

        //이제 필요없음 -> sharedPreferences로 값을 가져오기 때문
        //val song = Song(binding.mainPlayTitleTv.text.toString(),binding.mainPlaySingerTv.text.toString(),0,60,false,"music_tomorow")

        binding.mainPlayBar.setOnClickListener {
            //어디로 갈지 설정
            //mainPlayBar를 눌렀을 때 SongActivity로 전환
            //startActivity(Intent(this,SongActivity::class.java))
            //Song인스턴스의 title과 singer을 넣기위한 방법 putExtrax
            val intent = Intent(this,SongActivity::class.java)
            intent.putExtra("title",song.title)
            intent.putExtra("singer",song.singer)
            intent.putExtra("second",song.second)
            intent.putExtra("playTime",song.playTime)
            intent.putExtra("isPlaying",song.isPlaying)
            intent.putExtra("music",song.music)

            startActivity(intent)
        }

        initBottomNavigation()

        Log.d("Song",song.title+song.singer)
    }

    private fun initBottomNavigation() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }


            }
            false
        }
    }

    private fun setMiniPlayer(song: Song){
//        binding.mainPlayTitleTv.text = song.title
//        binding.mainPlaySingerTv.text = song.singer
        val title = intent.getStringExtra("album_title") //[오늘 발매음악]에서 재생 버튼 클릭시 miniplayer에 제목, 가수명 변경
        val singer = intent.getStringExtra("album_singer")

        binding.mainPlayTitleTv.text = title ?: song.title
        binding.mainPlaySingerTv.text = singer ?: song.singer
        binding.mainMiniplayerProgressSb.progress = (song.second*100000)/song.playTime
    }

    override fun onStart(){
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("songData",null)
        //val albumJson = getSharedPreferences("album", MODE_PRIVATE) //이건 아닌 것 같음, 데이터 받아오는 거


        song = if(songJson == null){
            Song("내일의 우리","카더가든",0,60,false,"music_tomorow")
        }else{
            gson.fromJson(songJson,Song::class.java)
        }
        setMiniPlayer(song)

    }


}