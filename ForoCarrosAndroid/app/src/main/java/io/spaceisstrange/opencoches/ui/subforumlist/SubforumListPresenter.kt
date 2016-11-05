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

package io.spaceisstrange.opencoches.ui.subforumlist

import com.google.firebase.crash.FirebaseCrash
import io.spaceisstrange.opencoches.data.api.subforumlist.SubforumList
import io.spaceisstrange.opencoches.data.model.Subforum
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class SubforumListPresenter @Inject constructor(val view: SubforumListContract.View) : SubforumListContract.Presenter {
    /**
     * CompositeSubscription donde agregar todos los observables que vayamos utilizando
     */
    lateinit var compositeSubscription: CompositeSubscription

    @Inject
    override fun setup() {
        // Nos declaramos presenters de la view
        view.setPresenter(this)
    }

    override fun init() {
        // Inicializamos la CompositeSubscription
        compositeSubscription = CompositeSubscription()

        // Cargamos los subforos
        loadSubforums()
    }

    override fun loadSubforums() {
        // Cargamos los subforos y se los pasamos a la view
        val subforumSubscription = SubforumList().observable().subscribe(
                {
                    subforums ->

                    view.showError(false)
                    view.showLoading(false)
                    view.showSubforums(subforums)
                },
                {
                    error ->

                    // Reportamos el error
                    FirebaseCrash.report(error)
                    view.showError(true)
                }
        )

        // Añadimos la subscripción al Composite para poder desubscribirnos cuando recibamos finish
        compositeSubscription.add(subforumSubscription)
    }

    override fun openSubforum(subforum: Subforum) {
        view.showSubforum(subforum)
    }

    override fun finish() {
        compositeSubscription.unsubscribe()
    }
}