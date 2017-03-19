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

package io.spaceisstrange.opencoches.data

import io.spaceisstrange.opencoches.data.api.login.Login
import io.spaceisstrange.opencoches.data.api.login.UserId
import io.spaceisstrange.opencoches.data.database.DatabaseManager
import io.spaceisstrange.opencoches.data.database.model.UserData
import rx.Observable

/**
 * Gestor de la cuenta del usuario desde donde podremos loguear y desloguear al usuario.
 */
class AccountManager {
    companion object {
        /**
         * Verifica si el usuario ha iniciado sesión anteriormente o no.
         */
        fun isUserLoggedIn(): Boolean {
            return DatabaseManager.hasUserData()
        }

        /**
         * Loguea al usuario en la web y retorna el resultado.
         */
        fun login(username: String, password: String): Observable<Boolean> {
            val loginObservable = Login(username, password).observable()

            // Obtiene el ID del usuario y lo guarda junto con el nombre de usuario y contraseña
            val userIdObservable = UserId().observable().map(
                    {
                        userId ->

                        val userData = UserData()
                        userData.username = username
                        userData.password = password
                        userData.id = userId

                        DatabaseManager.saveUserData(userData)
                    }
            )

            return loginObservable.flatMap(
                    {
                        loggedIn ->

                        // Ejecutamos el observable de UserId sólo si hemos conseguido loguearnos
                        if (loggedIn) {
                            userIdObservable.map({ userId -> loggedIn })
                        } else {
                            Observable.just(loggedIn)
                        }
                    })
        }

        /**
         * Loguea al usuario con los datos guardados en la base de datos y retorna el resultado.
         */
        fun loginWithSavedCredentials(): Observable<Boolean> {
            val userData = DatabaseManager.getUserData()!!
            val username = userData.username!!
            val password = userData.password!!

            return Login(username, password).observable().map(
                    {
                        loggedIn ->

                        // Si ha habido algún error eliminamos los datos guardados
                        if (!loggedIn) {
                            deleteSession()
                        }

                        loggedIn
                    }
            )
        }

        /**
         * Borra los datos de la base de datos y las cookies guardadas actualmente.
         */
        fun deleteSession() {
            DatabaseManager.nuke()
            CookiesCache.cookies = null
        }
    }
}