package io.spaceisstrange.opencoches.api.net

import io.spaceisstrange.opencoches.api.model.UserData

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

class FCUser(val userId: String) : BaseGetRequest() {

    fun getData(): UserData {
        val response = super.doRequest()
        val document = response.parse()

        // Obtenemos el div con la información del usuario
        var infoDiv = document.select("div.block_content")
        infoDiv = infoDiv.select("fieldset.statistics_group")
        infoDiv = infoDiv.select("ul.list_no_decoration")
        val userTotalPosts = infoDiv[0].select("li")[0].text()
        var userLastActivity = infoDiv[1].select("li")[0].text()
        val userRegistrationDate: String

        // Hay algunos usuarios que tienen la última actividad oculta
        try {
            userRegistrationDate = infoDiv[1].select("li")[1].text()
        } catch (e: IndexOutOfBoundsException) {
            userLastActivity = ""
            userRegistrationDate = infoDiv[1].select("li")[0].text()
        }

        return UserData(userLastActivity, userTotalPosts, userRegistrationDate)
    }

    override fun getUrl(): String {
        return ApiConstants.BASE_URL + ApiConstants.MEMBER_LINK_KEY + userId
    }
}