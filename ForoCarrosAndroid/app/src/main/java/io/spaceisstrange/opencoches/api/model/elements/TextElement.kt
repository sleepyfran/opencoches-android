package io.spaceisstrange.opencoches.api.model.elements

import android.content.Context
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView

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

class TextElement(text: String) : Element() {
    /**
     * Texto total del elemento
     */
    var elementText: String

    init {
        elementText = text
    }

    override fun getView(context: Context): View {
        val cached = cachedView

        if (cached != null) {
            return cached
        } else {
            val textView = TextView(context)
            textView.text = Html.fromHtml(elementText)

            // Hacemos el TextView clicable
            textView.movementMethod = LinkMovementMethod.getInstance()

            // Cacheamos la view para la próxima
            cachedView = textView

            return textView
        }
    }

    /**
     * Appends text to the current element
     */
    fun appendText(text: String) {
        elementText = elementText.plus(text)
    }
}