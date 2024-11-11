package com.example.vinillos_app_misw.presentation.ui.views.artist.artistList

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.vinillos_app_misw.data.model.Performer
import com.example.vinillos_app_misw.databinding.ArtistCardBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class ArtistListAdapter(private val artists: List<Performer>, private val listener: OnArtistClickListener)
    : RecyclerView.Adapter<ArtistListAdapter.ArtistViewHolder>() {

    interface OnArtistClickListener {
        fun onArtistClick(artist: Performer)
    }

    inner class ArtistViewHolder(private val binding: ArtistCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(artist: Performer) {
            binding.artist = artist
            loadImageFromUrl(
                binding.albumImage,
                artist.image
            )

             // Set up click listener
            binding.albumButton.setOnClickListener {
                listener.onArtistClick(artist)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding = ArtistCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(artists[position])
    }

    override fun getItemCount(): Int = artists.size

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