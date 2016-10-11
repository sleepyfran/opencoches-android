/*
 * Hecho con <3 por Fran González (@spaceisstrange)
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

import io.spaceisstrange.opencoches.data.api.thread.ThreadPage
import io.spaceisstrange.opencoches.data.bus.Bus
import io.spaceisstrange.opencoches.data.bus.events.PageScrolledEvent
import rx.subscriptions.CompositeSubscription

class ThreadPresenter(var view: ThreadContract.View,
                      val link: String,
                      var currentPage: Int = 1,
                      val bus: Bus) : ThreadContract.Presenter {
    /**
     * CompositeSubscription donde agregar todos los observables que vayamos utilizando
     */
    lateinit var compositeSubscription: CompositeSubscription

    override fun setup() {
        // Nada
    }

    override fun init() {
        // Inicializamos la CompositeSubscription
        compositeSubscription = CompositeSubscription()

        // Cargamos la página actual
        loadPage()

        // Nos subscribimos a los eventos del bus para recibir cuando el usuario ha respondido
        bus.observable().subscribe(
                {
                    event ->

                    // Recargamos la página si el usuario ha respondido al hilo
                    if (event is PageScrolledEvent && event.isSameThread(link)) {
                        loadPage({
                            // Hacemos scroll al final de la página al recargar
                            view.scrollToBottom()
                        })
                    }
                }
        )
    }

    override fun loadPage(onLoad: (() -> Unit)?) {
        // Cargamos los posts del hilo y los mostramos
        view.showLoading(true)

        val postsSubscription = ThreadPage(link, currentPage).observable().subscribe(
                {
                    posts ->

                    view.showLoading(false)
                    view.showError(false)
                    view.showPage(posts, {
                        onLoad?.invoke()
                    })
                },
                {
                    error ->

                    view.showLoading(false)
                    view.showError(true)
                }
        )

        compositeSubscription.add(postsSubscription)
    }

    override fun finish() {
        compositeSubscription.unsubscribe()
    }
}