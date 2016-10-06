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

package io.spaceisstrange.opencoches.ui.templates

import android.content.Context
import com.squareup.phrase.Phrase
import java.io.BufferedReader
import java.io.InputStreamReader

abstract class HtmlTemplate<in T>(val context: Context, val template: String) {
    /**
     * Template compilada a Phrase para ser utilizada
     */
    var compiledTemplate: Phrase = compileTemplate(readFromAssets(template))

    /**
     * Lee la template de los assets
     */
    fun readFromAssets(template: String): String {
        val buffer = StringBuilder()
        val stream = context.assets.open(template)
        val bufferedStream = BufferedReader(InputStreamReader(stream))
        var isFirstLine = true

        var line = bufferedStream.readLine()
        while (line != null) {
            if (isFirstLine) {
                isFirstLine = false
            } else {
                buffer.append("\n")
            }

            buffer.append(line)
            line = bufferedStream.readLine()
        }

        bufferedStream.close()

        return buffer.toString()
    }

    /**
     * Compila la templateFromAssets en un Phrase
     */
    fun compileTemplate(templateFromAssets: String): Phrase {
        return Phrase.from(templateFromAssets)
    }

    /**
     * Método a llamar cuando se quiera renderizar el contenido
     */
    fun render(content: T): String {
        return render(content, compiledTemplate)
    }

    /**
     * Método a llamar cuando se necesite renderizar la template
     */
    abstract fun render(content: T, template: Phrase): String
}