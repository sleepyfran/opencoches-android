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

package io.spaceisstrange.opencoches.data.api

import io.spaceisstrange.opencoches.util.RegexUtil

/**
 * Clase con utilidades que se utilizan a lo largo de la aplicación referentes a la API.
 */
class ApiUtils {
    companion object {
        /**
         * Devuelve una URL "real" de una "fake". Básicamente en ciertas partes FC nos devuelve una URL comenando
         * por // en lugar de por http://, así que tenemos que lidiar con eso.
         */
        fun getRealUrl(fakeUrl: String): String {
            if (fakeUrl.startsWith("http:")) {
                return fakeUrl
            }

            return "http:" + fakeUrl
        }

        /**
         * Remueve el prefijo http://... de las URLs para aquellas partes de la API que lo necesiten.
         */
        fun removePrefixFromUrl(url: String): String {
            if (url.startsWith(ApiConstants.BASE_URL)) {
                return url.removePrefix(ApiConstants.BASE_URL)
            } else if (url.startsWith(ApiConstants.BASE_MOBILE_URL)) {
                return url.removePrefix(ApiConstants.BASE_MOBILE_URL)
            } else {
                return url
            }
        }

        /**
         * Obtiene el ID de un usuario de una URL completa (sea de escritorio o móvil).
         */
        fun getIdFromUrl(url: String): String? {
            if (url.startsWith("http://www.")) {
                return RegexUtil.userIdFromFullDesktopLink().matchEntire(url)?.groups?.get(1)?.value!!
            } else if (url.startsWith("http://m.")) {
                return RegexUtil.userIdFromFullMobileLink().matchEntire(url)?.groups?.get(1)?.value!!
            }

            return null
        }

        /**
         * Retorna una URL completa de escritorio dado un "endpoint" de ForoCoches.
         */
        fun getUrlFromEndpoint(endpoint: String): String {
            return ApiConstants.BASE_URL + endpoint
        }
    }
}