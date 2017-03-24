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

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import io.spaceisstrange.opencoches.R
import kotlinx.android.synthetic.main.bottom_sheet_layout.view.*

/**
 * Diálogo personalizado para mostrar un BottomSheet.
 */
abstract class BottomSheetDialog : BottomSheetDialogFragment() {
    /**
     * Adapter del diálogo.
     */
    val adapter = BottomSheetAdapter({
        selectedItem ->

        onClick(selectedItem)
    })

    /**
     * Behavior del diálogo.
     */
    lateinit var behavior: BottomSheetBehavior<View>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val view = View.inflate(context, R.layout.bottom_sheet_layout, null)

        // Configuramos el contenido
        view.bottomSheetContent.setHasFixedSize(true)
        view.bottomSheetContent.layoutManager = LinearLayoutManager(context)
        adapter.updateItems(getItems())
        view.bottomSheetContent.adapter = adapter

        // Configuramos el título
        if (getTitle() != null) {
            view.bottomSheetTitleWrapper.visibility = View.VISIBLE
            view.bottomSheetTitle.text = getTitle()
        } else {
            view.bottomSheetTitleWrapper.visibility = View.GONE
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
     * A implementar por la subclase para obtener el item seleccionado.
     */
    open fun onClick(selectedItem: BottomSheetItem) {
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    /**
     * Método a llamar cuando se necesitan los items del diálogo.
     */
    abstract fun getItems(): MutableList<BottomSheetItem>

    /**
     * Método a llamar cuando se necesite el título del diálogo. Si se retorna null se ocultará.
     */
    abstract fun getTitle(): String?
}