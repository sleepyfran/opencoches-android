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
import io.spaceisstrange.opencoches.data.model.Smily

class SmiliesTemplate(context: Context) : HtmlTemplate<List<Smily>>(context, "smilies_list.html") {
    /**
     * Dependencias utilizadas
     */
    val smilyTemplate = SmilyTemplate(context)

    override fun render(content: List<Smily>, template: Phrase): String {
        val buffer = StringBuilder()

        for (smily in content) {
            buffer.append(smilyTemplate.render(smily))
        }

        // Añadimos el JS y CSS de los posts
        return template
                .put("css", readFromAssets("smilies_style.css"))
                .put("smilies", buffer)
                .format()
                .toString()
    }
}