package io.spaceisstrange.opencoches.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.adapters.holders.PostHolder
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

class PostAdapter : RecyclerView.Adapter<PostHolder>() {
    /**
     * Lista con los posts
     */
    var posts: MutableList<Post> = mutableListOf()

    /**
     * Actualiza la lista de posts y notifica al Adapter sobre los cambios
     */
    fun updatePosts(subs: MutableList<Post>) {
        posts.clear()
        posts.addAll(subs)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PostHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_post, parent, false)
        val holder = PostHolder(view)

        // Espacio a propósito por si en el futuro queremos hacer algo con el holder

        return holder
    }

    override fun onBindViewHolder(holder: PostHolder?, position: Int) {
        holder?.bindPost(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}