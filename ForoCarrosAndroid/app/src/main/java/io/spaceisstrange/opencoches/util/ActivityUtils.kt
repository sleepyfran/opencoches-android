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

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

/**
 * Set de utilidades para las activities
 */
class ActivityUtils {
    companion object {
        /**
         * Añade el fragment especificado a la activity
         */
        fun addFragmentToActivity(fragmentManager: FragmentManager, fragment: Fragment, frameId: Int) {
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(frameId, fragment)
            transaction.commit()
        }
    }
}