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

package io.spaceisstrange.opencoches.ui.views.editor

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import io.spaceisstrange.opencoches.R
import kotlinx.android.synthetic.main.dialog_text_field.view.*

/**
 * Diálogo personalizado que muestra un simple campo de texto.
 */
class LinkTextFieldDialog : DialogFragment() {
    /**
     * Método a llamar cuando tengamos el texto disponible.
     */
    lateinit var onTextAvailable: (text: String) -> Unit

    /**
     * Método a llamar para comprobar que el texto sea correcto.
     */
    lateinit var checkText: (text: String) -> Boolean

    companion object {
        /**
         * Crea una nueva instancia con los parámetros necesarios para el dialog.
         */
        fun instantiate(onLinkAvailable: (link: String) -> Unit,
                        checkText: (text: String) -> Boolean): LinkTextFieldDialog {
            val dialog = LinkTextFieldDialog()
            dialog.onTextAvailable = onLinkAvailable
            dialog.checkText = checkText
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_text_field, null, false)

        val dialog = AlertDialog.Builder(activity!!)
                .setView(view)
                .setTitle(getString(R.string.link_text_field_description))
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, { dialog, something -> })
                .create()

        // Sobre-escribimos el botón positivo para que no se oculte el diálogo cuando los datos
        // introducidos son inválidos
        dialog.setOnShowListener {
            val btnOk = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            btnOk.setOnClickListener {
                val text = view.textField.text.toString()

                // Comprobamos primero que los datos son válidos y sólo entonces ocultamos el diálogo
                if (!TextUtils.isEmpty(text) && checkText.invoke(text)) {
                    // Notificamos sobre el texto y ocultamos el diálogo
                    onTextAvailable(text)
                    dismiss()
                } else {
                    // Mostramos un error en caso contrario
                    view.textField.error = getString(R.string.link_text_field_error)
                }
            }
        }

        return dialog
    }
}