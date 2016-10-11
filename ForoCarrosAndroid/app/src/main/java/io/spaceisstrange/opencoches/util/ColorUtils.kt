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

package io.spaceisstrange.opencoches.util

import io.spaceisstrange.opencoches.R

class ColorUtils {
    companion object {
        /**
         * Retorna la lista de colores a usar en los SwipeRefreshLayout
         */
        fun getSwipeRefreshLayoutColors(): IntArray {
            return intArrayOf(
                    R.color.srl_blue,
                    R.color.srl_brown,
                    R.color.srl_green,
                    R.color.srl_green_blue,
                    R.color.srl_orange,
                    R.color.srl_pink,
                    R.color.srl_purple,
                    R.color.srl_red
            )
        }
    }
}