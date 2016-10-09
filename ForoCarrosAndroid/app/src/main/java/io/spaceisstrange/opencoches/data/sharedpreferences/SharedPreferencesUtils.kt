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

package io.spaceisstrange.opencoches.data.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

/**
 * Set de utilidades de las SharedPreferences
 */
class SharedPreferencesUtils @Inject constructor(val context: Context) {
    /**
     * Clave general de las SharedPreferences
     */
    val SHARED_PREFERENCES_KEY = "foroCarrosAndroid"

    /**
     * Clave para el ID del usuario
     */
    val USER_ID_KEY = "userId"

    /**
     * Clave para la contraseña. Sí, guardar la contraseña en las SharedPreferences es chustero
     * pero qué le vamos a hacer si el jefe no nos da otra opción ni una API. De todos modos
     * si alguien tiene una mejor sugerencia de cómo guardarlos los Pull Request están abiertos :)
     */
    val USERNAME_KEY = "user"
    val PASSWORD_KEY = "pass"

    private fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
    }

    /**
     * Retorna true si tenemos datos del usuario guardados y false en caso contrario
     */
    fun isLoggedIn(): Boolean {
        val sharedPrefs = getSharedPreferences()

        return sharedPrefs.contains(USERNAME_KEY) && sharedPrefs.contains(PASSWORD_KEY)
    }

    /**
     * Retorna true si tenemos los datos del usuario guardados y false en caso contrario
     */
    fun containsUserData(): Boolean {
        val sharedPrefs = getSharedPreferences()
        return sharedPrefs.contains(USER_ID_KEY)
    }

    /**
     * Guarda el ID del usuario en las SharedPreferences
     */
    fun saveUserId(userId: String) {
        val sharedPrefs = getSharedPreferences()
        sharedPrefs.edit().putString(USER_ID_KEY, userId).apply()
    }

    /**
     * Guarda el nombre usuario en las SharedPreferences
     */
    fun saveUsername(username: String) {
        val sharedPrefs = getSharedPreferences()
        sharedPrefs.edit().putString(USERNAME_KEY, username).apply()
    }

    /**
     * Guarda la contraseña del usuario en las SharedPreferences
     */
    fun savePassword(password: String) {
        val sharedPrefs = getSharedPreferences()
        sharedPrefs.edit().putString(PASSWORD_KEY, password).apply()
    }

    /**
     * Retorna el user ID del usuario de las SharedPreferences. Lanza un IllegalArguments
     * si no encuentra los datos
     */
    fun getUserId(): String {
        val sharedPrefs = getSharedPreferences()

        if (!sharedPrefs.contains(USER_ID_KEY)) {
            throw IllegalArgumentException("No tenemos datos guardados en las SharedPreferences")
        }

        return sharedPrefs.getString(USER_ID_KEY, "")
    }

    /**
     * Retorna el username del usuario de las SharedPreferences. Lanza un IllegalArguments
     * si no encuentra los datos
     */
    fun getUsername(): String {
        val sharedPrefs = getSharedPreferences()

        if (!sharedPrefs.contains(USERNAME_KEY)) {
            throw IllegalArgumentException("No tenemos datos guardados en las SharedPreferences")
        }

        return sharedPrefs.getString(USERNAME_KEY, "")
    }

    /**
     * Retorna la contraseña del usuario de las SharedPreferences. Lanza un IllegalArguments
     * si no encuentra los datos
     */
    fun getPassword(): String {
        val sharedPrefs = getSharedPreferences()

        if (!sharedPrefs.contains(PASSWORD_KEY)) {
            throw IllegalArgumentException("No tenemos datos guardados en las SharedPreferences")
        }

        return sharedPrefs.getString(PASSWORD_KEY, "")
    }

    /**
     * Borra todos los datos de las SharedPreferences
     */
    fun removePreferences() {
        getSharedPreferences().edit().clear().apply()
    }
}