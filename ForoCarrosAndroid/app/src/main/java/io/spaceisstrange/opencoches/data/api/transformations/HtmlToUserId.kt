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

import io.spaceisstrange.opencoches.util.RegexUtil
import org.jsoup.nodes.Document

class HtmlToUserId {
    companion object {
        /**
         * Retorna el ID del usuario del documento especificado
         */
        fun transform(document: Document): String {
            // Buscamos el enlace a nuestro usuario
            val userLink = document.select("a[href^=member.php?u=]").attr("href")
            val userId = RegexUtil.userIdFromLink().matchEntire(userLink)?.groups?.get(1)?.value
                    ?: throw IllegalStateException("No se ha podido determinar el ID del usuario")

            return userId
        }
    }
}