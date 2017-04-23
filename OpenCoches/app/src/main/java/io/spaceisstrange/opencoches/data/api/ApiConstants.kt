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
        const val BASE_MOBILE_URL = "http://m.forocoches.com/foro/"
        const val SEARCH_URL = "http://www.forocoches.com/forocoches_search.php"
        const val LOGIN_URL = "login.php"
        const val SUBFORUM_PAGE_URL = "&page="
        const val THREAD_PAGE_URL = "&page="
        const val SEARCH_PAGE_URL = "&page="
        const val SMILIES_URL = "misc.php?do=getsmilies&editorid=vB_Editor_001"
        const val REPLY_URL = "newreply.php"
        const val DO_URL = "?do="
        const val T_URL = "&t="
        const val QUOTE_URL = "newreply.php?do=newreply&p="
        const val USER_PROFILE_URL = "member.php?u="

        /**
         * Valores esperados de ciertas secciones del foro, como URLs, por ejemplo.
         */
        const val SUBFORUM_LINK_KEY = "forumdisplay.php?f="
        const val THREAD_MAX_POSTS_PER_PAGE = 30.toDouble()
        const val THREAD_TITLE_KEY = "thread_title_"
        const val THREAD_PAGES_KEY = "misc.php?do=whoposted&t="
        const val THREAD_STICKY_KEY = "collapseobj_st_"
        const val THREAD_NO_STICKY_KEY = "threadbits_forum_"
        const val SUBFORO_LINK_KEY = "forumdisplay.php?f="
        const val TD_THREAD_TITLE_KEY = "td_threadtitle"
        const val THREAD_POST_KEY = "td_post_"
        const val POST_ROOT_ID_KEY = "post"
        const val POST_USER_USERNAME_CLASS_KEY = "bigusername"
        const val POST_USER_IMAGE_CLASS_KEY = "avatar"
        const val POST_CONTENT_USER_INFO_KEY = "smallfont"
        const val POST_TIMESTAMP_CLASS_KEY = "thead"
        const val SECURITY_TOKEN_KEY = "securitytoken"
        const val THREAD_TITLE_CMEGA_KEY = "cmega"
        const val USERNAME_BOX_KEY = "username_box"
        const val USER_AVATAR_KEY = "user_avatar"
        const val QUOTE_TEXT_KEY = "message"
        const val POST_HASH_KEY = "posthash"
        const val POST_START_TIME_KEY = "poststarttime"

        /**
         * Etiquetas del editor de texto
         */
        const val EDITOR_B_TAG = "[B]"
        const val EDITOR_B_CLOSE_TAG = "[/B]"
        const val EDITOR_I_TAG = "[I]"
        const val EDITOR_I_CLOSE_TAG = "[/I]"
        const val EDITOR_U_TAG = "[U]"
        const val EDITOR_U_CLOSE_TAG = "[/U]"
        const val EDITOR_IMG_TAG = "[IMG]"
        const val EDITOR_IMG_CLOSE_TAG = "[/IMG]"
        const val EDITOR_VID_TAG = "[YOUTUBE]"
        const val EDITOR_VID_CLOSE_TAG = "[/YOUTUBE]"

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
        const val T_PARAMETER = "t"
        const val MESSAGE_PARAMETER = "message"
        const val WYSIWYG_PARAMETER = "wysiwyg"
        const val STYLE_ID_PARAMETER = "styleid"
        const val FROM_QUICK_REPLY_PARAMETER = "fromquickreply"
        const val P_PARAMETER = "p"
        const val SPECIFIED_POST_PARAMETER = "specifiedpost"
        const val PARSE_URL_PARAMETER = "parseurl"
        const val LOGGED_IN_USER_PARAMETER = "loggedinuser"
        const val S_BUTTON_PARAMETER = "sbutton"
        const val QUERY_PARAMETER = "query"
        const val OPCION_PARAMETER = "opcion"
        const val REG_PARAMETER = "reg"
        const val LB_PARAMETER = "lb"

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

        /**
         * Parámetros de la petición POST de envío de una respuesta a un hilo.
         */
        fun getThreadReplyParameters(securityToken: String, threadId: String, reply: String, userId: String): Map<String, String> {
            return hashMapOf(
                    MESSAGE_PARAMETER to reply,
                    WYSIWYG_PARAMETER to "0",
                    STYLE_ID_PARAMETER to "5",
                    FROM_QUICK_REPLY_PARAMETER to "1",
                    S_PARAMETER to "",
                    SECURITY_TOKEN_PARAMETER to securityToken,
                    DO_PARAMETER to "postreply",
                    T_PARAMETER to threadId,
                    P_PARAMETER to "who cares",
                    SPECIFIED_POST_PARAMETER to "0",
                    PARSE_URL_PARAMETER to "1",
                    LOGGED_IN_USER_PARAMETER to userId,
                    S_BUTTON_PARAMETER to ""
            )
        }

        /**
         * Parámetros de la petición POST de búsqueda.
         */
        fun getSearchParameters(query: String, userId: String): Map<String, String> {
            return hashMapOf(
                    QUERY_PARAMETER to query,
                    OPCION_PARAMETER to "foro",
                    REG_PARAMETER to userId,
                    LB_PARAMETER to "Buscar"
            )
        }
    }
}