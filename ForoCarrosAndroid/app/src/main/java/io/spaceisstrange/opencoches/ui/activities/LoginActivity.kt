package io.spaceisstrange.opencoches.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.api.rx.FCLoginObservable
import io.spaceisstrange.opencoches.utils.SharedPreferencesUtils
import kotlinx.android.synthetic.main.activity_login.*
import java.net.SocketTimeoutException

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

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tvLoginIn.setOnClickListener {
            // Comprobamos que se han rellenado los dos campos se hayan rellenado
            val username = etLoginUsername.text.toString()
            val password = etLoginPassword.text.toString()

            if (TextUtils.isEmpty(username)) {
                etLoginUsername.error = getString(R.string.login_error_username)
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                etLoginPassword.error = getString(R.string.login_error_password)
                return@setOnClickListener
            }

            // Si está correcto, intentamos loguear al usuario
            FCLoginObservable.create(username, password).subscribe(
                    {
                        loggedIn ->

                        if (loggedIn) {
                            // Guardamos los datos del usuario
                            SharedPreferencesUtils.saveUsername(this, username)
                            SharedPreferencesUtils.savePassword(this, password)

                            // Mostramos la lista de subforos
                            Log.i(this@LoginActivity.javaClass.canonicalName, "¡Logueado!")
                            startActivity(Intent(this@LoginActivity, SubforosActivity::class.java))
                        } else {
                            AlertDialog.Builder(this)
                                    .setTitle(getString(R.string.error_general_title))
                                    .setMessage(getString(R.string.error_login_message))
                                    .setPositiveButton(getString(R.string.error_general_ok), null)
                                    .show()

                            Log.e(this@LoginActivity.javaClass.canonicalName, "Error al iniciar sesión")
                        }
                    },
                    {
                        error ->

                        // Si recibimos un timeout es porque probablemente los datos del usuario estén mal
                        if (error is SocketTimeoutException) {
                            // Los eliminamos y mostramos un error
                            SharedPreferencesUtils.removePreferences(this)

                            AlertDialog.Builder(this)
                                    .setTitle(getString(R.string.error_general_title))
                                    .setMessage(getString(R.string.error_login_message))
                                    .setPositiveButton(getString(R.string.error_general_ok), null)
                                    .show()
                            Log.e(this@LoginActivity.javaClass.canonicalName, "Error al iniciar sesión")
                        }
                    }
            )
        }
    }
}