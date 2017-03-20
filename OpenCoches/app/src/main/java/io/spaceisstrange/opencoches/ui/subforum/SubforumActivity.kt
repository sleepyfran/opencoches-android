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

package io.spaceisstrange.opencoches.ui.subforum

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.api.subforum.Subforum
import io.spaceisstrange.opencoches.data.model.Thread
import io.spaceisstrange.opencoches.ui.common.BaseActivity
import kotlinx.android.synthetic.main.activity_subforum.*

/**
 * Activity que muestra un subforo y su contenido. Para la carga general la activity necesita recibir mediante los extras
 * del intent el título del subforo y el link del mismo.
 */
class SubforumActivity : BaseActivity() {
    companion object {
        /**
         * Clave asociada al título del subforo.
         */
        val SUBFORUM_TITLE = "subforumTitle"

        /**
         * Clave asociada al link del subfroo.
         */
        val SUBFORUM_LINK = "subforumLink"

        /**
         * Retorna un Intent con los parámetros necesarios para inicializar la activity.
         */
        fun startIntent(context: Context, subforumTitle: String, subforumLink: String): Intent {
            val startIntent = Intent(context, SubforumActivity::class.java)
            startIntent.putExtra(SUBFORUM_TITLE, subforumTitle)
            startIntent.putExtra(SUBFORUM_LINK, subforumLink)
            return startIntent
        }
    }

    /**
     * Adapter del subforo actual.
     */
    val adapter = SubforumAdapter({
        thread ->

        // TODO: Cargar el hilo
    })

    /**
     * Link del subforo que vamos a cargar.
     */
    lateinit var subforumLink: String

    /**
     * Página actual del subforo en la que nos encontramos. La usaremos para la carga de páginas según
     * vayamos haciendo scroll en la RecyclerView.
     */
    var actualPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subforum)
        setSupportActionBar(toolbar)

        // Cargamos el link del subforo de los extras del intent
        val subforumTitle = intent.extras?.getString(SUBFORUM_TITLE)
                ?: throw IllegalArgumentException("Necesitamos el título del hilo, Houston")
        subforumLink = intent.extras?.getString(SUBFORUM_LINK)
                ?: throw IllegalArgumentException("No soy mago, no puedo cargar el subforo sin link")

        setToolbarTitle(subforumTitle)

        // Configuramos la RecyclerView
        subforumThreads.adapter = adapter
        subforumThreads.layoutManager = LinearLayoutManager(this)

        // Configuramos la RecyclerView para ser "infinita" y para ocultar el fab al hacer scroll
        subforumThreads.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Si hemos llegado al final de la lista
                if (!subforumThreads.canScrollVertically(1)) {
                    // Cargamos la siguiente página
                    actualPage++
                    loadThreads({ threads -> addThreads(threads) })
                }

                // Ocultamos/Mostramos el fab al hacer scroll
                if (dy > 0) {
                    fab.hide()
                } else {
                    fab.show()
                }
            }
        })

        // Al recargar, ponemos la página a 1 y volvemos a cargar
        refresh.setOnRefreshListener {
            actualPage = 1
            loadThreads({ threads -> showThreads(threads) })
        }

        // Cargamos la primera página del subforo
        loadThreads({ threads -> showThreads(threads) })
    }

    /**
     * Carga los hilos del subforo actual y de la página que nos encontremos actualmente.
     */
    fun loadThreads(onLoad: (threads: List<Thread>) -> Unit) {
        showLoading(true)

        Subforum(subforumLink, actualPage).observable().subscribe(
                {
                    threads ->

                    showLoading(false)
                    showError(false)
                    onLoad(threads)
                },
                {
                    error ->

                    showLoading(false)
                    showError(true)
                }
        )
    }

    /**
     * Sustituye los hilos actuales (si hay algunos) con los cargados
     */
    fun showThreads(threads: List<Thread>) {
        adapter.update(threads)
    }

    /**
     * Añade los hilos cargados a la lista actual.
     */
    fun addThreads(threads: List<Thread>) {
        adapter.addThreads(threads)
    }

    /**
     * Muestra u oculta la animación de carga.
     */
    fun showLoading(loading: Boolean) {
        refresh.isRefreshing = loading
    }

    /**
     * Muestra u oculta el mensaje de error
     */
    fun showError(show: Boolean) {
        if (show) {
            subforumThreads?.visibility = View.GONE
            error?.visibility = View.VISIBLE
        } else {
            subforumThreads?.visibility = View.VISIBLE
            error?.visibility = View.GONE
        }
    }
}