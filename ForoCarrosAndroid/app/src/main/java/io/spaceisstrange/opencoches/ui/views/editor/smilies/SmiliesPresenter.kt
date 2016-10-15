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

package io.spaceisstrange.opencoches.ui.views.editor.smilies

import io.spaceisstrange.opencoches.data.api.smilies.Smilies
import io.spaceisstrange.opencoches.data.bus.Bus
import io.spaceisstrange.opencoches.data.bus.events.SmilySelectedEvent
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class SmiliesPresenter @Inject constructor(val view: SmiliesContract.View, val bus: Bus) : SmiliesContract.Presenter {
    /**
     * CompositeSubscription donde agregar todos los observables que vayamos utilizando
     */
    lateinit var compositeSubscription: CompositeSubscription

    @Inject
    override fun setup() {
        // Nos declaramos como presenter de la view
        view.setPresenter(this)
    }

    override fun init() {
        // Inicializamos la composite y cargamos los smilies
        compositeSubscription = CompositeSubscription()
        loadSmilies()
    }

    override fun loadSmilies() {
        view.showLoading(true)

        val smilySubscription = Smilies().observable().subscribe(
                {
                    smilies ->

                    view.showLoading(false)
                    view.showError(false)
                    view.showSmilies(smilies)
                },
                {
                    error ->

                    view.showLoading(false)
                    view.showError(true)
                }
        )

        // Añadimos la subscripción a la lista para poder desubscribirnos de ella
        compositeSubscription.add(smilySubscription)
    }

    override fun selectSmily(smilyCode: String) {
        bus.publish(SmilySelectedEvent(smilyCode))
    }

    override fun finish() {
        compositeSubscription.unsubscribe()
    }
}