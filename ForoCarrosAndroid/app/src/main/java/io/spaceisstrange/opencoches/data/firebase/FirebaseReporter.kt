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

package io.spaceisstrange.opencoches.data.firebase

/**
 * A comentar si vais a compilar sin Firebase
 */
import com.google.firebase.crash.FirebaseCrash

class FirebaseReporter {
    companion object {
        /**
         * Cambiad esta variablea false si queréis compilar el proyecto sin necesidad de la integración
         * con Firebase
         */
        val isFirebaseOn = true

        /**
         * Reporta un error a Firebase sólo si lo tenemos activado
         */
        fun report(e: Throwable) {
            if (isFirebaseOn) {
                // A comentar también si queréis compilar sin Firebase
                FirebaseCrash.report(e)
            }
        }
    }
}