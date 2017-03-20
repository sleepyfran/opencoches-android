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

package io.spaceisstrange.opencoches.data.api.subforum

import io.spaceisstrange.opencoches.data.api.ApiConstants
import io.spaceisstrange.opencoches.data.api.BaseGetRequest
import io.spaceisstrange.opencoches.data.api.transformations.HtmlToSubforum
import io.spaceisstrange.opencoches.data.model.Thread
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Clase para obtener los hilos de un subforo de ForoCoches.
 */
class Subforum(val link: String, val page: Int = 1) : BaseGetRequest() {
    /**
     * Retorna un observable para obtener el subforo especificado.
     */
    fun observable(): Observable<List<Thread>> {
        return Observable.fromCallable({
            getContent()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Obtiene el contenido de un subforo especificado por el link del mismo.
     */
    private fun getContent(): List<Thread> {
        val response = super.doRequest()

        return HtmlToSubforum.transform(response.parse())
    }

    override fun getUrl(): String {
        return ApiConstants.BASE_URL + link + ApiConstants.SUBFORUM_PAGE_URL + page.toString()
    }
}