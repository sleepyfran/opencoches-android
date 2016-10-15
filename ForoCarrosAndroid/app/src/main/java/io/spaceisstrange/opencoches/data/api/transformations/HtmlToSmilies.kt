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

package io.spaceisstrange.opencoches.data.api.transformations

import io.spaceisstrange.opencoches.data.model.Smily
import org.jsoup.nodes.Document

class HtmlToSmilies {
    companion object {
        /**
         * Obtiene los smilies disponibles en el foro y los retorna en forma de chapa (lista)
         */
        fun transform(document: Document): List<Smily> {
            // Lista donde almacenaremos los smilies
            val smilies: MutableList<Smily> = mutableListOf()

            // Obtenemos las <tr> del documento, obviando las dos primeras y la última
            val smilyRows = document.select("tr")
            smilyRows.removeAt(0)
            smilyRows.removeAt(0)
            smilyRows.removeAt(smilyRows.size - 1)

            for (smily in smilyRows) {
                // Obtenemos los datos del smily y lo añadimos a la lista
                val smilyColumns = smily.select("td")

                // En cada fila hay dos smilies, así que obtenemos sus datos
                var firstSmilyLink = smilyColumns[0].select("img").attr("src")
                val firstSmilyCode = smilyColumns[1].text()
                var secondSmilyLink = smilyColumns[2].select("img").attr("src")
                val secondSmilyCode = smilyColumns[3].text()

                // Reemplazamos las URLs "falsas" (vaya unos truquitos, Ilitri...)
                firstSmilyLink = firstSmilyLink.replace("//st.forocoches.com/", "http://st.forocoches.com/")
                secondSmilyLink = secondSmilyLink.replace("//st.forocoches.com/", "http://st.forocoches.com/")

                // Y los añadimos
                smilies.add(Smily(firstSmilyCode, firstSmilyLink))
                smilies.add(Smily(secondSmilyCode, secondSmilyLink))
            }

            return smilies
        }
    }
}