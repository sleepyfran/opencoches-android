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

package io.spaceisstrange.opencoches.ui.thread

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.api.thread.ThreadPage
import io.spaceisstrange.opencoches.data.bus.Bus
import io.spaceisstrange.opencoches.data.bus.events.PageScrolledEvent
import io.spaceisstrange.opencoches.data.bus.events.ThreadPageSearchEvent
import kotlinx.android.synthetic.main.activity_thread.*
import kotlinx.android.synthetic.main.fragment_thread.*

/**
 * Fragment encargado de mostrar una de las páginas de un hilo.
 */
class ThreadFragment : Fragment() {
    companion object {
        /**
         * Crea una nueva instancia del fragment.
         */
        fun instantiate(currentPage: Int, link: String, title: String): ThreadFragment {
            val fragment = ThreadFragment()
            fragment.currentPage = currentPage
            fragment.link = link
            fragment.title = title
            return fragment
        }
    }

    /**
     * Link del hilo en el que nos encontramos.
     */
    lateinit var link: String

    /**
     * Título del hilo en el que nos encontramos.
     */
    lateinit var title: String

    /**
     * Página actual en la que nos encontramos.
     */
    var currentPage = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_thread, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuramos el fragment
        setupSwipeToRefresh()
        setupOnScroll()
        setupOnUserClick()
        setupOnQuoteClick()
        setupBus()

        // Cargamos la página
        loadPage()
    }

    /**
     * Carga el contenido del hilo y lo muestra. El parámetro (opcional) onLoad permite ser notificado
     * cuando finaliza la carga del contenido en la WebView.
     */
    fun loadPage(onLoad: (() -> Unit)? = null) {
        showLoading(true)

        ThreadPage(link, currentPage).observable().subscribe(
                {
                    posts ->

                    // Comprobamos que haya posts que mostrar, sino mostramos el mensaje de +PRV.
                    // Por lo general funcionará aunque no es la mejor comprobación del mundo ya que si
                    // por alguna razón no se carga bien la página y no somos capaces de parsear los posts
                    // se mostrará el mensaje de +PRV. Lo dejamos así hasta mejor opción
                    if (posts.isNotEmpty()) {
                        // Actualizamos el contenido de la WebView
                        postContent.loadContent(posts, {
                            showLoading(false)
                            onLoad?.invoke()
                        })

                        showError(false)
                        showNotAvailable(false)
                    } else {
                        showLoading(false)
                        showNotAvailable(true)
                    }
                },
                {
                    error ->

                    showLoading(false)
                    showNotAvailable(false)
                    showError(true)
                }
        )
    }

    /**
     * Configura el gesto del Swipe para recargar.
     */
    fun setupSwipeToRefresh() {
        refresh.setOnRefreshListener { loadPage() }
    }

    /**
     * Configura la WebView para ocultar el fab de la activity al hacer scroll.
     */
    fun setupOnScroll() {
        postContent.onScroll = {
            x, y, oldX, oldY ->

            if (oldY - y <= 0) {
                // Ocultamos el fab
                activity.fab.hide()
            } else {
                // Mostramos el fab
                activity.fab.show()
            }
        }
    }

    /**
     * Configura la WebView para mostrar el perfil del usuario al pulsar sobre su imagen.
     */
    fun setupOnUserClick() {
        postContent.onUserClick = {
            posterId ->

            // TODO: Mostrar el perfil de usuario
        }
    }

    /**
     * Configura la WebView para mostrar la activity de respuesta cuando se pulse el botón
     * de respuesta.
     */
    fun setupOnQuoteClick() {
        postContent.onQuoteClick = {
            postId ->

            // TODO: Mostrar la activity de respuesta
        }
    }

    /**
     * Configura el bus para mostrar la barra de búsqueda al pulsar sobre el botón de búsqueda o
     * hace scroll hacia el fondo de la página cuando el usuario responda.
     */
    fun setupBus() {
        Bus.instance.observable().subscribe(
                {
                    event ->

                    // Mostramos la barra de búsqueda al ser notificados
                    if (event is ThreadPageSearchEvent && event.link == link) {
                        postContent.showFindDialog("", false)
                    }

                    // Recargamos la página si el usuario ha respondido al hilo
                    if (event is PageScrolledEvent && event.isSameThread(link)) {
                        loadPage({
                            // Hacemos scroll al final de la página al recargar
                            postContent?.pageDown(true)
                        })
                    }
                },
                {
                    error ->

                    // Nada por lo pronto
                }
        )
    }

    /**
     * Muestra u oculta la animación de carga.
     */
    fun showLoading(show: Boolean) {
        if (show) {
            postContent?.visibility = View.GONE
            activity?.fab?.hide()
            refresh?.isRefreshing = true
        } else {
            postContent?.visibility = View.VISIBLE
            activity?.fab?.show()
            refresh?.isRefreshing = false
        }
    }

    /**
     * Muestra u oculta el texto de "+PRV".
     */
    fun showNotAvailable(show: Boolean) {
        if (show) {
            postContent?.visibility = View.GONE
            activity?.fab?.hide()
            notAvailable?.visibility = View.VISIBLE
        } else {
            postContent?.visibility = View.VISIBLE
            activity?.fab?.show()
            notAvailable?.visibility = View.GONE
        }
    }

    /**
     * Muestra u oculta el mensaje de error.
     */
    fun showError(show: Boolean) {
        if (show) {
            postContent?.visibility = View.GONE
            activity?.fab?.hide()
            error?.visibility = View.VISIBLE
        } else {
            postContent?.visibility = View.VISIBLE
            activity?.fab?.show()
            error?.visibility = View.GONE
        }
    }
}