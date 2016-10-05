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

package io.spaceisstrange.opencoches.ui.thread

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import com.bumptech.glide.Glide
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.model.Post
import kotlinx.android.synthetic.main.list_item_post.view.*

class ThreadAdapter : RecyclerView.Adapter<ThreadAdapter.Holder>() {
    /**
     * Lista con los posts
     */
    var posts: MutableList<Post> = mutableListOf()

    /**
     * Callback al pulsar sobre la imagen del usuario
     */
    lateinit var onUserClick: (post: Post) -> Unit

    /**
     * Actualiza la lista de posts y notifica al Adapter sobre los cambios
     */
    fun updatePosts(subs: List<Post>) {
        posts.clear()
        posts.addAll(subs)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_post, parent, false)
        val holder = Holder(view, onUserClick)

        // Espacio a propósito por si en el futuro queremos hacer algo con el holder

        return holder
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindPost(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class Holder(val view: View, val onUserClick: (post: Post) -> Unit) : RecyclerView.ViewHolder(view) {
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
                    .error(R.drawable.ic_error_white)
                    .into(itemView.ivPostProfilePic)

            // Llamamos el onClick con los datos del usuario cuando se presione sobre su foto
            itemView.ivPostProfilePic.setOnClickListener { onUserClick(post) }

            itemView.tvPostUsername.text = itemView.context.getString(R.string.post_username_details,
                    post.posterUsername, post.posterDescription)

            itemView.wvPostContent.loadContent(post.postHtml)
        }
    }
}