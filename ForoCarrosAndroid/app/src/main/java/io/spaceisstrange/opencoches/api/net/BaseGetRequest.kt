package io.spaceisstrange.opencoches.api.net

import android.util.Log
import org.jsoup.Connection
import org.jsoup.Jsoup

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

abstract class BaseGetRequest : BaseRequest() {
    /**
     * Realiza la petición con la URL indicada en la subclase y los parámetros por defecto
     */
    override fun doRequest(): Connection.Response {
        val response = Jsoup.connect(getUrl())
                .userAgent(USER_AGENT)
                .timeout(TIMEOUT)
                .cookies(CookiesCache.cookies)
                .method(Connection.Method.GET)
                .execute()

        // Guardamos las cookies si contiene alguna más de la que ya tenemos
        val actualCookies = CookiesCache.cookies

        if (actualCookies == null || response.cookies().size >= actualCookies.size) {
            CookiesCache.cookies = response.cookies()
        }

        // Al log las cookies
        Log.i("Cookies ${javaClass.simpleName}", actualCookies?.size.toString())

        return response
    }
}