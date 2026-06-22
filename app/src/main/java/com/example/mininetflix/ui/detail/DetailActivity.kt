package com.example.mininetflix.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.mininetflix.R
import com.example.mininetflix.data.model.Movie
import com.example.mininetflix.databinding.ActivityDetailBinding
import com.example.mininetflix.ui.adapter.MovieAdapter
import com.example.mininetflix.ui.viewmodel.ViewModelFactory
import com.example.mininetflix.util.Constants

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var similarAdapter: MovieAdapter
    private var trailerKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movie = intent.getSerializableExtra("EXTRA_MOVIE") as? Movie
        if (movie == null) {
            Toast.makeText(this, "Movie data not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        setupUI(movie)
        setupRecyclerView()
        observeViewModel(movie)

        viewModel.checkFavoriteStatus(movie.id)
        viewModel.fetchDetails(movie.id)
    }

    private fun setupUI(movie: Movie) {
        binding.detailTitle.text = movie.displayTitle
        binding.detailReleaseDate.text = getString(R.string.release_date_label, movie.displayDate)
        binding.detailRating.text = getString(R.string.rating_label, movie.voteAverage ?: 0.0)
        binding.detailOverview.text = movie.overview ?: "No overview available."

        val backdropUrl = Constants.BACKDROP_BASE_URL + movie.backdropPath
        Glide.with(this)
            .load(backdropUrl)
            .placeholder(R.color.background_black)
            .error(R.color.background_black)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.detailBackdrop)

        val posterUrl = Constants.IMAGE_BASE_URL + movie.posterPath
        Glide.with(this)
            .load(posterUrl)
            .placeholder(R.color.card_background)
            .error(R.color.card_background)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.detailPoster)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnWatchTrailer.setOnClickListener {
            playTrailer()
        }

        binding.btnMylist.setOnClickListener {
            viewModel.toggleFavorite(movie)
        }
    }

    private fun setupRecyclerView() {
        similarAdapter = MovieAdapter { similarMovie ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("EXTRA_MOVIE", similarMovie)
            }
            startActivity(intent)
        }
        binding.rvSimilar.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = similarAdapter
        }
    }

    private fun observeViewModel(movie: Movie) {
        viewModel.isFavorite.observe(this) { isFav ->
            if (isFav) {
                binding.btnMylist.text = getString(R.string.remove_from_list)
                binding.btnMylist.setIconResource(android.R.drawable.checkbox_on_background)
            } else {
                binding.btnMylist.text = getString(R.string.add_to_list)
                binding.btnMylist.setIconResource(R.drawable.ic_my_list)
            }
        }

        viewModel.similarMovies.observe(this) { similar ->
            similarAdapter.submitList(similar)
            if (similar.isNullOrEmpty()) {
                binding.tvSimilarLabel.visibility = View.GONE
                binding.rvSimilar.visibility = View.GONE
            } else {
                binding.tvSimilarLabel.visibility = View.VISIBLE
                binding.rvSimilar.visibility = View.VISIBLE
            }
        }

        viewModel.trailerKey.observe(this) { key ->
            trailerKey = key
            if (key.isNullOrEmpty()) {
                binding.btnWatchTrailer.alpha = 0.5f
            } else {
                binding.btnWatchTrailer.alpha = 1.0f
            }
        }

        viewModel.errorMessage.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun playTrailer() {
        if (!trailerKey.isNullOrEmpty()) {
            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$trailerKey"))
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$trailerKey"))
            try {
                startActivity(appIntent)
            } catch (ex: Exception) {
                startActivity(webIntent)
            }
        } else {
            val movieTitle = binding.detailTitle.text.toString()
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=$movieTitle+trailer"))
            startActivity(webIntent)
        }
    }
}
