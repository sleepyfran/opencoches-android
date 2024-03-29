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

import io.spaceisstrange.opencoches.data.CookiesCache
import org.jsoup.Connection
import org.jsoup.Jsoup

/**
 * Clase base para todas las peticiones POST a ForoCoches.
 */
abstract class BasePostRequest : BaseRequest() {
    /**
     * Realiza la petición POST con la URL indicada en la subclase y los parámetros por defecto.
     */
    override fun doRequest(): Connection.Response {
        val connection = Jsoup.connect(getUrl())
                .data(getPostParameters())
                .userAgent(USER_AGENT)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .postDataCharset("ISO-8859-1")
                .timeout(TIMEOUT)
                .method(Connection.Method.POST)

        val actualCookies = CookiesCache.cookies

        if (actualCookies != null && actualCookies.isNotEmpty()) {
            connection.cookies(CookiesCache.cookies)
        }

        val response = connection.execute()

        // Guardamos las cookies si contiene alguna más de la que ya tenemos
        if (actualCookies == null || response.cookies().size >= actualCookies.size) {
            CookiesCache.cookies = response.cookies()
        }

        return response
    }

    /**
     * Método a llamar si se necesitan parámetros para la operación POST (que es lo más normal).
     */
    abstract fun getPostParameters(): Map<String, String>
}