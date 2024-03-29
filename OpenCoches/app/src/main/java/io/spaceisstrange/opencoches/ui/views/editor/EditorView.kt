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

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.api.ApiConstants
import io.spaceisstrange.opencoches.data.bus.Bus
import io.spaceisstrange.opencoches.data.bus.events.SmilySelectedEvent
import io.spaceisstrange.opencoches.ui.views.editor.imageupload.ImageSelectionDialog
import io.spaceisstrange.opencoches.ui.views.editor.smilies.SmiliesActivity
import kotlinx.android.synthetic.main.view_editor.view.*
import rx.Subscription

/**
 * Vista que muestra nuestro editor de texto personalizado
 */
class EditorView : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_editor, this, true)

        // Especificamos las opciones de los botones de la barra de herramientas
        editorBold.setOnClickListener {
            // Añadimos la etiqueta B
            appendTag(ApiConstants.EDITOR_B_TAG, ApiConstants.EDITOR_B_CLOSE_TAG)
        }

        editorItalics.setOnClickListener {
            // Añadimos la etiqueta I
            appendTag(ApiConstants.EDITOR_I_TAG, ApiConstants.EDITOR_I_CLOSE_TAG)
        }

        editorUnderline.setOnClickListener {
            // Añadimos la etiqueta U
            appendTag(ApiConstants.EDITOR_U_TAG, ApiConstants.EDITOR_U_CLOSE_TAG)
        }

        editorImg.setOnClickListener {
            // Mostramos el diálogo de selección de imagen
            ImageSelectionDialog.instantiate {
                link ->

                // Añadimos la imagen con el link al editor
                appendTag(ApiConstants.EDITOR_IMG_TAG, ApiConstants.EDITOR_IMG_CLOSE_TAG)
                editorText.text.insert(editorText.selectionStart, link)
            }.show((context as AppCompatActivity).supportFragmentManager, null)
        }

        editorVid.setOnClickListener {
            // Mostramos el diálogo de introducción de un link
            LinkTextFieldDialog.instantiate(
                    {
                        link ->

                        // Añadimos la imagen con el link al editor
                        appendTag(ApiConstants.EDITOR_VID_TAG, ApiConstants.EDITOR_VID_CLOSE_TAG)
                        editorText.text.insert(editorText.selectionStart, link)
                    },
                    {
                        text ->

                        true
                    }
            ).show((context as AppCompatActivity).supportFragmentManager, null)
        }

        editorEmoji.setOnClickListener {
            // Mostramos la activity de smilies
            val smilyIntent = Intent(context, SmiliesActivity::class.java)
            context.startActivity(smilyIntent)

            // Nos subscribimos al bus para saber cuándo el usuario ha seleccionado un smily
            var busSubscription: Subscription? = null
            busSubscription = Bus.instance.observable().subscribe(
                    {
                        event ->

                        if (event is SmilySelectedEvent) {
                            // Añadimos el smily al campo de texto
                            getActivity()?.runOnUiThread {
                                appendText(event.smilyCode)

                                // Nos desubscribimos para no volver a coger más eventos
                                busSubscription?.unsubscribe()
                            }
                        }
                    },
                    {
                        error ->

                        // Nada, silenciamos
                    }
            )
        }
    }

    /**
     * Utilidad para obtener la activity asociada a esta view.
     */
    private fun getActivity(): AppCompatActivity? {
        var context = context
        while (context is ContextWrapper) {
            if (context is AppCompatActivity) {
                return context
            }

            context = context.baseContext
        }
        return null
    }

    /**
     * Añade el texto especificado al campo de texto.
     */
    fun appendText(text: String) {
        if (editorText.selectionStart > -1) {
            // El usuario tiene colocado el cursor en una posición, así que añadimos el texto ahí
            val nextPosition = editorText.selectionStart + text.length
            editorText.text.insert(editorText.selectionStart, text)
            editorText.setSelection(nextPosition)
        } else {
            // Aún no se ha seleccionado ninguna posición en el campo de texto
            editorText.append(text)
            editorText.setSelection(editorText.length())
        }
    }

    /**
     * Añade la etiqueta especificada al campo de texto.
     */
    private fun appendTag(tag: String, closingTag: String) {
        if (editorText.selectionStart > -1) {
            // El usuario tiene colocado el cursor en una posición, así que añadimos la etiqueta ahí
            val nextPosition = editorText.selectionStart + tag.length
            editorText.text.insert(editorText.selectionStart, tag + closingTag)
            editorText.setSelection(nextPosition)
        } else {
            // Aún no se ha seleccionado ninguna posición en el campo de texto
            editorText.append(tag + closingTag)
            editorText.setSelection(editorText.length() - closingTag.length)
        }
    }

    /**
     * Retorna el texto introducido en el campo de texto del editor.
     */
    fun text(): String {
        return editorText.text.toString()
    }

    /**
     * Muestra un error en el campo de texto.
     */
    fun showError(error: String) {
        editorText.error = error
    }
}