package io.spaceisstrange.forocarrosandroid.adapters.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.spaceisstrange.forocarrosandroid.api.model.SubforoThread
import kotlinx.android.synthetic.main.list_item_subforo_thread.view.*

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

class SubforoThreadHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    /**
     * Inserta los datos del hilo en el holder
     */
    fun bindView(thread: SubforoThread) {
        itemView.tvSubforoThreadTitle.text = thread.title
        itemView.tvSubforoThreadPreview.text = thread.preview

        if (thread.isSticky) itemView.ivSubforoThreadSticky.visibility = View.VISIBLE
        else itemView.ivSubforoThreadSticky.visibility = View.GONE
    }
}