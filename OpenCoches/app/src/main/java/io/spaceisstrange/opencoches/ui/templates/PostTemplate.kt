/*
 * Made with <3 by Fran González (@spaceisstrange)
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

/**
 * Plantilla para cada post mostrado en los hilos.
 */
class PostTemplate(context: Context) : HtmlTemplate<Post>(context, "post_template.html") {
    /**
     * Dependencias utilizadas.
     */
    val postActionsTemplate = PostActionsTemplate(context)

    override fun render(content: Post, template: Phrase): String {
        return template
                .put("picture_src", content.posterPictureLink)
                .put("poster_username", content.posterUsername)
                .put("poster_id", content.userId)
                .put("post_date", content.postTimestamp)
                .put("content", content.postHtml)
                .put("post_actions", postActionsTemplate.render(content))
                .format()
                .toString()
    }
}