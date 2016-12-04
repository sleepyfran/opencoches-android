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

package io.spaceisstrange.opencoches.ui.newthread

import io.spaceisstrange.opencoches.data.api.ApiConstants
import io.spaceisstrange.opencoches.data.api.thread.NewThread
import io.spaceisstrange.opencoches.data.api.thread.NewThreadInfo
import io.spaceisstrange.opencoches.data.firebase.FirebaseReporter
import io.spaceisstrange.opencoches.data.sharedpreferences.SharedPreferencesUtils
import io.spaceisstrange.opencoches.util.RegexUtils
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class NewThreadPresenter @Inject constructor(val view: NewThreadContract.View,
                                             val subforumLink: String,
                                             val sharedPreferencesUtils: SharedPreferencesUtils) : NewThreadContract.Presenter {
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
    }

    override fun makeThread(title: String, body: String) {
        // Obtenemos el ID del subforo
        val subforumId = RegexUtils.subforumIdFromLink().matchEntire(subforumLink)?.groups?.get(1)?.value!!

        // Obtenemos el ID del usuario
        val userId = sharedPreferencesUtils.getUserId()

        // Obtenemos primero la información necesaria para mandar el hilo
        val securityTokenObservable = NewThreadInfo(ApiConstants.NEW_THREAD_NEW_THREAD_URL + ApiConstants.F_URL + subforumId).observable().subscribe(
                {
                    threadInfo ->

                    // Y mandamos el hilo
                    val newThreadObservable = NewThread(title, body, subforumId, threadInfo, userId).observable().subscribe(
                            {
                                threadUrl ->

                                view.showThreadSubmitted(threadUrl)
                            },
                            {
                                error ->

                                FirebaseReporter.report(error)
                                view.showError()
                            }
                    )

                    compositeSubscription.add(newThreadObservable)
                },
                {
                    error ->

                    FirebaseReporter.report(error)
                    view.showError()
                }
        )

        compositeSubscription.add(securityTokenObservable)
    }

    override fun finish() {
        compositeSubscription.unsubscribe()
    }
}