package io.spaceisstrange.opencoches.adapters.holders

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import io.spaceisstrange.opencoches.ui.BottomSheetItem
import io.spaceisstrange.opencoches.utils.Interfaces
import kotlinx.android.synthetic.main.bottom_sheet_item.view.*

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

class BottomSheetViewHolder(itemView: View, val onItemClick: Interfaces.OnBottomSheetItemClick?) :
        RecyclerView.ViewHolder(itemView) {
    /**
     * Item que corresponde a este holder
     */
    lateinit var item: BottomSheetItem

    init {
        // Notify about the click only if it's not null
        itemView.setOnClickListener {
            onItemClick?.onClick(item)
        }
    }

    /**
     * Pone los datos del item en el Bottom Sheet
     */
    fun bindView(item: BottomSheetItem) {
        // Save the item
        this.item = item

        // Set the image and text
        itemView.ivBottomSheetIcon.setImageDrawable(ContextCompat.getDrawable(itemView.context, item.drawable))
        itemView.ivBottomSheetText.text = itemView.context.getString(item.text)
    }
}