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

package io.spaceisstrange.opencoches.ui.subforum

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.model.Thread
import kotlinx.android.synthetic.main.list_item_subforum_thread.view.*

class SubforumAdapter(val onClick: (thread: Thread) -> Unit) : RecyclerView.Adapter<SubforumAdapter.SubforumHolder>() {
    /**
     * Lista con los hilos del subforo
     */
    var subforums: MutableList<Thread> = mutableListOf()

    /**
     * Actualiza la lista de hilos y notifica al adapter
     */
    fun update(threads: List<Thread>) {
        subforums.clear()
        subforums.addAll(threads)
        notifyDataSetChanged()
    }

    /**
     * Añade más hilos a la lista y notifica al adapter
     */
    fun addThreads(threads: List<Thread>) {
        subforums.addAll(threads)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SubforumHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_subforum_thread, parent, false)
        val holder = SubforumHolder(view)

        holder.itemView.llClickable.setOnClickListener {
            onClick(subforums[holder.adapterPosition])
        }

        return holder
    }

    override fun onBindViewHolder(holder: SubforumHolder, position: Int) {
        holder.bind(subforums[position])
    }

    override fun getItemCount(): Int {
        return subforums.size
    }

    /**
     * View Holder con la información de un subforo
     */
    class SubforumHolder(val view: View) : RecyclerView.ViewHolder(view) {
        /**
         * Actualiza el contenido del holder con un nuevo subforo
         */
        fun bind(thread: Thread) {
            if (thread.isSticky) {
                view.ivSubforoThreadSticky.visibility = View.VISIBLE
            } else {
                view.ivSubforoThreadSticky.visibility = View.GONE
            }

            view.tvSubforoThreadTitle.text = thread.title
            view.tvSubforoThreadPreview.text = thread.preview
        }
    }
}