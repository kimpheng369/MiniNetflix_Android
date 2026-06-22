package com.example.mininetflix.ui.mylist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mininetflix.data.db.FavoriteMovie
import com.example.mininetflix.data.model.Movie
import com.example.mininetflix.databinding.FragmentMyListBinding
import com.example.mininetflix.ui.adapter.MovieAdapter
import com.example.mininetflix.ui.detail.DetailActivity
import com.example.mininetflix.ui.viewmodel.ViewModelFactory

class MyListFragment : Fragment() {

    private var _binding: FragmentMyListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MyListViewModel
    private lateinit var listAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[MyListViewModel::class.java]

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        listAdapter = MovieAdapter { movie ->
            val intent = Intent(activity, DetailActivity::class.java).apply {
                putExtra("EXTRA_MOVIE", movie)
            }
            startActivity(intent)
        }
        binding.rvMylist.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = listAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.favoriteMovies.observe(viewLifecycleOwner) { favorites ->
            if (favorites.isNullOrEmpty()) {
                binding.tvMylistEmpty.visibility = View.VISIBLE
                listAdapter.submitList(emptyList())
            } else {
                binding.tvMylistEmpty.visibility = View.GONE
                // Map FavoriteMovie list back to Movie list for MovieAdapter
                val movies = favorites.map { fav ->
                    Movie(
                        id = fav.id,
                        title = fav.title,
                        name = fav.name,
                        overview = fav.overview,
                        posterPath = fav.posterPath,
                        backdropPath = fav.backdropPath,
                        releaseDate = fav.releaseDate,
                        firstAirDate = fav.firstAirDate,
                        voteAverage = fav.voteAverage,
                        mediaType = null
                    )
                }
                listAdapter.submitList(movies)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
