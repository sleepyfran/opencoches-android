package io.spaceisstrange.opencoches.api.net

import io.spaceisstrange.opencoches.api.model.Post

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

class FCThread(val threadLink: String) : BaseGetRequest() {

    fun getThreadPosts(): MutableList<Post> {
        val response = super.doRequest()
        val document = response.parse()

        // Lista donde almacenaremos los posts del hilo
        val postList: MutableList<Post> = mutableListOf()

        // Obtenemos la lista de posts (están todos bajo el div "posts")
        val posts = document.select("table[id^=" + ApiConstants.POST_ROOT_ID_KEY + "]")

        // Iteramos sobre cada post obteniendo su información
        for ((index, post) in posts.withIndex()) {
            val userUsername = post.select("a[class=" + ApiConstants.POST_USER_USERNAME_CLASS_KEY + "]").text()
            val userLink = post.select("a[class=" + ApiConstants.POST_USER_USERNAME_CLASS_KEY + "]").attr("href")
            val userPicture = post.select("img[class=" + ApiConstants.POST_USER_IMAGE_CLASS_KEY + "]").attr("src")

            // Hay veces que la info del usuario puede variar de posición si este está baneado
            var userInfo: String
            try {
                userInfo = post.select("td[class=alt2]").select("div[class=smallfont]")[2].text()
            } catch (e: IndexOutOfBoundsException) {
                userInfo = post.select("td[class=alt2]").select("div[class=smallfont]")[1].text()
            }

            val postTimestamp = post.select("td[class^=" + ApiConstants.POST_TIMESTAMP_CLASS_KEY + "]").text()
            val postTitle = post.select("td[id^=td_post_]").select("div[class=smallfont]").html()
            var postContent = post.select("td[id^=td_post_]").html()

            // Si es el OP, retiramos el título del mensaje
            // Sí, esto es súper chustero, pero no encontré otra forma de sacar el texto del post.
            // Si a alguien se le ocurre una mejor forma los pull requests están abiertos ;)
            if (index == 0) {
                postContent = postContent.slice(postTitle.length..postContent.length - 1)
                // TODO: Mejorar el recorte del título (o quitarlo mejor)
            }

            postList.add(Post(userUsername, userPicture, userInfo, userLink, postTimestamp, postContent))
        }

        return postList
    }

    override fun getUrl(): String {
        return ApiConstants.BASE_URL + threadLink
    }
}