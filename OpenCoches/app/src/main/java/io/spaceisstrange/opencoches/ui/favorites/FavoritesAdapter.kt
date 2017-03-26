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

package io.spaceisstrange.opencoches.ui.favorites

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.database.model.FavoriteThread
import kotlinx.android.synthetic.main.list_item_favorite_thread_item.view.*

/**
 * Adapter de la lista de hilos favoritos.
 */
class FavoritesAdapter(val onClick: (thread: FavoriteThread) -> Unit,
                       val onLongClick: (thread: FavoriteThread) -> Unit) :
        RecyclerView.Adapter<FavoritesAdapter.ThreadHolder>() {
    /**
     * Lista con los hilos favoritos del usuario.
     */
    var threads: MutableList<FavoriteThread> = mutableListOf()

    /**
     * Actualiza la lista de hilos y notifica al adapter.
     */
    fun update(threads: List<FavoriteThread>) {
        this.threads.clear()
        this.threads.addAll(threads)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ThreadHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_favorite_thread_item, parent, false)
        val holder = ThreadHolder(view)

        holder.itemView.clickable.setOnClickListener {
            onClick(threads[holder.adapterPosition])
        }

        holder.itemView.clickable.setOnLongClickListener {
            onLongClick(threads[holder.adapterPosition])
            true
        }

        return holder
    }

    override fun onBindViewHolder(holder: ThreadHolder, position: Int) {
        holder.bind(threads[position])
    }

    override fun getItemCount(): Int {
        return threads.size
    }

    /**
     * View Holder con la información de un hilo.
     */
    class ThreadHolder(val view: View) : RecyclerView.ViewHolder(view) {
        /**
         * Actualiza el contenido del holder con un nuevo hilo.
         */
        fun bind(thread: FavoriteThread) {
            view.threadTitle.text = thread.title
        }
    }
}