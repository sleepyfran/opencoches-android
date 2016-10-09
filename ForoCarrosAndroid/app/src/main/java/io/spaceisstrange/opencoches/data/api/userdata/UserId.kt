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

package io.spaceisstrange.opencoches.data.api.userdata

import io.spaceisstrange.opencoches.data.api.ApiConstants
import io.spaceisstrange.opencoches.data.api.BaseGetRequest
import io.spaceisstrange.opencoches.util.RegexUtil
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class UserId : BaseGetRequest() {
    /**
     * Retorna un observable para obtener el ID de un usuario
     */
    fun observable(): Observable<String> {
        return Observable.fromCallable({
            getId()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Retorna el ID del usuario
     */
    fun getId(): String {
        val response = super.doRequest()
        val parsedResponse = response.parse()

        // Buscamos el enlace a nuestro usuario
        val userLink = parsedResponse.select("a[href^=member.php?u=]").attr("href")
        val userId = RegexUtil.userIdFromLink().matchEntire(userLink)?.groups?.get(1)?.value
                ?: throw IllegalStateException("No se ha podido determinar el ID del usuario")

        return userId
    }

    override fun getUrl(): String {
        return ApiConstants.BASE_URL
    }
}