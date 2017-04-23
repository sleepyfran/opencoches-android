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

package io.spaceisstrange.opencoches.util

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.AccountManager
import io.spaceisstrange.opencoches.ui.login.LoginActivity

/**
 * Clase de utilidades a utilizar por las activities.
 */
class ActivityUtils {
    companion object {
        /**
         * Muestra la pantalla de login.
         */
        fun showLogin(activity: AppCompatActivity) {
            val loginIntent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(loginIntent)
            Toast.makeText(activity, R.string.general_login_first, Toast.LENGTH_LONG).show()
            activity.finish()
        }

        /**
         * Intenta loguear al usuario en la aplicación.
         */
        fun attemptLogin(activity: AppCompatActivity) {
            if (!AccountManager.hasUserData()) {
                showLogin(activity)
                return
            }

            AccountManager.loginWithSavedCredentials().subscribe(
                    {
                        loggedIn ->

                        if (!loggedIn) showLogin(activity)
                    }
            )
        }
    }
}