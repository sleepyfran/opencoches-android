package io.spaceisstrange.opencoches.adapters.holders

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.api.model.Post
import io.spaceisstrange.opencoches.api.net.ApiConstants
import io.spaceisstrange.opencoches.api.net.BaseRequest
import io.spaceisstrange.opencoches.utils.HtmlUtils
import kotlinx.android.synthetic.main.list_item_post.view.*

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

class PostHolder(itemView: View, val onUserClick: (post: Post) -> Unit) : RecyclerView.ViewHolder(itemView) {

    init {
        // Configuramos el contenido del post
        itemView.wvPostContent.setBackgroundColor(Color.TRANSPARENT)
        itemView.wvPostContent.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        itemView.wvPostContent.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
    }

    /**
     * Inserta los datos del post en el holder
     */
    fun bindPost(post: Post) {
        // Cargamos la imagen principal con Glide
        Glide.with(itemView.context)
                .load("http:" + post.posterPictureLink)
                .crossFade()
                .placeholder(android.R.drawable.progress_horizontal)
                .error(R.drawable.ic_error_black)
                .into(itemView.ivPostProfilePic)

        // Llamamos el onClick con los datos del usuario cuando se presione sobre su foto
        itemView.ivPostProfilePic.setOnClickListener { onUserClick(post) }

        itemView.tvPostUsername.text = itemView.context.getString(R.string.post_username_details,
                post.posterUsername, post.posterDescription)

        itemView.wvPostContent.loadContent(post.postHtml)
    }
}