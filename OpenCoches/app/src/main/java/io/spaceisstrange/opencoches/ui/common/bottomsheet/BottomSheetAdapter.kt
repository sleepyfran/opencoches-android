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

package io.spaceisstrange.opencoches.ui.common.bottomsheet

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.spaceisstrange.opencoches.R
import kotlinx.android.synthetic.main.bottom_sheet_item.view.*

/**
 * Adapter para los diálogos personalizados que mostremos como BottomSheets
 */
class BottomSheetAdapter(val onItemClick: (selectedItem: BottomSheetItem) -> Unit) : RecyclerView.Adapter<BottomSheetAdapter.BottomSheetViewHolder>() {
    /**
     * Items a mostrar en el Bottom Sheet
     */
    var items: MutableList<BottomSheetItem> = mutableListOf()

    fun updateItems(newItems: MutableList<BottomSheetItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BottomSheetViewHolder {
        // Inflate the BottomSheet view and return a holder from it
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.bottom_sheet_item, parent, false)
        return BottomSheetViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class BottomSheetViewHolder(itemView: View, val onItemClick: (selectedItem: BottomSheetItem) -> Unit) :
            RecyclerView.ViewHolder(itemView) {
        /**
         * Item que corresponde a este holder
         */
        lateinit var item: BottomSheetItem

        init {
            // Notify about the click only if it's not null
            itemView.setOnClickListener {
                onItemClick(item)
            }
        }

        /**
         * Pone los datos del item en el Bottom Sheet
         */
        fun bindView(item: BottomSheetItem) {
            // Save the item
            this.item = item

            // Set the image and text
            itemView.bottomSheetIcon.setImageDrawable(ContextCompat.getDrawable(itemView.context, item.drawable))
            itemView.bottomSheetText.text = itemView.context.getString(item.text)
        }
    }
}