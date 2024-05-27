package com.example.practice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practice.databinding.ItemSavemusicalbumBinding

class SavemusicRvadapter(private val albumList: ArrayList<Album>,private val listener: MyItemClickListener)
    : RecyclerView.Adapter<SavemusicRvadapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onitemClick(album: Album)
        fun onRemoveAlbum(position: Int)
    }

    fun addItem(album: Album) {
        albumList.add(album)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSavemusicalbumBinding = ItemSavemusicalbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])
        //val item = items[position]  // 일단 이 문장이 문제가 있는데...!!
        //holder.binding.albumswitch.isChecked = item.ischecked
        /*holder.itemView.setOnClickListener {
            listener.onitemClick(albumList[position])  // listener를 직접 사용
        }*/
    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemSavemusicalbumBinding, clicklistener: MyItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.savemusicMore.setOnClickListener {
                clicklistener.onRemoveAlbum(adapterPosition)
            }
        }
        fun bind(album: Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.covering!!)
        }
    }
}
