package io.spaceisstrange.forocarrosandroid.utils

import android.content.Context
import android.content.SharedPreferences

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

class SharedPreferencesUtils {
    companion object {
        /**
         * Clave general de las SharedPreferences
         */
        const val SHARED_PREFERENCES_KEY = "foroCarrosAndroid"

        /**
         * Clave para la contraseña. Sí, guardar la contraseña en las SharedPreferences es chustero
         * pero qué le vamos a hacer si el jefe no nos da otra opción ni una API. De todos modos
         * si alguien tiene una mejor sugerencia de cómo guardarlos los Pull Request están abiertos :)
         */
        const val USERNAME_KEY = "user"
        const val PASSWORD_KEY = "pass"

        fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        }

        /**
         * Retorna true si tenemos datos del usuario guardados y false en caso contrario
         */
        fun isLoggedIn(context: Context): Boolean {
            val sharedPrefs = getSharedPreferences(context)

            return sharedPrefs.contains(USERNAME_KEY) && sharedPrefs.contains(PASSWORD_KEY)
        }

        /**
         * Guarda el nombre usuario en las SharedPreferences
         */
        fun saveUsername(context: Context, username: String) {
            val sharedPrefs = getSharedPreferences(context)
            sharedPrefs.edit().putString(USERNAME_KEY, username).apply()
        }

        /**
         * Guarda la contraseña del usuario en las SharedPreferences
         */
        fun savePassword(context: Context, password: String) {
            val sharedPrefs = getSharedPreferences(context)
            sharedPrefs.edit().putString(PASSWORD_KEY, password).apply()
        }

        /**
         * Retorna el username del usuario de las SharedPreferences. Lanza un IllegalArguments
         * si no encuentra los datos
         */
        fun getUsername(context: Context): String {
            val sharedPrefs = getSharedPreferences(context)

            if (!sharedPrefs.contains(USERNAME_KEY)) {
                throw IllegalArgumentException("No tenemos datos guardados en las SharedPreferences")
            }

            return sharedPrefs.getString(USERNAME_KEY, "")
        }

        /**
         * Retorna la contraseña del usuario de las SharedPreferences. Lanza un IllegalArguments
         * si no encuentra los datos
         */
        fun getPassword(context: Context): String {
            val sharedPrefs = getSharedPreferences(context)

            if (!sharedPrefs.contains(PASSWORD_KEY)) {
                throw IllegalArgumentException("No tenemos datos guardados en las SharedPreferences")
            }

            return sharedPrefs.getString(PASSWORD_KEY, "")
        }

        /**
         * Borra todos los datos de las SharedPreferences
         */
        fun removePreferences(context: Context) {
            getSharedPreferences(context).edit().clear().apply()
        }
    }
}