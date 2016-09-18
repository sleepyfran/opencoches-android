package io.spaceisstrange.opencoches.ui.activities

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

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.spaceisstrange.opencoches.api.rx.FCLoginObservable
import io.spaceisstrange.opencoches.utils.SharedPreferencesUtils
import java.net.SocketTimeoutException

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Comprobamos si el usuario se ha logueado anteriormente
        if (SharedPreferencesUtils.isLoggedIn(this)) {
            // Nos logueamos y lo mandamos a la pantalla de selección de foros
            val username = SharedPreferencesUtils.getUsername(this)
            val password = SharedPreferencesUtils.getPassword(this)

            FCLoginObservable.create(username, password).subscribe(
                    {
                        result ->

                        // Mostramos la activity con la lista de foros
                        Log.i(this@SplashActivity.javaClass.canonicalName, "¡Logueado!")
                        startActivity(Intent(this@SplashActivity, SubforosActivity::class.java))
                        finish()
                    },
                    {
                        error ->

                        // Si recibimos un timeout es porque probablemente los datos del usuario estén mal
                        if (error is SocketTimeoutException) {
                            // Los eliminamos y mostramos un error
                            SharedPreferencesUtils.removePreferences(this)
                            Log.e(this@SplashActivity.javaClass.canonicalName, "Error al iniciar sesión")

                            // Mandamos al usuario a iniciar sesión
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                    }
            )
        } else {
            // Mostramos la activity para iniciar sesión
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
