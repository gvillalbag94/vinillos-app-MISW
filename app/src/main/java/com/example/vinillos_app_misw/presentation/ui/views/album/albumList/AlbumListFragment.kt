package com.example.vinillos_app_misw.presentation.ui.views.album.albumList

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vinillos_app_misw.R
import com.example.vinillos_app_misw.data.adapters.AlbumAdapter
import com.example.vinillos_app_misw.data.database.VinilosRoomDatabase
import com.example.vinillos_app_misw.data.model.AlbumWithDetails
import com.example.vinillos_app_misw.data.repositories.AlbumRepository
import com.example.vinillos_app_misw.databinding.FragmentAlbumListBinding
import com.example.vinillos_app_misw.presentation.ui.views.album.albumDetail.AlbumDetailActivity
import com.example.vinillos_app_misw.presentation.view_model.album.AlbumViewModel
import com.example.vinillos_app_misw.presentation.view_model.album.AlbumViewModelFactory
import com.google.android.material.snackbar.Snackbar


class AlbumListFragment : Fragment(), AlbumListAdapter.OnAlbumClickListener  {

    private lateinit var albumViewModel: AlbumViewModel

    private lateinit var albumAdapter: AlbumListAdapter

    private var _binding: FragmentAlbumListBinding? = null

    private val binding get() = _binding!!

    private var onRender = 0

    var fragment: Fragment = this


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumListBinding.inflate(inflater, container, false)
        val database = VinilosRoomDatabase.getDatabase(requireContext().applicationContext)
        val albumAdapter = AlbumAdapter(requireContext())
        val albumDao = database.albumDao()
        val repository = AlbumRepository(requireContext(),albumAdapter, albumDao)
        val factory = AlbumViewModelFactory(repository)
        albumViewModel = ViewModelProvider(this, factory)[AlbumViewModel::class.java]
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        albumViewModel.albums.observe(viewLifecycleOwner) { albums ->
            albumAdapter = AlbumListAdapter(albums, this)
            binding.recyclerView.adapter = albumAdapter
        }

        albumViewModel.getAlbums()
    }

    override fun onAlbumClick(album: AlbumWithDetails) {
        albumViewModel.saveAlbumID(album.album.id)
        val intent = Intent(requireContext(), AlbumDetailActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        if(albumViewModel.getUpdateValue()) {
            showSnackbarSuccess("El álbum se creo correctamente.")
            albumViewModel.fetchAlbums()
            albumViewModel.saveUpdateValue(false)
        }

    }


    private fun showSnackbarSuccess(message: String) {
        val rootLayout = requireActivity().window.decorView.findViewById<View>(android.R.id.content)
        Snackbar.make(rootLayout, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint( ContextCompat.getColor(requireContext(), R.color.success))
            .setTextColor(Color.WHITE)
            .show()
    }
}