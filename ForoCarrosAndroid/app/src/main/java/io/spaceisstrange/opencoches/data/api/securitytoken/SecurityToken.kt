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

package io.spaceisstrange.opencoches.data.api.securitytoken

import io.spaceisstrange.opencoches.data.api.ApiConstants
import io.spaceisstrange.opencoches.data.api.BaseGetRequest
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class SecurityToken(val link: String) : BaseGetRequest() {
    /**
     * Retorna un observable para obtener el security token de una página
     */
    fun observable(): Observable<String> {
        return Observable.fromCallable({
            getToken()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Busca en la página del link solicitado el security token asociado a dicha página. Necesitamos
     * este token para enviar respuestas a hilos, mensajes privados, etc.
     */
    fun getToken(): String {
        val response = super.doRequest()
        val document = response.parse()

        // Obtenemos el security token del input actual
        val input = document.select("input[name=" + ApiConstants.SECURITY_TOKEN_KEY + "]").first()
        val securityToken = input.attr("value")

        // Si está vacío es que probablemente no haya ningún security token en la página
        if (securityToken == "") {
            throw IllegalStateException("No se puede obtener el token de una página que no lo tiene")
        }

        return securityToken
    }

    override fun getUrl(): String {
        return ApiConstants.BASE_URL + link
    }
}