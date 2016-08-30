package io.spaceisstrange.forocarrosandroid.adapters.holders

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import io.spaceisstrange.forocarrosandroid.R
import io.spaceisstrange.forocarrosandroid.api.model.Post
import io.spaceisstrange.forocarrosandroid.api.net.BaseRequest
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

class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    /**
     * Inserta los datos del post en el holder
     */
    fun bindPost(post: Post) {
        // Cargamos la imagen principal con Glide
        Glide.with(itemView.context)
                .load("http:" + post.posterPictureLink)
                .crossFade()
                .into(itemView.ivPostProfilePic) // TODO: Añadir imagen de error

        itemView.tvPostUsername.text = itemView.context.getString(R.string.post_username_details,
                post.posterUsername, post.posterDescription)

        // Parseamos el HTML para que sea visible (o algo visible al menos) en el TextView
        itemView.tvPostContent.text = Html.fromHtml(post.postText)

        // Hacemos los links clicables
        itemView.tvPostContent.movementMethod = LinkMovementMethod.getInstance()

        // TODO: Cargar imágenes también
    }
}