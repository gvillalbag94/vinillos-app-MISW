package com.example.vinillos_app_misw.presentation.ui.views.album.albumList

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vinillos_app_misw.data.adapters.AlbumAdapter
import com.example.vinillos_app_misw.data.model.Album
import com.example.vinillos_app_misw.data.repositories.AlbumRepository
import com.example.vinillos_app_misw.databinding.FragmentAlbumListBinding
import com.example.vinillos_app_misw.presentation.ui.views.album.albumDetail.AlbumDetailActivity
import com.example.vinillos_app_misw.presentation.view_model.album.AlbumViewModel
import com.example.vinillos_app_misw.presentation.view_model.album.AlbumViewModelFactory


class AlbumListFragment : Fragment(), AlbumListAdapter.OnAlbumClickListener  {

    private lateinit var albumViewModel: AlbumViewModel

    private lateinit var albumAdapter: AlbumListAdapter

    private var _binding: FragmentAlbumListBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumListBinding.inflate(inflater, container, false)
        val albumAdapter = AlbumAdapter(requireContext())
        val repository = AlbumRepository(requireContext(),albumAdapter)
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

    override fun onAlbumClick(album: Album) {
        albumViewModel.saveAlbumID(album.id)
        val intent = Intent(requireContext(), AlbumDetailActivity::class.java)
        startActivity(intent)
    }
}