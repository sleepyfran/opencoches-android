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
    }

    /**
     * Presenter asociado a la activity y fragment
     */
    @Inject lateinit var replyThreadPresenter: ReplyThreadPresenter

    /**
     * Bus de la aplicación
     */
    @Inject lateinit var bus: Bus

    override fun init(savedInstanceState: Bundle?) {
        // Intentamos obtener los datos de los extras del intent
        val threadTitle = intent.extras?.getString(THREAD_TITLE)
                ?: throw IllegalArgumentException("Mayday, necesito el título del hilo para funcionar")
        val threadLink = intent.extras?.getString(THREAD_LINK)
                ?: throw IllegalArgumentException("Como que necesitamos el link del hilo para funcionar")

        // Configuramos la activity
        title = threadTitle

        setFab(R.color.accent, R.drawable.ic_reply_white, {
            // Notificamos al presenter sobre el envío
            replyThreadPresenter.sendReply()
        })

        setContent(R.layout.activity_reply_thread)

        // Intentamos conseguir de nuevo el fragment anterior si existe
        var replyThreadFragment = supportFragmentManager.findFragmentById(R.id.fragment) as? ReplyThreadFragment

        if (replyThreadFragment == null) {
            // Sino, lo creamos el fragment y lo añadimos
            replyThreadFragment = ReplyThreadFragment.newInstance()
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
                }
        )
    }


}