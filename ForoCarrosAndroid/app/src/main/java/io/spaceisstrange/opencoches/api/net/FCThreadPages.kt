package io.spaceisstrange.opencoches.api.net

import io.spaceisstrange.opencoches.utils.RegexUtils

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

class FCThreadPages(val threadLink: String) : BaseGetRequest() {

    fun getPages(): Int {
        val response = super.doRequest()
        val document = response.parse()

        // Obtenemos la tabla con las páginas
        val pagesTable = document.select("td[class^=vbmenu_control]")

        var tableText: String
        for (tables in pagesTable) {
            tableText = tables.text()

            if (tableText.contains("Pág")) {
                val totalPages = RegexUtils.getPagesRegex().matchEntire(tableText)?.groups?.get(1)?.value

                if (totalPages != null) {
                    return totalPages.toInt()
                }
            }
        }

        return 1
    }

    override fun getUrl(): String {
        return ApiConstants.BASE_URL + threadLink
    }
}