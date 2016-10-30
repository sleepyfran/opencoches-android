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

import android.content.Context
import android.content.Intent
import android.net.Uri

class IntentUtils {
    companion object {
        /**
         * Crea un Intent Chooser para que el usuario pueda elegir el navegador dónde abrir el link
         */
        fun createBrowserIntentChooser(context: Context, uri: Uri): Intent? {
            val browserIntent = Intent(Intent.ACTION_VIEW, uri).addCategory(Intent.CATEGORY_BROWSABLE)
            val chooser = Intent.createChooser(browserIntent, "Selecciona el navegador")

            if (browserIntent.resolveActivity(context.packageManager) != null) {
                return chooser
            } else {
                return null
            }
        }

        fun createBrowserIntentChooser(context: Context, url: String): Intent? {
            return createBrowserIntentChooser(context, Uri.parse(url))
        }
    }
}