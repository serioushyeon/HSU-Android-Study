package com.example.flo_clone.ui.locker.savedsongs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_clone.R
import com.example.flo_clone.data.SavedSongs
import com.example.flo_clone.databinding.FragmentSavedSongsBinding

class SavedSongsFragment : Fragment() {

    lateinit var binding: FragmentSavedSongsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedSongsBinding.inflate(layoutInflater)
        setRecyclerView()
        return binding.root
    }

    private fun setRecyclerView() {

        // 리사이클러뷰 생성 및 바인딩
        val savedSongsPlayListRv = binding.savedSongsPlayListRv

        // 리사이클러뷰에 넣을 데이터 리스트
        val itemList = ArrayList<SavedSongs>()
        itemList.add(SavedSongs(R.drawable.img_album_exp2,"하루끝","아이유"))
        itemList.add(SavedSongs(R.drawable.img_album_exp2,"금요일에 만나요","아이유"))
        itemList.add(SavedSongs(R.drawable.img_album_exp2,"좋은날","아이유"))
        itemList.add(SavedSongs(R.drawable.img_album_exp2,"너랑나","아이유"))
        itemList.add(SavedSongs(R.drawable.img_album_exp2,"선물","멜로망스"))
        itemList.add(SavedSongs(R.drawable.img_album_exp2,"Bad Boy","레드벨벳"))
        itemList.add(SavedSongs(R.drawable.img_album_exp2,"빨간맛","레드벨벳"))
        itemList.add(SavedSongs(R.drawable.img_album_exp2,"Psycho","레드벨벳"))
        itemList.add(SavedSongs(R.drawable.img_album_exp2,"Rookie","레드벨벳"))

        // 어댑터 생성 (데이터 넣어줌)
        val savedSongAdapter = SavedSongAdapter(itemList)
        savedSongAdapter.notifyDataSetChanged()

        savedSongAdapter.setOnItemClickListener(object : SavedSongAdapter.OnItemClickListener {
            override fun onItemClick() {
                // 아이템 클릭 시 수행할 동작
            }
        })

        // 리사이클러뷰와 어댑터 연결
        savedSongsPlayListRv.adapter = savedSongAdapter
        savedSongsPlayListRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}