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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.klinker.android.sliding.SlidingActivity
import io.spaceisstrange.opencoches.App
import io.spaceisstrange.opencoches.AppModule
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.bus.Bus
import io.spaceisstrange.opencoches.data.bus.events.RepliedToThreadEvent
import io.spaceisstrange.opencoches.util.ActivityUtils
import io.spaceisstrange.opencoches.util.SnackbarUtils
import javax.inject.Inject

class ReplyThreadActivity : SlidingActivity() {
    /**
     * Presenter asociado a la activity y fragment
     */
    @Inject lateinit var replyThreadPresenter: ReplyThreadPresenter

    /**
     * Bus de la aplicación
     */
    @Inject lateinit var bus: Bus

    companion object {
        /**
         * Clave asociada al título del hilo a responder
         */
        val THREAD_TITLE = "threadTitle"

        /**
         * Clave asociada al link del hilo a responder
         */
        val THREAD_LINK = "threadLink"

        /**
         * Clave asociada a si es o no una cita
         */
        val IS_QUOTE = "isQuote"

        /**
         * Clave asociada con el nombre de la persona a citar
         */
        val QUOTE = "quote"

        /**
         * Retorna un Intent con los parámetros necesarios para inicializar la activity
         */
        fun getStartIntent(context: Context,
                           title: String,
                           link: String): Intent {
            val startIntent = Intent(context, ReplyThreadActivity::class.java)
            startIntent.putExtra(THREAD_TITLE, title)
            startIntent.putExtra(THREAD_LINK, link)
            return startIntent
        }

        /**
         * Retorna un Intent con los parámetros necesarios para inicializar la activity lista para citar un post
         */
        fun getQuoteStartIntent(context: Context,
                                title: String,
                                link: String,
                                quote: String): Intent {
            val startIntent = getStartIntent(context, title, link)
            startIntent.putExtra(IS_QUOTE, true)
            startIntent.putExtra(QUOTE, quote)
            return startIntent
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        // Intentamos obtener los datos de los extras del intent
        val threadTitle = intent.extras?.getString(THREAD_TITLE)
                ?: throw IllegalArgumentException("Mayday, necesito el título del hilo para funcionar")
        val threadLink = intent.extras?.getString(THREAD_LINK)
                ?: throw IllegalArgumentException("Como que necesitamos el link del hilo para funcionar")
        val isQuote = intent.extras?.getBoolean(IS_QUOTE) ?: false

        // Intentamos obtener los datos de una quote, si los hay
        var quoteName: String = ""

        if (isQuote) {
            quoteName = intent.extras?.getString(QUOTE)
                    ?: throw IllegalArgumentException("No puedo poner una cita sin el texto original :C")
        }

        // Configuramos la activity
        title = threadTitle

        setFab(resources.getColor(R.color.accent), R.drawable.ic_reply_white, {
            // Notificamos al presenter sobre el envío
            replyThreadPresenter.sendReply()

            // Deshabilitamos el fab para evitar mandar comentarios dos veces
            enableFab(false)
        })

        setContent(R.layout.activity_reply_thread)

        // Intentamos conseguir de nuevo el fragment anterior si existe
        var replyThreadFragment = supportFragmentManager.findFragmentById(R.id.fragment) as? ReplyThreadFragment

        if (replyThreadFragment == null) {
            // Sino, lo creamos el fragment y lo añadimos
            replyThreadFragment = ReplyThreadFragment.newInstance(isQuote, quoteName)
            ActivityUtils.addFragmentToActivity(supportFragmentManager, replyThreadFragment, R.id.fragment)
        }

        // Inyectamos la activity
        DaggerReplyThreadComponent.builder()
                .replyThreadModule(ReplyThreadModule(replyThreadFragment, threadLink))
                .busComponent((application as App).busComponent)
                .appModule(AppModule(applicationContext))
                .build()
                .inject(this)

        // Nos subscribimos al bus para ver cuándo se ha enviado la respuesta
        bus.observable().subscribe(
                {
                    event ->

                    if (event is RepliedToThreadEvent) {
                        // Mostramos una snackbar y nos vamos cuando se oculte
                        SnackbarUtils.makeSnackbar(findViewById(android.R.id.content),
                                getString(R.string.thread_reply_sent),
                                Snackbar.LENGTH_SHORT)
                                .setCallback(object : Snackbar.Callback() {
                                    override fun onDismissed(snackbar: Snackbar?, event: Int) {
                                        super.onDismissed(snackbar, event)
                                        finish()
                                    }
                                }).show()
                    }
                },
                {
                    error ->

                    // Nada, silenciamos
                }
        )
    }

    /**
     * Habilita o deshabilita el FAB de enviar
     */
    fun enableFab(enable: Boolean) {
        val fab = findViewById(R.id.fab)
        fab.isEnabled = enable
    }
}