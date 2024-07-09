package com.serioushyeon.floclone.ui.main.locker.musicfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.serioushyeon.floclone.data.entities.MusicFile
import com.serioushyeon.floclone.databinding.ItemMusicfileBinding

class MusicFileRVAdapter (private val albumList: ArrayList<MusicFile>) : RecyclerView.Adapter<MusicFileRVAdapter.ViewHolder>(){

    // 클릭 인터페이스 정의
    interface MyItemClickListener{
        //fun onItemClick(album: MusicFile)
        fun onRemoveMusicFile(position: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemMusicfileBinding = ItemMusicfileBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    fun addItem(album: MusicFile){
        albumList.add(album)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    // 뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])
//        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(albumList[position]) }
        holder.binding.musicfileMore.setOnClickListener { mItemClickListener.onRemoveMusicFile(position) } //삭제됐을 때
    }

    // 데이터 세트 크기를 알려주는 함수 => 리사이클러뷰가 마지막이 언제인지를 알게 된다.
    override fun getItemCount(): Int = albumList.size

    // 뷰홀더
    inner class ViewHolder(val binding: ItemMusicfileBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(album: MusicFile){
            binding.musicfileTitle.text = album.title
            binding.musicfileSinger.text = album.singer
            binding.musicfileImg.setImageResource(album.coverImg!!)
        }
    }
}