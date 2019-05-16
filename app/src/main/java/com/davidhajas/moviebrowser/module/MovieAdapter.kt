package com.davidhajas.moviebrowser.module

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.davidhajas.moviebrowser.R
import com.davidhajas.moviebrowser.datasource.movie.entity.Movie

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.Holder>() {

    interface MovieClickListener {
        fun onMovieClicked(movie: Movie)
        fun onFavoriteClicked(movie: Movie)
    }

    var movieClickListener: MovieClickListener? = null

    private val movies = arrayListOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_movie, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = movies.count()

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val movie = movies[position]
        holder.view.setOnClickListener { movieClickListener?.onMovieClicked(movie) }
        holder.tvTitle.text = movie.title
        holder.btnFavorite.setImageResource(
            if (movie.favorite) {
                R.drawable.ic_favorite
            } else {
                R.drawable.ic_favorite_add
            }
        )
        holder.btnFavorite.setOnClickListener { movieClickListener?.onFavoriteClicked(movie) }
        Glide.with(holder.view).load(movie.coverURL).into(holder.imgCover)
    }

    fun refreshList(list: List<Movie>) {
        movies.apply {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    fun updateItem(movie: Movie, favorite: Boolean) {
        movie.favorite = favorite
        notifyDataSetChanged()
    }

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val btnFavorite: ImageView = view.findViewById(R.id.btnFavorite)
        val imgCover: ImageView = view.findViewById(R.id.imgCover)
    }
}