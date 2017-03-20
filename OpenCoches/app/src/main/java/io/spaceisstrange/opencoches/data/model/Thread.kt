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

package io.spaceisstrange.opencoches.data.model

import io.spaceisstrange.opencoches.data.api.ApiConstants

/**
 * Representación de un hilo, con título, link, páginas y si es o no "sticky".
 */
data class Thread(val title: String,
                  val link: String,
                  val pages: Int = 1,
                  val isSticky: Boolean = false) {
    companion object {
        /**
         * Devuelve la cantidad de páginas dado un número de mensajes.
         */
        fun pagesFromMessages(messages: String): Int {
            val messagesNumber = messages.replace(".", "").toInt()

            // Si no hay respuestas entonces, por cojones, sólo hay una página
            if (messagesNumber == 0) return 1

            val pagesNumber = Math.ceil((messagesNumber / ApiConstants.THREAD_MAX_POSTS_PER_PAGE))
            return pagesNumber.toInt()
        }
    }
}