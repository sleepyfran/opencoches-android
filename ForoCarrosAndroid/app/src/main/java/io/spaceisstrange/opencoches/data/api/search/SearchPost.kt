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
import io.spaceisstrange.opencoches.data.api.BasePostRequest
import io.spaceisstrange.opencoches.data.api.transformations.HtmlToSearchResult
import io.spaceisstrange.opencoches.data.model.SearchQuery
import io.spaceisstrange.opencoches.data.model.SearchResult
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class SearchPost(val userId: String, val searchQuery: SearchQuery) : BasePostRequest() {
    /**
     * Retorna un observable para realizar una búsqueda en el foro
     */
    fun observable(): Observable<SearchResult> {
        return Observable.fromCallable({
            search()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Realiza una búsqueda en el foro y retorna los resultados iniciales además de su URL de búsqueda, número de
     * páginas, etc.
     */
    private fun search(): SearchResult {
        val response = super.doRequest()

        // Intentamos utilizar la transformación especificada en la SearchQuery
        // si no existe pues nos volvemos a la de por defecto (HtmlToSearchResult)
        val transformation = searchQuery.transformation?.invoke(response.parse())
                ?: HtmlToSearchResult.transform(response.parse())
        transformation.searchUrl = response.url().toString()
        return transformation
    }

    override fun getPostParameters(): Map<String, String> {
        // Añadimos los valores por defecto a los parámetros pasados
        searchQuery.parameters.putAll(ApiConstants.getSearchParameters(userId))

        return searchQuery.parameters
    }

    override fun getUrl(): String {
        return ApiConstants.SEARCH_URL
    }
}