package io.spaceisstrange.opencoches.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.adapters.holders.SubforosHolder
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

class SubforosAdapter : RecyclerView.Adapter<SubforosHolder>() {
    /**
     * Lista con los subforos
     */
    var subforos: MutableList<Subforo> = mutableListOf()

    /**
     * Método a llamar cuando el usuario hace click en un subforo
     */
    var onClick: ((subforo: Subforo) -> Unit)? = null

    /**
     * Actualiza la lista de subforos y notifica al Adapter sobre los cambios
     */
    fun updateSubforos(subs: MutableList<Subforo>) {
        subforos.clear()
        subforos.addAll(subs)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SubforosHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_subforo, parent, false)
        val holder = SubforosHolder(view)

        // Invocamos el onClick (si hay alguno definido) con el subforo que el usuario ha pulsado
        holder.itemView.llClickable.setOnClickListener {
            onClick?.invoke(subforos[holder.adapterPosition])
        }

        return holder
    }

    override fun onBindViewHolder(holder: SubforosHolder?, position: Int) {
        holder?.bindView(subforos[position])
    }

    override fun getItemCount(): Int {
        return subforos.size
    }
}