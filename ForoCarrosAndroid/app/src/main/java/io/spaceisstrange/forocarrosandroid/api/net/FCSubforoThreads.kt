package io.spaceisstrange.forocarrosandroid.api.net

import io.spaceisstrange.forocarrosandroid.api.model.SubforoThread

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

class FCSubforoThreads(val subforoLink: String, val page: Int? = null) : BaseGetRequest() {

    /**
     * Obtiene el contenido de un subforo especificado por la key de este (ejemplo: 2 para general)
     */
    fun getThreads(): MutableList<SubforoThread> {
        val response = super.doRequest()
        val document = response.parse()

        // Lista donde almacenaremos cada hilo
        val threadList: MutableList<SubforoThread> = mutableListOf()

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
            threadList.add(SubforoThread(threadTitle, threadPreview, threadLink, isSticky))
        }

        return threadList
    }

    override fun getUrl(): String {
        val baseUrl = ApiConstants.BASE_URL + subforoLink

        // Si la variable page no es null entonces es que queremos cargar otra página
        if (page != null) return baseUrl + ApiConstants.THREAD_PAGE_KEY + page.toString()
        else return baseUrl
    }
}