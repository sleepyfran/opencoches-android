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
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class ThreadPresenter @Inject constructor(var view: ThreadContract.View,
                                          val link: String,
                                          var currentPage: Int = 1) : ThreadContract.Presenter {
    /**
     * CompositeSubscription donde agregar todos los observables que vayamos utilizando
     */
    lateinit var compositeSubscription: CompositeSubscription

    @Inject
    override fun setup() {
        // Nada
    }

    override fun init() {
        // Inicializamos la CompositeSubscription
        compositeSubscription = CompositeSubscription()

        // Cargamos la página actual
        loadPage()
    }

    override fun loadPage() {
        // Cargamos los posts del hilo y los mostramos
        val postsSubscription = ThreadPage(link, currentPage).observable().subscribe(
                {
                    posts ->

                    view.showError(false)
                    view.showPage(posts)
                },
                {
                    error ->

                    view.showError(true)
                }
        )

        compositeSubscription.add(postsSubscription)
    }

    override fun finish() {
        compositeSubscription.unsubscribe()
    }
}