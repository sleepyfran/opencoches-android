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

package io.spaceisstrange.opencoches.ui.replythread

import android.text.TextUtils
import io.spaceisstrange.opencoches.data.api.securitytoken.SecurityToken
import io.spaceisstrange.opencoches.data.api.thread.ThreadReply
import io.spaceisstrange.opencoches.data.bus.Bus
import io.spaceisstrange.opencoches.data.bus.events.RepliedToThreadEvent
import io.spaceisstrange.opencoches.data.sharedpreferences.SharedPreferencesUtils
import io.spaceisstrange.opencoches.util.RegexUtil
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class ReplyThreadPresenter @Inject constructor(val view: ReplyThreadContract.View,
                                               val threadLink: String,
                                               val bus: Bus,
                                               val sharedPreferences: SharedPreferencesUtils) : ReplyThreadContract.Presenter {
    /**
     * CompositeSubscription donde agregar todos los observables que vayamos utilizando
     */
    lateinit var compositeSubscription: CompositeSubscription

    @Inject
    override fun setup() {
        view.setPresenter(this)
    }

    override fun init() {
        // Inicializamos la composite
        compositeSubscription = CompositeSubscription()
    }

    override fun sendReply() {
        // Obtenemos el mensaje introducido en la view asociada y lo enviamos
        val replyMessage = view.getReplyMessage()
        sendReply(replyMessage)
    }

    override fun sendReply(reply: String) {
        // Comprobamos que el mensaje no esté vacío y, en dicho caso, mostramos un error
        if (TextUtils.isEmpty(reply)) {
            view.showEmptyReply()
            return
        }

        // Obtenemos el security token asociado a la página
        SecurityToken(threadLink).observable().subscribe(
                {
                    token ->

                    view.showError(false)

                    // Obtenemos el ID del hilo
                    val threadId = RegexUtil.threadIdFromLink().matchEntire(threadLink)?.groups?.get(1)?.value
                            ?: throw IllegalArgumentException("La URL del hilo no es válida")

                    // Obtenemos el ID del usuario
                    val userId = sharedPreferences.getUserId()

                    // Enviamos el mensaje
                    ThreadReply(token, threadId, reply, userId).observable().subscribe(
                            {
                                pair ->

                                val success = pair.first
                                val pagesCount = pair.second

                                // Si el mensaje se ha enviado con éxito, notificamos esto en el bus
                                if (success) {
                                    bus.publish(RepliedToThreadEvent(threadLink, pagesCount))
                                } else {
                                    // Sino, mostramos un mensaje de error
                                    view.showCouldNotSendReply()
                                }
                            },
                            {
                                error ->

                                view.showError(true)
                            }
                    )
                },
                {
                    error ->

                    view.showError(true)
                }
        )
    }

    override fun finish() {
        // Limpiamos la mierda
        compositeSubscription.unsubscribe()
    }
}