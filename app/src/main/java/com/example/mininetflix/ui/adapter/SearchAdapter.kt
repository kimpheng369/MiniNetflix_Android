package com.example.mininetflix.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.mininetflix.R
import com.example.mininetflix.data.model.Movie
import com.example.mininetflix.databinding.ItemMovieSearchBinding
import com.example.mininetflix.util.Constants

class SearchAdapter(private val onMovieClick: (Movie) -> Unit) :
    ListAdapter<Movie, SearchAdapter.SearchViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemMovieSearchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SearchViewHolder(private val binding: ItemMovieSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.tvSearchTitle.text = movie.displayTitle

            val year = if (movie.displayDate.length >= 4) movie.displayDate.substring(0, 4) else "N/A"
            val ratingText = String.format("%.1f", movie.voteAverage ?: 0.0)
            binding.tvSearchInfo.text = "$year • Rating: $ratingText"

            val posterUrl = Constants.IMAGE_BASE_URL + movie.posterPath

            Glide.with(binding.ivSearchPoster.context)
                .load(posterUrl)
                .placeholder(R.color.background_black)
                .error(R.color.background_black)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.ivSearchPoster)

            binding.root.setOnClickListener {
                onMovieClick(movie)
            }

            binding.ivPlayIndicator.setOnClickListener {
                onMovieClick(movie)
            }
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}
