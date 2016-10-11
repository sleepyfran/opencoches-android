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

package io.spaceisstrange.opencoches.ui.views.editor.imageupload

import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.ui.common.bottomsheet.BottomSheetDialog
import io.spaceisstrange.opencoches.ui.common.bottomsheet.BottomSheetItem
import io.spaceisstrange.opencoches.ui.views.editor.LinkTextFieldDialog

class ImageSelectionDialog : BottomSheetDialog() {
    /**
     * Método a llamar cuando tengamos un link disponible
     */
    lateinit var onLinkAvailable: (link: String) -> Unit

    companion object {
        /**
         * Crea una nueva instancia con los parámetros necesarios para el dialog
         */
        fun newInstance(onLinkAvailable: (link: String) -> Unit): ImageSelectionDialog {
            val dialog = ImageSelectionDialog()
            dialog.onLinkAvailable = onLinkAvailable
            return dialog
        }
    }

    override fun onClick(selectedItem: BottomSheetItem) {
        super.onClick(selectedItem)

        if (selectedItem.drawable == R.drawable.ic_image_white) {
            // TODO: Mostrar la selección de fotos desde la galería y subirlas a algún proveedor (¿Imgur?)
        } else if (selectedItem.drawable == R.drawable.ic_link_white) {
            // Mostramos el diálogo con introducción del link
            LinkTextFieldDialog.newInstance {
                link ->

                onLinkAvailable(link)
            }.show(activity.supportFragmentManager, null)
        }
    }

    override fun getTitle(): String? {
        return getString(R.string.image_upload_source)
    }

    override fun getItems(): MutableList<BottomSheetItem> {
        return mutableListOf(
                BottomSheetItem(R.drawable.ic_image_white, R.string.image_upload_gallery),
                BottomSheetItem(R.drawable.ic_link_white, R.string.image_upload_link)
        )
    }
}