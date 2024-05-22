package com.example.flo_clone.ui.locker.savedsongs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo_clone.data.SavedSongs
import com.example.flo_clone.databinding.ItemSavedSongBinding

class SavedSongAdapter(val itemList: ArrayList<SavedSongs>) :
    RecyclerView.Adapter<SavedSongAdapter.SaveSongsViewHolder>() {

    private lateinit var mOnItemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        mOnItemClickListener = onItemClickListener
    }

    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveSongsViewHolder {
        val binding = ItemSavedSongBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SaveSongsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    override fun onBindViewHolder(holder: SaveSongsViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class SaveSongsViewHolder(val binding: ItemSavedSongBinding) : RecyclerView.ViewHolder(binding.root) {
        var img = binding.itemAlbumCoverImgIv
        var title = binding.itemAlbumTitleTv
        var singer = binding.itemAlbumSingerTv

        init {
            binding.root.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION && mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(binding.root, pos)
                }
            }
            deleteItem()
        }

        fun deleteItem() {
            val deleteBtn = binding.itemOptionImgIv
            deleteBtn.setOnClickListener{
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    itemList.removeAt(pos)
                    notifyItemRemoved(pos)
                }
            }
        }

        fun bind(savedSong: SavedSongs) {
            // 이미지 로딩 라이브러리를 사용하여 이미지 설정
            Glide.with(itemView.context)
                .load(savedSong.img)
                .into(img)

            title.text = savedSong.title
            singer.text = savedSong.singer
        }
    }
}