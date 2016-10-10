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

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import io.spaceisstrange.opencoches.App
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.api.ApiConstants
import io.spaceisstrange.opencoches.data.bus.Bus
import io.spaceisstrange.opencoches.data.bus.events.LinkAvailableEvent
import io.spaceisstrange.opencoches.ui.views.editor.imageupload.ImageSelectionDialog
import kotlinx.android.synthetic.main.view_editor.view.*
import rx.Subscription
import javax.inject.Inject

class EditorView : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * Bus de la aplicación
     */
    @Inject lateinit var bus: Bus

    init {
        LayoutInflater.from(context).inflate(R.layout.view_editor, this, true)

        // Inyectamos la view
        DaggerEditorViewComponent.builder()
                .busComponent((context.applicationContext as App).busComponent)
                .build()
                .inject(this)

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
            // Nos suscribimos al bus para obtener el link cuando esté listo
            var imgObservable: Subscription? = null
            imgObservable = bus.observable().subscribe(
                    {
                        event ->

                        // Ponemos el link en el editor
                        if (event is LinkAvailableEvent) {
                            appendTag(ApiConstants.EDITOR_IMG_TAG, ApiConstants.EDITOR_IMG_CLOSE_TAG)
                            etEditorText.text.insert(etEditorText.selectionStart, event.link)

                            // Nos desubscribimos para no caer aquí eternamente
                            imgObservable?.unsubscribe()
                        }
                    }
            )

            // Mostramos el diálogo de selección de imagen
            ImageSelectionDialog().show((context as AppCompatActivity).supportFragmentManager, null)
        }

        btnEditorVid.setOnClickListener {
            // Nos suscribimos al bus para obtener el link cuando esté listo
            var vidObservable: Subscription? = null
            vidObservable = bus.observable().subscribe(
                    {
                        event ->

                        // Ponemos el link en el editor
                        if (event is LinkAvailableEvent) {
                            appendTag(ApiConstants.EDITOR_VID_TAG, ApiConstants.EDITOR_VID_CLOSE_TAG)
                            etEditorText.text.insert(etEditorText.selectionStart, event.link)

                            // Nos desubscribimos para no caer aquí eternamente
                            vidObservable?.unsubscribe()
                        }
                    }
            )

            // Mostramos el diálogo de introducción de un link
            LinkTextFieldDialog().show((context as AppCompatActivity).supportFragmentManager, null)
        }
    }

    /**
     * Añade la etiqueta especificada al campo de texto
     */
    private fun appendTag(tag: String, closingTag: String) {
        if (etEditorText.selectionStart > -1) {
            // El usuario tiene colocado el cursor en una posición, así que añadimos la etiqueta ahí
            val nextPosition = etEditorText.selectionStart + tag.length
            etEditorText.text.insert(etEditorText.selectionStart, tag + closingTag)
            etEditorText.setSelection(nextPosition)
        } else {
            // Aún no se ha seleccionado ninguna posición en el campo de texto
            etEditorText.append(tag + closingTag)
            etEditorText.setSelection(etEditorText.length() - closingTag.length)
        }
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