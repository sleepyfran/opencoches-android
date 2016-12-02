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

package io.spaceisstrange.opencoches.util

class RegexUtils {
    companion object {
        /**
         * Retorna un Regex que saca el ID de una URL de un hilo
         */
        fun threadIdFromLink(): Regex {
            return "showthread\\.php\\?t=(\\d+)".toRegex()
        }

        /**
         * Retorna un Regex que saca el ID de un usuario de su link
         */
        fun userIdFromLink(): Regex {
            return "member\\.php\\?u=(\\d+)".toRegex()
        }

        /**
         * Retorna un Regex que saca el ID de un usuario de su link completo de escritorio
         */
        fun userIdFromFullDesktopLink(): Regex {
            return "http://www\\.forocoches\\.com/foro/member\\.php\\?u=(\\d+)".toRegex()
        }

        /**
         * Retorna un Regex que saca el ID de un usuario de su link completo de móvil
         */
        fun userIdFromFullMobileLink(): Regex {
            return "http://m\\.forocoches\\.com/foro/member\\.php\\?u=(\\d+)".toRegex()
        }

        /**
         * Retorna un Regex que saca el ID de un subforo de su link
         */
        fun subforumIdFromLink(): Regex {
            return "forumdisplay\\.php\\?f=(\\d+)".toRegex()
        }

        /**
         * Retorna un Regex que selecciona las páginas de un hilo
         */
        fun getPagesRegex(): Regex {
            return "Pág \\d de (\\d+)".toRegex()
        }
    }
}