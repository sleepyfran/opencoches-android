package io.spaceisstrange.opencoches.adapters.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.spaceisstrange.opencoches.api.model.Subforo
import kotlinx.android.synthetic.main.list_item_subforo.view.*

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

class SubforosHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    /**
     * Inserta los datos del subforo en el holder
     */
    fun bindView(subforo: Subforo) {
        itemView.tvSubforoIcon.text = subforo.title[0].toString()
        itemView.tvSubforoTitle.text = subforo.title
    }
}