/*
 * Made with <3 by Fran González (@spaceisstrange)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package io.spaceisstrange.opencoches.ui.search

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.api.search.Search
import io.spaceisstrange.opencoches.data.api.search.SearchPage
import io.spaceisstrange.opencoches.data.database.DatabaseManager
import io.spaceisstrange.opencoches.data.model.Thread
import io.spaceisstrange.opencoches.ui.common.BaseActivity
import io.spaceisstrange.opencoches.ui.thread.ThreadActivity
import kotlinx.android.synthetic.main.activity_search.*

/**
 * Activity que permite al usuario realizar búsquedas en ForoCoches.
 */
class SearchActivity : BaseActivity() {
    /**
     * Adapter de los hilos del subforo.
     */
    val adapter = SearchAdapter({
        thread ->

        // Abrimos el hilo seleccionado
        startActivity(ThreadActivity.startIntent(this, thread.link))
    })

    /**
     * Variables para hacer el RecyclerView infinito.
     */
    var currentPage = 1
    var totalItemCount = 0

    /**
     * URL actual de la búsqueda para cuando queramos obtener las sucesivas páginas de la misma.
     */
    var searchUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)

        // Configuramos el RecyclerView
        searchResults.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        searchResults.layoutManager = layoutManager

        // Configuramos la RecyclerView para ser "infinita"
        searchResults.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Cargamos más contenido sólo si lo hay
                if (currentPage >= totalItemCount) return

                if (!searchResults.canScrollVertically(1)) {
                    currentPage++
                    loadNextSearchPage()
                }
            }
        })

        // Sólo queremos el swipe to refresh para indicar carga, no para recargar nada
        searchRefresh.isEnabled = false

        // Nos suscribimos a los eventos de la barra de búsqueda para saber cuándo el usuario ha buscado algo
        searchBar.setOnSearchListener(object : FloatingSearchView.OnSearchListener {
            override fun onSearchAction(currentQuery: String?) {
                if (currentQuery != null) {
                    // Buscamos el nuevo término
                    search(currentQuery)
                }
            }

            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
                // Nada de nada
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return true
    }

    /**
     * Busca y muestra los resultados de la query especificada.
     */
    fun search(query: String) {
        showLoading(true)

        val userId = DatabaseManager.userData()?.id!!
        Search(query, userId).observable().subscribe(
                {
                    searchResults ->

                    showLoading(false)

                    if (searchResults.results.isNotEmpty()) {
                        showNoResults(false)
                        totalItemCount = searchResults.pages
                        showSearchResults(searchResults.results)

                        // Guardamos la URL para futuros usos
                        searchUrl = searchResults.searchUrl
                    } else {
                        showNoResults(true)
                    }
                },
                {
                    error ->

                    showError(true)
                }
        )
    }

    /**
     * Método a llamar cuando queramos cargar el contenido de la siguiente página de la búsqueda.
     */
    fun loadNextSearchPage() {
        // Si no tenemos guardada la URL de búsqueda no tiene sentido estar por aquí >.<
        val url = searchUrl ?: return

        // Cargamos la nueva página
        showLoading(true)
        SearchPage(url, currentPage).observable().subscribe(
                {
                    results ->

                    showLoading(false)
                    showMoreSearchResults(results)
                },
                {
                    error ->

                    showError(true)
                }
        )
    }

    /**
     * Método a llamar cuando los datos de la búsqueda estén cargados.
     */
    fun showSearchResults(results: List<Thread>) {
        adapter.update(results)
    }

    /**
     * Método a llamar cuando tenemos más datos de la búsqueda.
     */
    fun showMoreSearchResults(results: List<Thread>) {
        adapter.add(results)
    }

    /**
     * Muestra u oculta el mensaje de no resultados.
     */
    fun showNoResults(show: Boolean) {
        if (show) {
            searchResults?.visibility = View.GONE
            noResults?.visibility = View.VISIBLE
        } else {
            searchResults?.visibility = View.VISIBLE
            noResults?.visibility = View.GONE
        }
    }

    /**
     * Muestra u oculta la animación de carga.
     */
    fun showLoading(show: Boolean) {
        searchRefresh?.isRefreshing = show
    }

    /**
     * Muestra u oculta el mensaje de error.
     */
    fun showError(show: Boolean) {
        if (show) {
            searchResults?.visibility = View.GONE
            error?.visibility = View.VISIBLE
        } else {
            searchResults?.visibility = View.VISIBLE
            error?.visibility = View.GONE
        }
    }
}