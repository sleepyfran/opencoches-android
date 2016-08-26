package io.spaceisstrange.forocarrosandroid.api.net

import java.util.*

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

class ApiConstants {
    companion object {
        /**
         * Constantes de atributos en la web a buscar (ejemplo: showthread.php?t= para hilos).
         */
        const val HILO_SUBFORO = "showthread.php?t="

        /**
         * Constantes de URLs donde hacer peticiones
         * (ejemplo: http://m.forocoches.com/foro/forumdisplay.php?f= para los subforos).
         *
         * Por lo general las peticiones he decidido hacerlas a la versión móvil de la web para
         * así gastar la menor cantidad de datos posibles
         */
        const val BASE_URL = "http://m.forocoches.com/foro/"
        const val SUBFORO_URL = "forumdisplay.php?f="
        const val LOGIN_URL = "login.php"

        /**
         * IDs de los subforos (ejemplo: 2 para el general), que vienen en la URL.
         */
        // Zona General
        const val GENERAL_KEY = "GENERAL"
        const val ELECTRONICA_KEY = "ELECTRONICA"
        const val EMPLEO_KEY = "EMPLEO"
        const val VIAJES_KEY = "VIAJES"
        const val QUEDADAS_KEY = "QUEDADAS"

        // Zona ForoCoches
        const val FOROCOCHES_KEY = "FOROCOCHES"
        const val COMPETICION_KEY = "COMPETICION"
        const val CLASICOS_KEY = "CLASICOS"
        const val MONOVOLUMENES_KEY = "MONOVOLUMENES"
        const val OCIO_KEY = "4X4-OCIO"
        const val MODELISMO_KEY = "MODELISMO"
        const val CAMIONES_KEY = "CAMIONES"
        const val MOTOS_KEY = "MOTOS"

        // Zona Técnica
        const val MECANICA_KEY = "MECANICA"
        const val CAR_AUDIO_KEY = "CAR_AUDIO"
        const val SEGUROS_KEY = "SEGUROS"
        const val TRAFICO_KEY = "TRAFICO"
        const val TUNING_KEY = "TUNING"

        // Zona Gaming
        const val JUEGOS_COCHES_KEY = "JUEGOS_COCHES"
        const val JUEGOS_ONLINE_KEY = "JUEGOS_ONLINE"

        // TODO: Añadir el resto de zonas en un futuro próximo

        val SUBFOROS: HashMap<String, Int> = hashMapOf(
                // Zona General
                GENERAL_KEY to 2,
                ELECTRONICA_KEY to 17,
                EMPLEO_KEY to 23,
                VIAJES_KEY to 27,
                QUEDADAS_KEY to 15,

                // Zona ForoCoches
                FOROCOCHES_KEY to 4,
                COMPETICION_KEY to 18,
                CLASICOS_KEY to 20,
                MONOVOLUMENES_KEY to 47,
                OCIO_KEY to 21,
                MODELISMO_KEY to 28,
                CAMIONES_KEY to 76,
                MOTOS_KEY to 48,

                // Zona Técnica
                MECANICA_KEY to 19,
                CAR_AUDIO_KEY to 5,
                SEGUROS_KEY to 31,
                TRAFICO_KEY to 30,
                TUNING_KEY to 6,

                // Zona Gaming
                JUEGOS_COCHES_KEY to 16,
                JUEGOS_ONLINE_KEY to 43
        )

        /**
         * Claves de los parámetros de las peticiones POST utilizadas más abajo
         */
        // Parámetros del login
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
                    LOGB2_PARAMETER to "+++Acceder+++"
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