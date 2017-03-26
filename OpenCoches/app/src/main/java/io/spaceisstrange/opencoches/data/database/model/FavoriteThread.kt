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
 * Representa la información que se guardará con respecto a un hilo favorito en nuestra base de datos. Básicamente
 * contiene la información necesaria para cargar un hilo dentro de la aplicación.
 */
open class FavoriteThread : RealmObject() {
    /**
     * ID del hilo en cuestión. Sólo sirve para identificarlo en nuestra base de datos.
     */
    @PrimaryKey open var id: String? = null

    /**
     * Título del hilo.
     */
    open var title: String? = null

    /**
     * Link del hilo.
     */
    open var link: String? = null

    /**
     * Establece la ID del objeto.
     */
    fun id(id: String?): FavoriteThread {
        this.id = id
        return this
    }

    /**
     * Establece el título del objeto.
     */
    fun title(title: String?): FavoriteThread {
        this.title = title
        return this
    }

    /**
     * Establece el link del objeto.
     */
    fun link(link: String?): FavoriteThread {
        this.link = link
        return this
    }
}