package io.spaceisstrange.opencoches.api.model.elements

import android.content.Context
import android.view.View

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

abstract class Element {
    /**
     * View cacheada por si se llama varias veces al getView
     */
    var cachedView: View? = null

    /**
     * Método a llamar cuando necesitemos obtener la view del elemento con toda su información
     */
    abstract fun getView(context: Context): View
}