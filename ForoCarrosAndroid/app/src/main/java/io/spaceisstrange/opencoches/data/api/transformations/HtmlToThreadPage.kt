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
import io.spaceisstrange.opencoches.data.model.Post
import org.jsoup.nodes.Document

class HtmlToThreadPage {
    companion object {
        /**
         * Retorna una lista de posts del documento especificado
         */
        fun transform(document: Document): List<Post> {
            // Lista donde almacenaremos los posts del hilo
            val postList: MutableList<Post> = mutableListOf()

            // Obtenemos la lista de posts (están todos bajo el div "posts")
            val posts = document.select("table[id^=" + ApiConstants.POST_ROOT_ID_KEY + "]")

            // Iteramos sobre cada post obteniendo su información
            for (post in posts) {
                val userUsername = post.select("a[class=" + ApiConstants.POST_USER_USERNAME_CLASS_KEY + "]").text()
                val userLink = post.select("a[class=" + ApiConstants.POST_USER_USERNAME_CLASS_KEY + "]").attr("href")
                val userIdRegex = "member\\.php\\?u=(\\d+)".toRegex()
                var userId = userIdRegex.matchEntire(userLink)?.groups?.get(1)?.value

                if (userId == null) {
                    userId = ""
                }

                val userPicture = post.select("img[class=" + ApiConstants.POST_USER_IMAGE_CLASS_KEY + "]").attr("src")

                // Hay veces que la info del usuario puede variar de posición si este está baneado
                var userInfo: String
                try {
                    userInfo = post.select("td[class=alt2]")
                            .select("div[class=" + ApiConstants.POST_CONTENT_USER_INFO_KEY + "]")[2].text()
                } catch (e: IndexOutOfBoundsException) {
                    userInfo = post.select("td[class=alt2]")
                            .select("div[class=" + ApiConstants.POST_CONTENT_USER_INFO_KEY + "]")[1].text()
                }

                val postTimestamp = post.select("td[class^=" + ApiConstants.POST_TIMESTAMP_CLASS_KEY + "]").text()
                val postContent = post.select("td[id^=td_post_]").first().html()

                postList.add(Post(userUsername,
                        userPicture,
                        userInfo,
                        userId,
                        postTimestamp,
                        postContent))
            }

            return postList
        }
    }
}