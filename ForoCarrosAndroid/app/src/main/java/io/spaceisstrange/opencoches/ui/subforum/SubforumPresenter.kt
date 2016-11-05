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

package io.spaceisstrange.opencoches.ui.subforum

import com.google.firebase.crash.FirebaseCrash
import io.spaceisstrange.opencoches.data.api.subforum.Subforum
import io.spaceisstrange.opencoches.data.model.Thread
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class SubforumPresenter @Inject constructor(val view: SubforumContract.View, val link: String) : SubforumContract.Presenter {
    /**
     * CompositeSubscription donde agregar todos los observables que vayamos utilizando
     */
    lateinit var compositeSubscription: CompositeSubscription

    /**
     * Página actual del subforo
     */
    var subforumActualPage = 1

    /**
     * Carga los hilos del subforo especificado en la página especificada y realiza la acción
     * que se especifica en el método pasado cuando se haya terminado de cargar el contenido
     */
    private fun loadThreads(link: String, onLoad: (threads: List<Thread>) -> Unit) {
        view.showLoading(true)

        val subforumObservable = Subforum(link, subforumActualPage).observable().subscribe(
                {
                    threads ->

                    view.showError(false)
                    view.showLoading(false)
                    onLoad(threads)
                },
                {
                    error ->

                    // Reportamos el error
                    FirebaseCrash.report(error)
                    view.showLoading(false)
                    view.showError(true)
                }
        )

        compositeSubscription.add(subforumObservable)
    }

    @Inject
    override fun setup() {
        // Nos declaramos presenters de la view
        view.setPresenter(this)
    }

    override fun init() {
        // Inicializamos la CompositeSubscription
        compositeSubscription = CompositeSubscription()

        // Cargamos los hilos
        loadThreads()
    }

    override fun reloadThreads() {
        subforumActualPage = 1
        loadThreads()
    }

    override fun loadThreads() {
        loadThreads(link, {
            threads ->

            view.showThreads(threads)
        })
    }

    override fun loadNextPage() {
        subforumActualPage++

        loadThreads(link, {
            threads ->

            view.addThreads(threads)
        })
    }

    override fun openThread(thread: Thread) {
        view.showThread(thread)
    }

    override fun finish() {
        compositeSubscription.unsubscribe()
    }
}