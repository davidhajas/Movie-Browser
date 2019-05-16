package com.davidhajas.moviebrowser.module.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.davidhajas.moviebrowser.R
import com.davidhajas.moviebrowser.datasource.movie.entity.Movie
import com.davidhajas.moviebrowser.module.MovieAdapter
import com.davidhajas.moviebrowser.plugin.threading.DefaultDispatcherProvider
import com.davidhajas.moviebrowser.util.Injector
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesFragment : Fragment(), FavoritesView {

    private val adapter = MovieAdapter()

    private val presenter: FavoritesPresenter by lazy {
        val movieRemoteRepository = Injector.injectMovieRemoteRepository()
        val context = context!!
        val movieLocalRepository = Injector.injectMovieLocalRepository(context)
        val movieInteractor = Injector.injectMovieInteractor(movieRemoteRepository, movieLocalRepository)
        FavoritesPresenter(this, context, lifecycle, DefaultDispatcherProvider(), movieInteractor)
    }

    // region Fragment callbacks

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_favorites, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        adapter.movieClickListener = object : MovieAdapter.MovieClickListener {
            override fun onMovieClicked(movie: Movie) {
            }

            override fun onFavoriteClicked(movie: Movie) {
                presenter.onFavoriteClicked(movie)
            }
        }

        presenter.onViewReady()
    }

    // endregion

    // region MovieListView

    override fun showList(list: List<Movie>) {
        recyclerView.visibility = View.VISIBLE
        adapter.refreshList(list)
    }

    override fun showHint() {
        tvEmptyList.visibility = View.VISIBLE
    }

    override fun hideHint() {
        tvEmptyList.visibility = View.GONE
    }

    override fun hideList() {
        recyclerView.visibility = View.GONE
    }

    // endregion
}
