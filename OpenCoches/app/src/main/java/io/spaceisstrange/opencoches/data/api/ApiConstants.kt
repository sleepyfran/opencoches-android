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

package io.spaceisstrange.opencoches.data.api

/**
 * Constantes usadas a lo largo de todas las peticiones a ForoCoches.
 */
class ApiConstants {
    companion object {
        /**
         * Constantes de URLs donde hacer peticiones.
         * (ejemplo: http://www.forocoches.com/foro/forumdisplay.php?f= para los subforos).
         */
        const val BASE_URL = "http://www.forocoches.com/foro/"
        const val LOGIN_URL = "login.php"
        const val SUBFORUM_PAGE_URL = "&page="

        /**
         * Valores esperados de ciertas secciones del foro, como URLs, por ejemplo.
         */
        const val SUBFORUM_LINK_KEY = "forumdisplay.php?f="
        const val THREAD_MAX_POSTS_PER_PAGE = 30.toDouble()
        const val THREAD_TITLE_KEY = "thread_title_"
        const val THREAD_PAGES_KEY = "misc.php?do=whoposted&t="

        /**
         * Claves de las cookies.
         */
        val LOGGED_IN_KEY_COOKIE = "bbimloggedin"

        /**
         * Valores esperados de las cookies.
         */
        val LOGGED_IN_COOKIE_YES = "yes"
        val LOGGED_IN_COOKIE_NO = "no"

        /**
         * Claves de los parámetros de las peticiones POST utilizadas más abajo.
         */
        const val URL_PARAMETER = "url"
        const val DO_PARAMETER = "do"
        const val VB_LOGIN_MD5_PASSWORD_PARAMETER = "vb_login_md5password"
        const val VB_LOGIN_MD5_PASSWORD_UTF_PARAMETER = "vb_login_md5password_utf"
        const val S_PARAMETER = "s" // MEJOR PUTO PARÁMETRO DE LA HISTORIA.
        const val SECURITY_TOKEN_PARAMETER = "securitytoken"
        const val VB_LOGIN_USERNAME_PARAMETER = "vb_login_username"
        const val VB_LOGIN_PASSWORD_PARAMETER = "vb_login_password"
        const val COOKIE_USER_PARAMETER = "cookieuser"
        const val LOGB2_PARAMETER = "logb2"

        /**
         * Parámetros de la petición POST de Login.
         */
        fun loginParameters(username: String, password: String): Map<String, String> {
            return hashMapOf(
                    URL_PARAMETER to BASE_URL,
                    DO_PARAMETER to "login",
                    VB_LOGIN_MD5_PASSWORD_PARAMETER to "",
                    VB_LOGIN_MD5_PASSWORD_UTF_PARAMETER to "",
                    S_PARAMETER to "",
                    SECURITY_TOKEN_PARAMETER to "guest",
                    VB_LOGIN_USERNAME_PARAMETER to username,
                    VB_LOGIN_PASSWORD_PARAMETER to password,
                    COOKIE_USER_PARAMETER to "1",
                    LOGB2_PARAMETER to "   Acceder   "
            )
        }
    }
}