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

package io.spaceisstrange.opencoches.data.api.thread

import io.spaceisstrange.opencoches.data.api.ApiConstants
import io.spaceisstrange.opencoches.data.api.BaseGetRequest
import io.spaceisstrange.opencoches.data.api.transformations.HtmlToPostHash
import io.spaceisstrange.opencoches.data.api.transformations.HtmlToPostStartTime
import io.spaceisstrange.opencoches.data.api.transformations.HtmlToSecurityToken
import io.spaceisstrange.opencoches.data.model.NewThreadParameters
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class NewThreadInfo(val subforumId: String) : BaseGetRequest() {
    /**
     * Retorna un observable para obtener la información necesaria para enviar un nuevo hilo
     */
    fun observable(): Observable<NewThreadParameters> {
        return Observable.fromCallable({
            getInfo()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Busca en la página la información necesaria (y escondida) para poder mandar un nuevo hilo
     */
    fun getInfo(): NewThreadParameters {
        val response = super.doRequest()
        val parsedResponse = response.parse()

        // Obtenemos los datos de la respuesta
        val securityToken = HtmlToSecurityToken.transform(parsedResponse)
        val postHash = HtmlToPostHash.transform(parsedResponse)
        val postStartTime = HtmlToPostStartTime.transform(parsedResponse)

        return NewThreadParameters(securityToken, postHash, postStartTime)
    }

    override fun getUrl(): String {
        return ApiConstants.BASE_URL + ApiConstants.NEW_THREAD_NEW_THREAD_URL + ApiConstants.F_URL + subforumId
    }
}