package io.spaceisstrange.opencoches.ui.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.api.rx.FCPrivateMessageSecurityTokenObservable
import io.spaceisstrange.opencoches.api.rx.FCSendPrivateMessageObservable
import io.spaceisstrange.opencoches.utils.IntentUtils
import kotlinx.android.synthetic.main.activity_message.*

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

class MessageActivity : BaseActivity() {
    /**
     * ID del usuario al que se pretende enviar el mensaje
     */
    lateinit var recipientId: String

    /**
     * Nombre del usuario al que se pretende enviar el mensaje
     */
    lateinit var recipientUsername: String

    /**
     * Security Token asociado al mensaje que usaremos posteriormente para enviarlo
     */
    lateinit var securityToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        setSupportActionBar(toolbar)

        // Cargamos los datos del intent
        recipientId = intent.extras?.getString(IntentUtils.USER_ID_KEY)!!
        recipientUsername = intent.extras?.getString(IntentUtils.USERNAME_KEY)!!

        // Configuramos la UI
        tvMessageRecipient.text = getString(R.string.message_recipient, recipientUsername)

        FCPrivateMessageSecurityTokenObservable.create(recipientId).subscribe(
                {
                    securityToken ->

                    this.securityToken = securityToken

                    // Ocultamos el mensajito de error (si no lo hemos hecho ya)
                    llRoot.visibility = View.VISIBLE
                    hideErrorMessage(vError)
                },
                {
                    error ->

                    // Mostramos el mensajito de error y ocultamos lo demás
                    llRoot.visibility = View.GONE
                    showErrorMessage(vError)
                }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.send_message_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val selectedId = item?.itemId

        if (selectedId == android.R.id.home) {
            onBackPressed()
            return true
        } else if (selectedId == R.id.menu_message_send) {
            // Comprobamos que se hayan rellenado el título y el body del mensaje
            val title = etMessageTitle.text.toString()
            val body = etMessageBody.text.toString()

            if (TextUtils.isEmpty(title)) {
                etMessageTitle.error = getString(R.string.message_error_empty_title)
                return true
            }

            if (TextUtils.isEmpty(body)) {
                etMessageBody.error = getString(R.string.message_error_empty_title)
                return true
            }

            // Si está correcto, enviamos el mensaje
            FCSendPrivateMessageObservable.create(recipientUsername, title, body, securityToken).subscribe(
                    {
                        Toast.makeText(this, getString(R.string.message_sent), Toast.LENGTH_SHORT).show()
                        finish()
                    },
                    {
                        error ->

                        Toast.makeText(this, getString(R.string.message_error_general), Toast.LENGTH_SHORT).show()
                    }
            )

            return true
        }

        return super.onOptionsItemSelected(item)
    }
}