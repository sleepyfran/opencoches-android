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

package io.spaceisstrange.opencoches.ui.thread.reply

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.View
import com.klinker.android.sliding.SlidingActivity
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.api.securitytoken.SecurityToken
import io.spaceisstrange.opencoches.data.api.thread.ThreadReply
import io.spaceisstrange.opencoches.data.bus.Bus
import io.spaceisstrange.opencoches.data.bus.events.RepliedToThreadEvent
import io.spaceisstrange.opencoches.data.database.DatabaseManager
import io.spaceisstrange.opencoches.util.RegexUtil
import kotlinx.android.synthetic.main.activity_reply_thread.*

/**
 * Activity que permite enviar una respuesta a un hilo.
 */
class ReplyThreadActivity : SlidingActivity() {
    companion object {
        /**
         * Clave asociada al título del hilo a responder.
         */
        val THREAD_TITLE = "threadTitle"

        /**
         * Clave asociada al link del hilo a responder.
         */
        val THREAD_LINK = "threadLink"

        /**
         * Clave asociada a si es o no una cita.
         */
        val IS_QUOTE = "isQuote"

        /**
         * Clave asociada con el nombre de la persona a citar.
         */
        val QUOTE = "quote"

        /**
         * Retorna un Intent con los parámetros necesarios para inicializar la activity.
         */
        fun startIntent(context: Context,
                        title: String,
                        link: String): Intent {
            val startIntent = Intent(context, ReplyThreadActivity::class.java)
            startIntent.putExtra(THREAD_TITLE, title)
            startIntent.putExtra(THREAD_LINK, link)
            return startIntent
        }

        /**
         * Retorna un Intent con los parámetros necesarios para inicializar la activity lista para citar un post.
         */
        fun quoteStartIntent(context: Context,
                             title: String,
                             link: String,
                             quote: String): Intent {
            val startIntent = startIntent(context, title, link)
            startIntent.putExtra(IS_QUOTE, true)
            startIntent.putExtra(QUOTE, quote)
            return startIntent
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        setContent(R.layout.activity_reply_thread)

        // Intentamos obtener los datos de los extras del intent
        val threadTitle = intent.extras?.getString(THREAD_TITLE)
                ?: throw IllegalArgumentException("Mayday, necesito el título del hilo para funcionar")
        val threadLink = intent.extras?.getString(THREAD_LINK)
                ?: throw IllegalArgumentException("Como que necesitamos el link del hilo para funcionar")
        val isQuote = intent.extras?.getBoolean(IS_QUOTE) ?: false

        // Intentamos obtener los datos de una quote, si los hay
        var quoteText: String = ""

        if (isQuote) {
            quoteText = intent.extras?.getString(QUOTE)
                    ?: throw IllegalArgumentException("No puedo poner una cita sin el texto original :C")

            putQuoteText(quoteText)
        }

        // Configuramos la activity
        title = threadTitle

        setFab(resources.getColor(R.color.accent), R.drawable.ic_reply_white, {
            reply(threadLink)

            // Deshabilitamos el fab para evitar mandar comentarios dos veces
            enableFab(false)
        })
    }

    /**
     * Manda la respuesta al foro.
     */
    fun reply(threadLink: String) {
        val reply = editor.text()

        // Comprobamos que el mensaje no esté vacío y, en dicho caso, mostramos un error
        if (TextUtils.isEmpty(reply)) {
            showEmptyReply()
            return
        }

        SecurityToken(threadLink).observable().subscribe(
                {
                    securityToken ->

                    // Obtenemos el ID del hilo
                    val threadId = RegexUtil.threadIdFromLink().matchEntire(threadLink)?.groups?.get(1)?.value
                            ?: throw IllegalArgumentException("La URL del hilo no es válida")

                    // Obtenemos el ID del usuario
                    val userId = DatabaseManager.userData()?.id!!

                    ThreadReply(securityToken, threadId, reply, userId).observable().subscribe(
                            {
                                pair ->

                                val success = pair.first
                                val pagesCount = pair.second

                                if (success) {
                                    // Mostramos la Snackbar que indica que se ha enviado la respuesta
                                    val view = findViewById(android.R.id.content)
                                    Snackbar.make(view,
                                            getString(R.string.thread_reply_sent),
                                            Snackbar.LENGTH_SHORT)
                                            .setCallback(object : Snackbar.Callback() {
                                                override fun onDismissed(snackbar: Snackbar?, event: Int) {
                                                    super.onDismissed(snackbar, event)

                                                    // Cuando se oculte la Snackbar notificamos al bus y nos vamos
                                                    Bus.instance.publish(RepliedToThreadEvent(threadLink, pagesCount))
                                                    finish()
                                                }
                                            }).show()
                                } else {
                                    showCouldNotSendReply()
                                }
                            }
                    )
                },
                {
                    error ->

                    showError(true)
                }
        )
    }

    /**
     * Habilita o deshabilita el FAB de enviar.
     */
    fun enableFab(enable: Boolean) {
        val fab = findViewById(R.id.fab)
        fab.isEnabled = enable
    }

    /**
     * Adjunta el texto de la cita al editor.
     */
    fun putQuoteText(quote: String) {
        editor.appendText(quote)
        editor.appendText("\n\n") // El salto de línea que emocionó a Spielberg
    }

    /**
     * Muestra un mensaje de error cuando el campo de texto esté vacío.
     */
    fun showEmptyReply() {
        editor.showError(getString(R.string.thread_reply_empty_body))
    }

    /**
     * Muestra un mensaje indicando que no se ha podido enviar respuesta.
     */
    fun showCouldNotSendReply() {
        val view = findViewById(android.R.id.content)
        Snackbar.make(view, getString(R.string.thread_reply_error), Snackbar.LENGTH_LONG).show()

        // Habilitamos el fab otra vez por si quieren volver a intentarlo
        enableFab(true)
    }

    /**
     * Muestra u oculta el mensaje de error.
     */
    fun showError(show: Boolean) {
        if (show) {
            editor.visibility = View.GONE
            error.visibility = View.VISIBLE
        } else {
            editor.visibility = View.VISIBLE
            error.visibility = View.GONE
        }
    }
}