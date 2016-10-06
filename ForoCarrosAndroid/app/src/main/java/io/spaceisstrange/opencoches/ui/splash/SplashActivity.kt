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

package io.spaceisstrange.opencoches.ui.splash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.spaceisstrange.opencoches.App
import io.spaceisstrange.opencoches.data.api.login.Login
import io.spaceisstrange.opencoches.ui.login.LoginActivity
import io.spaceisstrange.opencoches.ui.subforumlist.SubforumListActivity
import io.spaceisstrange.opencoches.data.sharedpreferences.SharedPreferencesUtils
import rx.Subscription
import java.net.SocketTimeoutException
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {
    /**
     * Subscripción del login
     */
    var loginSubscription: Subscription? = null
        set(value) {
            loginSubscription?.unsubscribe()
            field = value
        }

    @Inject lateinit var sharedPreferences: SharedPreferencesUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerSplashComponent.builder()
                .sharedPreferencesUtilsComponent((application as App).sharedPrefsComponent)
                .build()
                .inject(this)

        // Comprobamos si el usuario se ha logueado anteriormente
        if (sharedPreferences.isLoggedIn()) {
            // Nos logueamos y lo mandamos a la pantalla de selección de foros
            val username = sharedPreferences.getUsername()
            val password = sharedPreferences.getPassword()

            loginSubscription = Login(username, password).observable().subscribe(
                    {
                        result ->

                        // Mostramos la lista de foros
                        startActivity(Intent(this, SubforumListActivity::class.java))
                        finish()
                    },
                    {
                        error ->

                        // Si recibimos un timeout es porque probablemente los datos del usuario estén mal
                        if (error is SocketTimeoutException) {
                            // Los eliminamos y mostramos un error
                            sharedPreferences.removePreferences()

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

    override fun onDestroy() {
        super.onDestroy()
        loginSubscription?.unsubscribe()
    }
}