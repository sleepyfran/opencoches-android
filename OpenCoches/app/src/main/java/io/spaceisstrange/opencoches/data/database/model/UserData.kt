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

package io.spaceisstrange.opencoches.data.database.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Representa la información que se guardará en la base de datos relativa a los datos de inicio de sesión
 * del usuario de nuestra aplicación.
 */
open class UserData : RealmObject() {
    /**
     * ID del usuario que usa nuestra aplicación. Lo necesitaremos en varias peticiones.
     */
    @PrimaryKey open var id: String? = null

    /**
     * Nombre de usuario que usaremos para iniciar sesión.
     */
    open var username: String? = null

    /**
     * Contraseña que usaremos para iniciar sesión.
     */
    open var password: String? = null
}