package com.example.practice.ui.song

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practice.R
import com.example.practice.data.entity.Album
import com.example.practice.databinding.ItemLockerBinding

class SongRVAdapter(private var albumList: ArrayList<Album>) :
    RecyclerView.Adapter<SongRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(item : Album)
        fun onRemoveAlbum(position: Int)
        fun onToggle(position: Int)
    }
    private lateinit var mItemClickListener : MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    fun addItem(album: Album){
        albumList.add(album)
        notifyDataSetChanged()
    }

    fun setData(list : ArrayList<Album>){
        albumList.clear()
        albumList.addAll(list)
        notifyDataSetChanged()
    }


    fun toggleItem(position: Int){
        albumList[position].state = albumList[position].state == false
        notifyDataSetChanged()
    }
    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemLockerBinding =
            ItemLockerBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])
//        holder.itemView.setOnClickListener {
//            mItemClickListener.onItemClick(albumList[position])
//        }
        holder.binding.lockerToggle.setOnClickListener { mItemClickListener.onToggle(position) }
        holder.binding.lockerMenu.setOnClickListener { mItemClickListener.onRemoveAlbum(position) }
    }

    override fun getItemCount() = albumList.size



    inner class ViewHolder(val binding: ItemLockerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.ivLocker.setImageResource(album.coverImg!!)
            binding.tvLockerSinger.text = album.singer.toString()
            binding.tvLockerTitle.text = album.title.toString()
            if(album.state==true){
                binding.lockerToggle.setImageResource(R.drawable.btn_toggle_on)
            }
            else
            {
                binding.lockerToggle.setImageResource(R.drawable.btn_toggle_off)
            }
        }
    }

}