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
        fun onPlayBtnClick(item: Album)
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
        holder.binding.itemAlbumPlayImgIv.setOnClickListener {
            mOnItemClickListener.onPlayBtnClick(albumList[position])
        }
    }

    inner class AlbumViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.converImg!!)
        }
    }
}