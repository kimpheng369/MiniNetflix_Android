package com.example.mininetflix.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.mininetflix.R
import com.example.mininetflix.data.model.Movie
import com.example.mininetflix.databinding.FragmentHomeBinding
import com.example.mininetflix.ui.adapter.MovieAdapter
import com.example.mininetflix.ui.detail.DetailActivity
import com.example.mininetflix.ui.viewmodel.ViewModelFactory
import com.example.mininetflix.util.Constants

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    private lateinit var trendingAdapter: MovieAdapter
    private lateinit var popularAdapter: MovieAdapter
    private lateinit var topRatedAdapter: MovieAdapter
    private lateinit var upcomingAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        setupRecyclerViews()
        observeViewModel()

        viewModel.fetchHomeData()
    }

    private fun setupRecyclerViews() {
        trendingAdapter = MovieAdapter { movie -> navigateToDetail(movie) }
        popularAdapter = MovieAdapter { movie -> navigateToDetail(movie) }
        topRatedAdapter = MovieAdapter { movie -> navigateToDetail(movie) }
        upcomingAdapter = MovieAdapter { movie -> navigateToDetail(movie) }

        binding.rvTrending.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = trendingAdapter
        }

        binding.rvPopular.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularAdapter
        }

        binding.rvTopRated.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = topRatedAdapter
        }

        binding.rvUpcoming.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = upcomingAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.trendingMovies.observe(viewLifecycleOwner) { movies ->
            trendingAdapter.submitList(movies)
        }

        viewModel.popularMovies.observe(viewLifecycleOwner) { movies ->
            popularAdapter.submitList(movies)
        }

        viewModel.topRatedMovies.observe(viewLifecycleOwner) { movies ->
            topRatedAdapter.submitList(movies)
        }

        viewModel.upcomingMovies.observe(viewLifecycleOwner) { movies ->
            upcomingAdapter.submitList(movies)
        }

        viewModel.heroMovie.observe(viewLifecycleOwner) { movie ->
            bindHeroMovie(movie)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun bindHeroMovie(movie: Movie) {
        binding.heroTitle.text = movie.displayTitle
        binding.heroGenres.text = "Rating: ${movie.voteAverage ?: 0.0} • Released: ${movie.displayDate}"

        val backdropUrl = Constants.BACKDROP_BASE_URL + movie.backdropPath
        Glide.with(this)
            .load(backdropUrl)
            .placeholder(R.color.card_background)
            .error(R.color.card_background)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.heroImage)

        binding.heroPlayButton.setOnClickListener {
            navigateToDetail(movie)
        }

        binding.heroInfoButton.setOnClickListener {
            navigateToDetail(movie)
        }
    }

    private fun navigateToDetail(movie: Movie) {
        val intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra("EXTRA_MOVIE", movie)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
