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

package io.spaceisstrange.opencoches.ui.templates

import android.content.Context
import com.squareup.phrase.Phrase
import io.spaceisstrange.opencoches.data.model.Post

class PostsTemplate(context: Context) : HtmlTemplate<List<Post>>(context, "post_list.html") {
    /**
     * Dependencias utilizadas
     */
    val postTemplate = PostTemplate(context)

    override fun render(content: List<Post>, template: Phrase): String {
        val buffer = StringBuilder()

        for (post in content) {
            buffer.append(postTemplate.render(post))
        }

        // Añadimos el JS y CSS de los posts
        return template
                .put("js", readFromAssets("open_coches_script.js"))
                .put("css", readFromAssets("post_styles.css"))
                .put("posts", buffer)
                .format()
                .toString()
    }
}