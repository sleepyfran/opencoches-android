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

package io.spaceisstrange.opencoches.data.api.subforum

import io.spaceisstrange.opencoches.data.api.ApiConstants
import io.spaceisstrange.opencoches.data.api.BaseGetRequest
import io.spaceisstrange.opencoches.data.model.Thread
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class Subforum(val link: String, val page: Int? = null) : BaseGetRequest() {
    /**
     * Retorna un observable para obtener el subforo especificado
     */
    fun getContentObservable(): Observable<List<Thread>> {
        return Observable.fromCallable({
            getContent()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Obtiene el contenido de un subforo especificado por la key de este (ejemplo: 2 para general)
     */
    private fun getContent(): List<Thread> {
        val response = super.doRequest()
        val document = response.parse()

        // Lista donde almacenaremos cada hilo
        val threadList: MutableList<Thread> = mutableListOf()

        // Obtenemos los td del documento. Cada hilo está contenido en una td
        val threads = document.select("td[id^=" + ApiConstants.TD_THREAD_TITLE_KEY + "]")

        // Iteramos sobre cada uno de los hilos obteniendo el contenido
        for (thread in threads) {
            var isSticky = false
            val threadPreview = thread.attr("title")
            val threadInfo = thread.select("a[id^=" + ApiConstants.THREAD_TITLE_KEY + "]")[0]
            val threadTitle = threadInfo.text()
            val threadLink = threadInfo.attr("href")

            // Comprobamos si contiene "chincheta" por alguna parte
            if (thread.getElementsContainingText("Chincheta").size > 0) {
                isSticky = true
            }

            // Añadimos el nuevo thread a la lista
            threadList.add(Thread(threadTitle, threadPreview, threadLink, isSticky))
        }

        return threadList
    }

    override fun getUrl(): String {
        val baseUrl = ApiConstants.BASE_URL + link

        // Si la variable page no es null entonces es que queremos cargar otra página
        if (page != null) return baseUrl + ApiConstants.SUBFORO_PAGE_URL + page.toString()
        else return baseUrl
    }
}