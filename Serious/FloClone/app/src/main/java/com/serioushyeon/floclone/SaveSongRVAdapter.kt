package com.serioushyeon.floclone

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.serioushyeon.floclone.databinding.ItemSavedsongBinding

class SaveSongRVAdapter(private val albumList: ArrayList<SaveSong>) : RecyclerView.Adapter<SaveSongRVAdapter.ViewHolder>() {
    interface MyItemClickListener {
        fun onRemoveMusicFile(position: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    fun removeItem(position: Int) {
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SaveSongRVAdapter.ViewHolder {
        val binding: ItemSavedsongBinding = ItemSavedsongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SaveSongRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.binding.saveSongMore.setOnClickListener { mItemClickListener.onRemoveMusicFile(position) }
        holder.binding.saveSongSwitch.setOnCheckedChangeListener(null)
        holder.binding.saveSongSwitch.isChecked = albumList[position].isChecked
        holder.binding.saveSongSwitch.setOnCheckedChangeListener { _, isChecked ->
            albumList[position].isChecked = isChecked
        }
    }

    override fun getItemCount(): Int = albumList.size

    // 뷰홀더
    inner class ViewHolder(val binding: ItemSavedsongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: SaveSong) {
            binding.saveSongTitle.text = album.title
            binding.saveSongSinger.text = album.singer
            binding.saveSongImg.setImageResource(album.coverImg!!)
        }
    }
}
