package com.example.vinillos_app_misw.presentation.ui.views.album.albumList

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.vinillos_app_misw.data.model.AlbumWithDetails
import com.example.vinillos_app_misw.databinding.AlbumCardBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class AlbumListAdapter(private val albums: List<AlbumWithDetails>, private val listener: OnAlbumClickListener
) : RecyclerView.Adapter<AlbumListAdapter.AlbumViewHolder>() {

    interface OnAlbumClickListener {
        fun onAlbumClick(album: AlbumWithDetails)
    }

    inner class AlbumViewHolder(private val binding: AlbumCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: AlbumWithDetails) {
            binding.album = album
            loadImageFromUrl(
                binding.albumImage,
                album.album.cover
            )

            // Set up click listener
            binding.albumButton.setOnClickListener {
                listener.onAlbumClick(album)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = AlbumCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albums[position])
    }

    override fun getItemCount(): Int = albums.size

    fun loadImageFromUrl(imageView: ImageView, url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input: InputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(input)
                withContext(Dispatchers.Main) {
                    imageView.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Opcional: establece una imagen de error en caso de fallo

            }
        }
    }
}
