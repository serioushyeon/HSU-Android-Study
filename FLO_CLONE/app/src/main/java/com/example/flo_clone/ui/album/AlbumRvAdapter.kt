package com.example.flo_clone.ui.album

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo_clone.MainActivity
import com.example.flo_clone.data.Album
import com.example.flo_clone.databinding.ItemAlbumBinding
class AlbumRvAdapter (val albumList: ArrayList<Album>) :
    RecyclerView.Adapter<AlbumRvAdapter.AlbumViewHolder>() {

    private lateinit var mOnItemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        mOnItemClickListener = onItemClickListener
    }

    interface OnItemClickListener{
        fun onItemClick(album: Album)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AlbumViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return albumList.count()
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener {
            mOnItemClickListener.onItemClick(albumList[position])
        }
    }

    inner class AlbumViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            sendToMiniPlayer()
        }

        // 플레이 버튼 클릭하면 메인 액티비티로 제목, 가수명 전달 (인텐트 사용)
        private fun sendToMiniPlayer() {
            binding.itemAlbumPlayImgIv.setOnClickListener {
                val intent = Intent(binding.root.context, MainActivity::class.java).apply {
                    putExtra("album_title", albumList[adapterPosition].title)
                    putExtra("album_singer", albumList[adapterPosition].singer)
                    // 다른 필요한 정보들도 필요한 경우 추가
                }
                binding.root.context.startActivity(intent)
            }
        }

        fun bind(album: Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.converImg!!)
        }
    }
}