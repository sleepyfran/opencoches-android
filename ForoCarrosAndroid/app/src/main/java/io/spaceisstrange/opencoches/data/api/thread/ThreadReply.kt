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
import io.spaceisstrange.opencoches.data.api.BasePostRequest
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ThreadReply(val securityToken: String, val threadId: String, val reply: String, val userId: String) : BasePostRequest() {
    /**
     * Retorna un observable para enviar la respuesta al hilo
     */
    fun observable(): Observable<Boolean> {
        return Observable.fromCallable({
            reply()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Envía el mensaje especificado como respuesta al hilo especificado
     */
    fun reply(): Boolean {
        val response = super.doRequest()
        return isSuccessful(response.statusCode())
    }

    override fun getPostParameters(): Map<String, String> {
        return ApiConstants.getThreadReplyParameters(securityToken, threadId, reply, userId)
    }

    override fun getUrl(): String {
        // Obtenemos el ID del hilo de la URL
        return ApiConstants.BASE_URL + ApiConstants.REPLY_URL + ApiConstants.DO_URL + "postreply" + ApiConstants.T_URL + threadId
    }
}