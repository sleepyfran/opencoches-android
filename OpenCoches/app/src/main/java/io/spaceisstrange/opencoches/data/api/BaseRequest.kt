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

import org.jsoup.Connection

/**
 * Clase base para todas las peticiones realizadas.
 */
abstract class BaseRequest {
    /**
     * Constantes de cada petición.
     */
    val USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36"
    val TIMEOUT = 15000

    /**
     * Comprueba si la request actual ha sido o no correcta, es decir, que el código esté entre
     * el 200 y el 400. He decidido incluir el rango de los 300 ya que hay veces que al responder
     * en hilos el servidor te responde con una redirección (está por comprobar pero creo que sucede
     * siempre que se inicia una página nueva con tu comentario).
     */
    fun isSuccessful(code: Int): Boolean {
        return code in 200..400
    }

    /**
     * Realiza la petición con la URL indicada en la subclase y los parámetros por defecto.
     */
    abstract fun doRequest(): Connection.Response

    /**
     * Método a llamar cuando la petición necesite una URL. Será implementado por la subclase.
     */
    abstract fun getUrl(): String
}