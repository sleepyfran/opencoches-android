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

package io.spaceisstrange.opencoches.ui.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.model.Thread
import kotlinx.android.synthetic.main.list_item_subforum_thread.view.*

/**
 * Adapter de los resultados de búsqueda.
 */
class SearchAdapter(val onClick: (thread: Thread) -> Unit) : RecyclerView.Adapter<SearchAdapter.SearchHolder>() {
    /**
     * Lista con los hilos de la búsqueda.
     */
    var threads: MutableList<Thread> = mutableListOf()

    /**
     * Actualiza la lista de hilos y notifica al adapter.
     */
    fun update(threads: List<Thread>) {
        this.threads.clear()
        this.threads.addAll(threads)
        notifyDataSetChanged()
    }

    /**
     * Añade más contenido al adapter.
     */
    fun add(threads: List<Thread>) {
        this.threads.addAll(threads)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SearchHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_search_result, parent, false)
        val holder = SearchHolder(view)

        holder.itemView.clickable.setOnClickListener {
            onClick(threads[holder.adapterPosition])
        }

        return holder
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        holder.bind(threads[position])
    }

    override fun getItemCount(): Int {
        return threads.size
    }

    /**
     * View Holder con la información de un hilo.
     */
    class SearchHolder(val view: View) : RecyclerView.ViewHolder(view) {
        /**
         * Actualiza el contenido del holder con un nuevo hilo.
         */
        fun bind(thread: Thread) {
            view.threadTitle.text = thread.title
        }
    }
}