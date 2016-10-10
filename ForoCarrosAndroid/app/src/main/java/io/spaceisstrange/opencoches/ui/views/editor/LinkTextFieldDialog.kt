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

package io.spaceisstrange.opencoches.ui.views.editor

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import io.spaceisstrange.opencoches.App
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.bus.Bus
import io.spaceisstrange.opencoches.data.bus.events.LinkAvailableEvent
import kotlinx.android.synthetic.main.dialog_image_link.view.*
import javax.inject.Inject

class LinkTextFieldDialog : DialogFragment() {
    /**
     * Bus donde notificaremos cuando el link esté disponible
     */
    @Inject lateinit var bus: Bus

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_image_link, null, false)

        // Inyectamos el diálogo
        DaggerLinkTextFieldComponent.builder()
                .busComponent((activity.applicationContext as App).busComponent)
                .build()
                .inject(this)

        val dialog = AlertDialog.Builder(activity)
                .setView(view)
                .setTitle(getString(R.string.link_text_field_description))
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create()

        // Sobre-escribimos el botón positivo para que no se oculte el diálogo cuando los datos
        // introducidos son inválidos
        dialog.setOnShowListener {
            val btnOk = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            btnOk.setOnClickListener {
                val link = view.etImageLink.text

                // Comprobamos primero que los datos son válidos y sólo entonces ocultamos el diálogo
                if (!TextUtils.isEmpty(link)) {
                    // Notificamos sobre el link al bus y ocultamos el diálogo
                    bus.publish(LinkAvailableEvent(link.toString()))
                    dismiss()
                } else {
                    // Mostramos un error en caso contrario
                    view.etImageLink.error = getString(R.string.link_text_field_error)
                }
            }
        }

        return dialog
    }
}