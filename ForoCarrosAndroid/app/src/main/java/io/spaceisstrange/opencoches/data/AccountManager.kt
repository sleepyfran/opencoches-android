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

package io.spaceisstrange.opencoches.data

import io.spaceisstrange.opencoches.data.api.login.Login
import io.spaceisstrange.opencoches.data.sharedpreferences.SharedPreferencesUtils
import rx.Subscription

class AccountManager {
    companion object {
        /**
         * Verifica que el usuario actual está logueado
         */
        fun isUserLoggedIn(sharedPrefs: SharedPreferencesUtils): Boolean {
            return sharedPrefs.isLoggedIn()
        }

        /**
         * Loguea al usuario en la web y retorna el resultado
         */
        fun login(sharedPrefs: SharedPreferencesUtils,
                  username: String,
                  password: String,
                  onResult: (Boolean, Throwable?) -> Unit) {
            Login(username, password).observable().subscribe(
                    {
                        loggedIn ->

                        // Guardamos las credenciales
                        saveCredentials(sharedPrefs, username, password)

                        onResult(loggedIn, null)
                    },
                    {
                        error ->

                        onResult(false, error)
                    }
            )
        }

        /**
         * Loguea al usuario con los datos guardados en las SharedPreferences (si los hay) y devuelve el resultado
         */
        fun loginWithSavedCredentials(sharedPrefs: SharedPreferencesUtils,
                                      onResult: (result: Boolean) -> Unit) {
            try {
                val username = sharedPrefs.getUsername()
                val password = sharedPrefs.getPassword()

                Login(username, password).observable().subscribe(
                        {
                            loggedIn ->

                            onResult(loggedIn)
                        },
                        {
                            error ->

                            onResult(false)
                        }
                )
            } catch (e: IllegalArgumentException) {
                onResult(false)
            }
        }

        /**
         * Guarda las credenciales del usuario tras haber iniciado sesión
         */
        fun saveCredentials(sharedPrefs: SharedPreferencesUtils, username: String, password: String) {
            sharedPrefs.saveUsername(username)
            sharedPrefs.savePassword(password)
        }
    }
}