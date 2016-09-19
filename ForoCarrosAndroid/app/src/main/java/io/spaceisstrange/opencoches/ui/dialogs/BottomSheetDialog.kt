package io.spaceisstrange.opencoches.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.adapters.BottomSheetAdapter
import io.spaceisstrange.opencoches.ui.BottomSheetItem
import io.spaceisstrange.opencoches.utils.Interfaces
import kotlinx.android.synthetic.main.bottom_sheet_layout.view.*

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

abstract class BottomSheetDialog : BottomSheetDialogFragment(), Interfaces.OnBottomSheetItemClick {
    /**
     * Adapter del diálogo
     */
    val adapter = BottomSheetAdapter(this)

    /**
     * Behavior del diálogo
     */
    lateinit var behavior: BottomSheetBehavior<View>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val view = View.inflate(context, R.layout.bottom_sheet_layout, null)

        // Configuramos el contenido
        view.rvBottomSheetContent.setHasFixedSize(true)
        view.rvBottomSheetContent.layoutManager = LinearLayoutManager(context)
        adapter.updateItems(getItems())
        view.rvBottomSheetContent.adapter = adapter

        // Configuramos el título
        if (getTitle() != null) {
            view.flBottomSheetTitleWrapper.visibility = View.VISIBLE
            view.tvBottomSheetTitle.text = getTitle()
        } else {
            view.flBottomSheetTitleWrapper.visibility = View.GONE
        }

        dialog.setContentView(view)
        behavior = BottomSheetBehavior.from(view.parent as View)

        return dialog
    }

    override fun onStart() {
        super.onStart()
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    /**
     * A implementar por la subclase para obtener el item seleccionado
     */
    override fun onClick(selectedItem: BottomSheetItem) {
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    /**
     * Método a llamar cuando se necesitan los items del diálogo
     */
    abstract fun getItems(): MutableList<BottomSheetItem>

    /**
     * Método a llamar cuando se necesite el título del diálogo. Si se retorna null se ocultará
     */
    abstract fun getTitle(): String?
}