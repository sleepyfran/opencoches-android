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

package io.spaceisstrange.opencoches.data.api

class ApiConstants {
    companion object {
        /**
         * Constantes de URLs donde hacer peticiones
         * (ejemplo: http://www.forocoches.com/foro/forumdisplay.php?f= para los subforos).
         */
        const val BASE_URL = "http://www.forocoches.com/foro/"
        const val SUBFORUM_URL = "forumdisplay.php?f="
        const val LOGIN_URL = "login.php"
        const val USER_PROFILE_URL = "member.php?u="
        const val MESSAGE_URL = "private.php?do=newpm&u="
        const val SEND_MESSAGE_URL = "private.php?do=insertpm&pmid="
        const val THREAD_PAGE_URL = "&page="
        const val SUBFORO_PAGE_URL = "&page="

        /**
         * Valores esperados de ciertas secciones del foro, como links de los subforos, temas, etc.
         */
        const val SUBFORO_LINK_KEY = "forumdisplay.php?f="
        const val TD_THREAD_TITLE_KEY = "td_threadtitle"
        const val THREAD_TITLE_KEY = "thread_title_"
        const val THREAD_PAGES_KEY = "misc.php?do=whoposted&t="
        const val THREAD_MAX_POSTS_PER_PAGE = 30.toDouble()
        const val POST_ROOT_ID_KEY = "post"
        const val POST_USER_USERNAME_CLASS_KEY = "bigusername"
        const val POST_USER_IMAGE_CLASS_KEY = "avatar"
        const val POST_CONTENT_USER_INFO_KEY = "smallfont"
        const val POST_TIMESTAMP_CLASS_KEY = "thead"

        /**
         * Claves de los parámetros de las peticiones POST utilizadas más abajo
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
         * Parámetros de la petición POST de Login
         */
        fun getLoginParameters(username: String, password: String): Map<String, String> {
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

        /**
         * Claves de las cookies
         */
        val LOGGED_IN_KEY_COOKIE = "bbimloggedin"

        /**
         * Valores esperados de las cookies
         */
        val LOGGED_IN_COOKIE_YES = "yes"
        val LOGGED_IN_COOKIE_NO = "no"
    }
}