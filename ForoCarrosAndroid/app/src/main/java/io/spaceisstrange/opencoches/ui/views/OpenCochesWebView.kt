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
import android.graphics.Color
import android.util.AttributeSet
import android.webkit.WebView

abstract class OpenCochesWebView<in T> : WebView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        // Hacemos el fondo transparente para que sea del color de la App
        setBackgroundColor(Color.TRANSPARENT)

        // Habilitamos JavaScript y nos declaramos como interfaz
        settings.javaScriptEnabled = true
    }

    /**
     * Carga el contenido HTML especificado en el webview
     */
    abstract fun loadContent(content: T)
}