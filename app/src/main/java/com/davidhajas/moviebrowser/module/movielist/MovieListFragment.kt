package com.davidhajas.moviebrowser.module.movielist


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.davidhajas.moviebrowser.R
import com.davidhajas.moviebrowser.datasource.movie.entity.Movie
import com.davidhajas.moviebrowser.module.MovieAdapter
import com.davidhajas.moviebrowser.plugin.threading.DefaultDispatcherProvider
import com.davidhajas.moviebrowser.util.Injector
import kotlinx.android.synthetic.main.fragment_movie_list.*
import kotlinx.coroutines.delay

class MovieListFragment : Fragment(), MovieListView {

    private val adapter = MovieAdapter()

    private val presenter: MovieListPresenter by lazy {
        val movieRemoteRepository = Injector.injectMovieRemoteRepository()
        val context = context!!
        val movieLocalRepository = Injector.injectMovieLocalRepository(context)
        val movieInteractor = Injector.injectMovieInteractor(movieRemoteRepository, movieLocalRepository)
        MovieListPresenter(this, context, lifecycle, DefaultDispatcherProvider(), movieInteractor)
    }

    // region Fragment callbacks

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movie_list, container, false)

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

        swipeRefreshLayout.setOnRefreshListener {
            presenter.onRefresh(etSearch.text.toString())
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                presenter.onSearch(s?.toString() ?: "")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        presenter.onViewReady()
    }

    // endregion

    // region MovieListView

    override fun showLoading() {
        pbLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pbLoading.visibility = View.INVISIBLE
    }

    override fun showList(list: List<Movie>) {
        recyclerView.visibility = View.VISIBLE
        adapter.refreshList(list)
        swipeRefreshLayout.isRefreshing = false
    }

    override fun updateFavorite(movie: Movie, favorite: Boolean) {
        adapter.updateItem(movie, favorite)
    }

    override fun hideList() {
        recyclerView.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = false
    }

    override fun showHint() {
        tvHint.visibility = View.VISIBLE
    }

    override fun hideHint() {
        tvHint.visibility = View.GONE
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        swipeRefreshLayout.isRefreshing = false
    }

    // endregion
}
