package com.example.floclone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.floclone.databinding.ItemSongBinding
import java.util.ArrayList

class SavedSongRVAdapter(private val savedSongList: ArrayList<SavedSong>): RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {

    interface MyItemClickLitener{
        fun onItemClick()
        fun onRemoveSavedSong(position: Int)

    }

    private lateinit var  mItemClickListener: MyItemClickLitener
    fun setMyItemClickListener(itemClickLitener: MyItemClickLitener){
        mItemClickListener = itemClickLitener
    }

    fun addItem(savedSong: SavedSong){
        savedSongList.add(savedSong)
        notifyDataSetChanged()
    }
    fun removeItem(position: Int){
        savedSongList.removeAt(position)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SavedSongRVAdapter.ViewHolder {
        val binding: ItemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedSongRVAdapter.ViewHolder, position: Int) {
        holder.bind(savedSongList[position])

        holder.binding.itemSongMoreIv.setOnClickListener {
            mItemClickListener.onRemoveSavedSong(position)
        }

        // savedSongList[position]의 isToggled 상태에 따라 뷰의 초기 상태를 설정합니다
        holder.binding.itmeSongMixonTg.visibility = if (savedSongList[position].isToggled) View.VISIBLE else View.GONE
        holder.binding.itmeSongMixoffTg.visibility = if (savedSongList[position].isToggled) View.GONE else View.VISIBLE

        holder.binding.itmeSongMixonTg.apply {
            isClickable = true
            isFocusable = true
            setOnClickListener {
                val newState = visibility != View.VISIBLE
                visibility = if (newState) View.VISIBLE else View.GONE
                holder.binding.itmeSongMixoffTg.visibility = if (newState) View.GONE else View.VISIBLE
                savedSongList[position].isToggled = newState // 토글 상태를 저장합니다
            }
        }

        holder.binding.itmeSongMixoffTg.apply {
            isClickable = true
            isFocusable = true
            setOnClickListener {
                val newState = visibility != View.VISIBLE
                visibility = if (newState) View.VISIBLE else View.GONE
                holder.binding.itmeSongMixonTg.visibility = if (newState) View.GONE else View.VISIBLE
                savedSongList[position].isToggled = !newState // 토글 상태를 저장합니다
            }
        }
    }


    override fun getItemCount(): Int = savedSongList.size

    inner class ViewHolder(val binding: ItemSongBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(savedSong: SavedSong){
            binding.itemSongTitleTv.text = savedSong.title
            binding.itemSongSingerTv.text = savedSong.singer
            binding.itemSongImgIv.setImageResource(savedSong.coverImg!!)



        }
    }

}

