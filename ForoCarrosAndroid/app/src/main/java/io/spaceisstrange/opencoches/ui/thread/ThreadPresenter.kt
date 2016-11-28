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

import io.spaceisstrange.opencoches.data.api.thread.ThreadInfo
import io.spaceisstrange.opencoches.data.api.thread.ThreadPage
import io.spaceisstrange.opencoches.data.api.thread.ThreadQuote
import io.spaceisstrange.opencoches.data.bus.Bus
import io.spaceisstrange.opencoches.data.bus.events.PageScrolledEvent
import io.spaceisstrange.opencoches.data.firebase.FirebaseReporter
import io.spaceisstrange.opencoches.data.model.Thread
import rx.subscriptions.CompositeSubscription

class ThreadPresenter(var view: ThreadContract.View,
                      val link: String,
                      var currentPage: Int = 1,
                      val bus: Bus) : ThreadContract.Presenter {
    /**
     * CompositeSubscription donde agregar todos los observables que vayamos utilizando
     */
    lateinit var compositeSubscription: CompositeSubscription

    companion object {
        /**
         * Carga la información general del hilo. Está feo hacerse un método estático estando en el maravilloso mundo
         * del pseudo-MVP, pero dado que la activity sólo realiza esta opción veía una tontería adaptar el presenter
         * a ella o incluso crear otro
         */
        fun loadThreadInfo(link: String, onLoad: (Thread) -> Unit) {
            ThreadInfo(link).observable().subscribe(
                    {
                        thread ->

                        onLoad(thread)
                    },
                    {
                        error ->

                        // Reportamos el error
                        FirebaseReporter.report(error)

                        // Y a silenciar se ha dicho
                    }
            )
        }
    }

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
                },
                {
                    error ->

                    // Reportamos el error
                    FirebaseReporter.report(error)
                    view.showError(true)
                }
        )
    }

    override fun loadPage(onLoad: (() -> Unit)?) {
        // Cargamos los posts del hilo y los mostramos
        view.showLoading(true)

        val postsSubscription = ThreadPage(link, currentPage).observable().subscribe(
                {
                    posts ->

                    // Comprobamos que haya posts que mostrar, sino mostramos el mensaje de +PRV.
                    // Por lo general funcionará aunque no es la mejor comprobación del mundo ya que si
                    // por alguna razón no se carga bien la página y no somos capaces de parsear los posts
                    // se mostrará el mensaje de +PRV. Lo dejamos así hasta mejor opción
                    if (posts.size > 0) {
                        view.showNotAvailable(false)
                        view.showLoading(false)
                        view.showError(false)
                        view.showPage(posts, {
                            onLoad?.invoke()
                        })
                    } else {
                        view.showLoading(false)
                        view.showNotAvailable(true)
                    }
                },
                {
                    error ->

                    // Reportamos el error
                    FirebaseReporter.report(error)
                    view.showLoading(false)
                    view.showError(true)
                }
        )

        compositeSubscription.add(postsSubscription)
    }

    override fun quote(postId: String) {
        // Cargamos el texto de la cita y notificamos a la view cuandoe esté disponible
        val quoteSubscription = ThreadQuote(postId).observable().subscribe(
                {
                    quote ->

                    view.openEditorWithQuote(quote)
                },
                {
                    error ->

                    view.showError(true)
                }
        )

        compositeSubscription.add(quoteSubscription)
    }

    override fun finish() {
        compositeSubscription.unsubscribe()
    }
}