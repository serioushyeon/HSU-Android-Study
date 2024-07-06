package com.example.floClone.core.ui.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.floClone.databinding.ItemAlbumBinding
import com.example.floClone.core.data.model.local.entities.AlbumEntity

class AlbumRvAdapter (val albumList: ArrayList<AlbumEntity>) :
    RecyclerView.Adapter<AlbumRvAdapter.AlbumViewHolder>() {

    private lateinit var mOnItemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        mOnItemClickListener = onItemClickListener
    }

    interface OnItemClickListener{
        fun onItemClick(album: AlbumEntity)
        fun onPlayBtnClick(item: AlbumEntity)
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
        fun bind(albumEntity: AlbumEntity) {
            binding.itemAlbumTitleTv.text = albumEntity.title
            binding.itemAlbumSingerTv.text = albumEntity.singer
            binding.itemAlbumCoverImgIv.setImageResource(albumEntity.coverImg!!)
        }
    }
}