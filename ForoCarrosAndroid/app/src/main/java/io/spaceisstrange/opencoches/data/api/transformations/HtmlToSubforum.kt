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
import io.spaceisstrange.opencoches.data.model.Thread
import org.jsoup.nodes.Document

class HtmlToSubforum {
    companion object {
        /**
         * Retorna la lista de hilos del subforo especificado
         */
        fun transform(document: Document): List<Thread> {
            // Lista donde almacenaremos cada hilo
            val threadList: MutableList<Thread> = mutableListOf()

            // Obtenemos primero los hilos con chincheta
            try {
                val pinnedThreadsBody = document.select("tbody[id^=collapseobj_st_]")[0]
                val pinnedThreads = pinnedThreadsBody.select("tr")

                // Eliminamos los que no nos interesan
                pinnedThreads.removeAt(pinnedThreads.size - 1)

                for (thread in pinnedThreads) {
                    val isSticky = true
                    val threadInfo = thread.select("a[id^=" + ApiConstants.THREAD_TITLE_KEY + "]")[0]
                    val threadPages = thread.select("a[href^=" + ApiConstants.THREAD_PAGES_KEY + "]")[0].text()
                    val threadTitle = threadInfo.text()
                    val threadLink = threadInfo.attr("href")

                    // Añadimos el nuevo thread a la lista
                    threadList.add(Thread(threadTitle,
                            threadLink,
                            Thread.pagesFromMessages(threadPages),
                            isSticky))
                }
            } catch (exception: IndexOutOfBoundsException) {
                // Hay algunos subforos que no tienen temas con chincheta, así que a silenciar se ha dicho
            }

            // Y después los que no tienen chincheta
            val nonPinnedThreadsBody = document.select("tbody[id^=threadbits_forum_]")[0]
            val nonPinnedThreads = nonPinnedThreadsBody.select("tr")

            // Iteramos sobre cada uno de los hilos obteniendo el contenido
            for (thread in nonPinnedThreads) {
                val isSticky = false
                val threadInfo = thread.select("a[id^=" + ApiConstants.THREAD_TITLE_KEY + "]")[0]
                val threadPages = thread.select("a[href^=" + ApiConstants.THREAD_PAGES_KEY + "]")[0].text()
                val threadTitle = threadInfo.text()
                val threadLink = threadInfo.attr("href")

                // Añadimos el nuevo thread a la lista
                threadList.add(Thread(threadTitle,
                        threadLink,
                        Thread.pagesFromMessages(threadPages),
                        isSticky))
            }

            return threadList
        }
    }
}