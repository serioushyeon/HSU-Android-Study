package com.example.flo_clone.ui.locker.savedsongs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo_clone.data.SavedSongs
import com.example.flo_clone.databinding.ItemSavedSongBinding
import com.example.flo_clone.room.SongEntity

class SavedSongAdapter() :
    RecyclerView.Adapter<SavedSongAdapter.SaveSongsViewHolder>() {

    private val songs = ArrayList<SongEntity>()

    private lateinit var mOnItemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        mOnItemClickListener = onItemClickListener
    }

    interface OnItemClickListener{
        fun onItemClick()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveSongsViewHolder {
        val binding = ItemSavedSongBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SaveSongsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return songs.count()
    }

    override fun onBindViewHolder(holder: SaveSongsViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<SongEntity>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeSong(position: Int){
        songs.removeAt(position)
        notifyDataSetChanged()
    }


    inner class SaveSongsViewHolder(val binding: ItemSavedSongBinding) : RecyclerView.ViewHolder(binding.root) {
        var img = binding.itemAlbumCoverImgIv
        var title = binding.itemAlbumTitleTv
        var singer = binding.itemAlbumSingerTv

        init {
            binding.root.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION && mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick()
                }
            }
            deleteItem()
        }

        fun deleteItem() {
            val deleteBtn = binding.itemOptionImgIv
            deleteBtn.setOnClickListener{
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    songs.removeAt(pos)
                    notifyItemRemoved(pos)
                }
            }
        }

        fun bind(songEntity: SongEntity) {
            // 이미지 로딩 라이브러리를 사용하여 이미지 설정
            Glide.with(itemView.context)
                .load(songEntity.coverImg)
                .into(img)

            title.text = songEntity.title
            singer.text = songEntity.singer
        }
    }
}