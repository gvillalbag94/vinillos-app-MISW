package com.example.vinillos_app_misw.presentation.ui.views.collector.collectorDetail

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vinillos_app_misw.R
import com.example.vinillos_app_misw.data.model.Album
import com.example.vinillos_app_misw.data.model.AlbumWithDetails
import com.example.vinillos_app_misw.data.model.CollectorAlbum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class CollectorAlbumAdapter(private val albumList: List<CollectorAlbum>, private val albums: List<AlbumWithDetails>) : RecyclerView.Adapter<CollectorAlbumAdapter.CollectorAlbumViewHolder>() {

    class CollectorAlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val albumTitleTextView: TextView = itemView.findViewById(R.id.albumTitle)
        val albumGenreTextView: TextView = itemView.findViewById(R.id.albumGenre)
        val albumPriceTextView: TextView = itemView.findViewById(R.id.albumPrice)
        val albumStateTextView: TextView = itemView.findViewById(R.id.albumState)
        val albumImageView: ImageView = itemView.findViewById(R.id.albumImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorAlbumViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.collector_album_card, parent, false)
        return CollectorAlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: CollectorAlbumViewHolder, position: Int) {
        val collectorAlbum = albumList[position]
        val album: AlbumWithDetails = albums.find { it.album.id == collectorAlbum.id } ?: albums.first()

        holder.albumTitleTextView.text = album.album.name
        holder.albumGenreTextView.text = album.album.genre
        holder.albumPriceTextView.text = String.format(collectorAlbum.price.toString())
        holder.albumStateTextView.text = collectorAlbum.status
        loadImageFromUrl(holder.albumImageView,album.album.cover)
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

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