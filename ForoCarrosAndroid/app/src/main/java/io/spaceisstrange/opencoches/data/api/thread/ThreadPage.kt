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
import io.spaceisstrange.opencoches.data.api.transformations.HtmlToThreadPage
import io.spaceisstrange.opencoches.data.model.Post
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ThreadPage(val link: String, val page: Int?) : BaseGetRequest() {
    /**
     * Retorna un observable para obtener los posts del hilo
     */
    fun observable(): Observable<List<Post>> {
        return Observable.fromCallable({
            getPosts()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Carga el hilo especificado y parsea los posts para poder mostrarlos dentro de nuestra
     * preciosa WebView.
     */
    private fun getPosts(): List<Post> {
        val response = super.doRequest()

        return HtmlToThreadPage.transform(response.parse())
    }

    override fun getUrl(): String {
        var url = ApiConstants.BASE_URL + link

        if (page != null) {
            url += ApiConstants.THREAD_PAGE_URL + page
        }

        return url
    }
}