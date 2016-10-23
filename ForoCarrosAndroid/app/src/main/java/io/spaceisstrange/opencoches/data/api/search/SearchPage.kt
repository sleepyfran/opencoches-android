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

package io.spaceisstrange.opencoches.data.api.search

import io.spaceisstrange.opencoches.data.api.ApiConstants
import io.spaceisstrange.opencoches.data.api.BaseGetRequest
import io.spaceisstrange.opencoches.data.api.transformations.HtmlToSearchResult
import io.spaceisstrange.opencoches.data.model.Thread
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class SearchPage(val searchUrl: String, val page: Int) : BaseGetRequest() {
    /**
     * Retorna un observable para realizar una búsqueda en el foro
     */
    fun observable(): Observable<List<Thread>> {
        return Observable.fromCallable({
            getPage()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Obtiene los resultados de la página actual especificada de búsqueda
     */
    private fun getPage(): List<Thread> {
        val response = super.doRequest()

        return HtmlToSearchResult.obtainThreads(response.parse())
    }

    override fun getUrl(): String {
        return searchUrl + ApiConstants.SEARCH_PAGE_URL + page.toString()
    }
}