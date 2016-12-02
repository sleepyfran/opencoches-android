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
import io.spaceisstrange.opencoches.data.model.NewThreadParameters
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class NewThread(val title: String,
                val body: String,
                val subforumId: String,
                val newThreadInfo: NewThreadParameters,
                val userId: String) : BasePostRequest() {
    /**
     * Retorna un observable para enviar la respuesta al hilo
     */
    fun observable(): Observable<String> {
        return Observable.fromCallable({
            create()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Crea un nuevo hilo con el título y cuerpo especificado
     */
    private fun create(): String {
        val response = super.doRequest()
        return response.url().toString()
    }

    override fun getPostParameters(): Map<String, String> {
        return ApiConstants.getNewThreadParameters(title,
                body,
                newThreadInfo.securityToken,
                subforumId,
                newThreadInfo.postHash,
                newThreadInfo.postStartTime,
                userId)
    }

    override fun getUrl(): String {
        return ApiConstants.BASE_URL + ApiConstants.NEW_THREAD_POST_THREAD_URL + ApiConstants.F_URL + subforumId
    }
}