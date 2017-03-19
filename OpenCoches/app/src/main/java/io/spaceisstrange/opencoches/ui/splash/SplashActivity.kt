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

package io.spaceisstrange.opencoches.ui.splash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.spaceisstrange.opencoches.data.AccountManager
import io.spaceisstrange.opencoches.ui.login.LoginActivity
import io.spaceisstrange.opencoches.ui.subforumlist.SubforumListActivity

/**
 * Activity inicial que verá el usuario donde intentaremos loguearle con los datos que tengamos guardados.
 *
 * En dicho caso, si conseguimos iniciar sesión, mostraremos la lista de subforos o, en caso de error
 * o si no tenemos datos guardados, mostraremos la Activity de inicio de sesión.
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Comprobamos si el usuario se ha logueado anteriormente
        if (AccountManager.isUserLoggedIn()) {
            // Intentamos loguearnos con los datos que tenemos guardados
            AccountManager.loginWithSavedCredentials().subscribe(
                    {
                        loggedIn ->

                        if (loggedIn) {
                            // Mostramos la lista de foros
                            startActivity(Intent(this, SubforumListActivity::class.java))
                            finish()
                        } else {
                            showLogin()
                        }
                    },
                    {
                        error ->

                        // Si hay algún error con los datos que tenemos guardados los eliminamos
                        AccountManager.deleteSession()
                        showLogin()
                    }
            )
        } else {
            showLogin()
        }
    }

    /**
     * Muestra la activity de login.
     */
    fun showLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}