package io.spaceisstrange.forocarrosandroid.api.net

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

class FCLogin(val username: String, val password: String) : BasePostRequest() {

    /**
     * Método a llamar para loguear al usuario en FC
     */
    fun login(): Boolean {
        super.doRequest()
        val cookies = CookiesCache.cookies

        // Vamos a comprobar que tenemos la cookie que nos indica si estamos logueados y está a "yes"
        return cookies != null &&
                cookies.contains(ApiConstants.LOGGED_IN_KEY_COOKIE) &&
                cookies[ApiConstants.LOGGED_IN_KEY_COOKIE] == ApiConstants.LOGGED_IN_COOKIE_YES
    }

    override fun getPostParameters(): Map<String, String> {
        return ApiConstants.getLoginParameters(username, password)
    }

    override fun getUrl(): String {
        return ApiConstants.BASE_URL + ApiConstants.LOGIN_URL
    }
}