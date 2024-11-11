package com.example.vinillos_app_misw.presentation.ui.views.artist.artistList

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vinillos_app_misw.data.adapters.ArtistAdapter
import com.example.vinillos_app_misw.data.model.Performer
import com.example.vinillos_app_misw.data.repositories.ArtistRepository
import com.example.vinillos_app_misw.databinding.FragmentArtistListBinding
import com.example.vinillos_app_misw.presentation.ui.views.artist.artistDetail.ArtistDetailActivity
import com.example.vinillos_app_misw.presentation.view_model.artist.ArtistViewModel
import com.example.vinillos_app_misw.presentation.view_model.artist.ArtistViewModelFactory


class ArtistListFragment : Fragment(), ArtistListAdapter.OnArtistClickListener {

    private lateinit var artistViewModel: ArtistViewModel

    private lateinit var artistAdapter: ArtistListAdapter

    private var _binding: FragmentArtistListBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArtistListBinding.inflate(inflater, container, false)
        val artistAdapter = ArtistAdapter(requireContext())
        val repository = ArtistRepository(requireContext(),artistAdapter)
        val factory = ArtistViewModelFactory(repository)
        artistViewModel = ViewModelProvider(this, factory)[ArtistViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.artistsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        artistViewModel.artists.observe(viewLifecycleOwner) { artists ->
            artistAdapter = ArtistListAdapter(artists, this)
            binding.artistsRecyclerView.adapter = artistAdapter
        }
        artistViewModel.getArtists()
    }

    override fun onArtistClick(artist: Performer) {
        artistViewModel.saveArtistID(artist.id)
        val intent = Intent(requireContext(), ArtistDetailActivity::class.java)
        startActivity(intent)
    }
}