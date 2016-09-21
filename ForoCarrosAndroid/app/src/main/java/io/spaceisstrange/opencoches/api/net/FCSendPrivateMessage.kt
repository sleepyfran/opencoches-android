package io.spaceisstrange.opencoches.api.net

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

class FCSendPrivateMessage(val recipient: String,
                           val title: String,
                           val message: String,
                           val securityToken: String) : BasePostRequest() {

    fun sendPm() {
        super.doRequest()
    }

    override fun getPostParameters(): Map<String, String> {
        return ApiConstants.getPmParameters(recipient, title, message, securityToken)
    }

    override fun getUrl(): String {
        return ApiConstants.BASE_URL + ApiConstants.SEND_MESSAGE_URL
    }
}