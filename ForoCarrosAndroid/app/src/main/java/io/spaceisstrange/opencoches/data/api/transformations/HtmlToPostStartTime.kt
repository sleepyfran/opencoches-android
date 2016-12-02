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

package io.spaceisstrange.opencoches.data.api.transformations

import io.spaceisstrange.opencoches.data.api.ApiConstants
import org.jsoup.nodes.Document

class HtmlToPostStartTime {
    companion object {
        /**
         * Retorna el Post Hash asociado al documento especificado
         */
        fun transform(document: Document): String {
            // Obtenemos el security token del input actual
            val input = document.select("input[name=" + ApiConstants.POST_START_TIME_KEY + "]").first()
            val postStartTime = input.attr("value")

            // Si está vacío es que probablemente no haya ningún post start time en la página
            if (postStartTime == "") {
                throw IllegalStateException("No se puede obtener el start time de una página que no lo tiene")
            }

            return postStartTime
        }
    }
}