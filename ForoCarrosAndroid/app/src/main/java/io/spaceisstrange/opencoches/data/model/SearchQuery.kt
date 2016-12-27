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

package io.spaceisstrange.opencoches.data.model

class SearchQuery {
    /**
     * Parámetros de la búsqueda
     */
    val parameters: MutableMap<String, String> = mutableMapOf()

    /**
     * Añade un nuevo parámetro de búsqueda a la lista. En la medida de lo posible estos parámetros
     * serán los definidos en las constantes de la API para tenerlos bien localizados
     */
    fun addParameter(parameter: String, value: String) {
        parameters.put(parameter, value)
    }
}