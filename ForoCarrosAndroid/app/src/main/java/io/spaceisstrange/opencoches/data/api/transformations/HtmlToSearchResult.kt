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

package io.spaceisstrange.opencoches.data.api.transformations

import io.spaceisstrange.opencoches.data.api.ApiConstants
import io.spaceisstrange.opencoches.data.model.SearchResult
import io.spaceisstrange.opencoches.data.model.Thread
import org.jsoup.nodes.Document

class HtmlToSearchResult {
    companion object {
        /**
         * Retorna una lista con los hilos de la búsqueda actual
         */
        fun obtainThreads(document: Document): List<Thread> {
            val threadList: MutableList<Thread> = mutableListOf()

            // Obtenemos los links de los hilos
            val threadLinks = document.select("a[id^=" + ApiConstants.THREAD_TITLE_KEY + "]")

            // Obtenemos los títulos y los links de los hilos
            for (thread in threadLinks) {
                val threadTitle = thread.text()
                val threadLink = thread.attr("href")

                threadList.add(Thread(threadTitle, threadLink))
            }

            return threadList
        }

        /**
         * Retorna el resultado inicial de una búsqueda en ForoCoches: resultado inicial y número de páginas
         */
        fun transform(document: Document): SearchResult {
            // Obtenemos las páginas de la búsqueda
            val pages = HtmlToPages.transform(document)

            return SearchResult(pages, obtainThreads(document))
        }
    }
}