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

package io.spaceisstrange.opencoches.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.api.ApiConstants
import kotlinx.android.synthetic.main.view_editor.view.*

class EditorView : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_editor, this, true)

        // Especificamos las opciones de los botones de la barra de herramientas
        btnEditorB.setOnClickListener {
            // Añadimos la etiqueta B
            appendTag(ApiConstants.EDITOR_B_TAG, ApiConstants.EDITOR_B_CLOSE_TAG)
        }

        btnEditorI.setOnClickListener {
            // Añadimos la etiqueta I
            appendTag(ApiConstants.EDITOR_I_TAG, ApiConstants.EDITOR_I_CLOSE_TAG)
        }

        btnEditorU.setOnClickListener {
            // Añadimos la etiqueta U
            appendTag(ApiConstants.EDITOR_U_TAG, ApiConstants.EDITOR_U_CLOSE_TAG)
        }

        btnEditorImg.setOnClickListener {
            // TODO: Cargar imagen y subirla a algún proveedor
            appendTag(ApiConstants.EDITOR_IMG_TAG, ApiConstants.EDITOR_IMG_CLOSE_TAG)
        }

        btnEditorVid.setOnClickListener {
            // TODO: Mostrar un diálogo preguntando por la URL del vídeo
            appendTag(ApiConstants.EDITOR_VID_TAG, ApiConstants.EDITOR_VID_CLOSE_TAG)
        }
    }

    /**
     * Añade la etiqueta especificada al campo de texto
     */
    private fun appendTag(tag: String, closingTag: String) {
        etEditorText.append(tag)
        etEditorText.append(closingTag)
        etEditorText.setSelection(etEditorText.length() - closingTag.length)
    }

    /**
     * Retorna el texto introducido en el campo de texto del editor
     */
    fun text(): String {
        return etEditorText.text.toString()
    }

    /**
     * Muestra un error en el campo de texto
     */
    fun showError(error: String) {
        etEditorText.error = error
    }
}