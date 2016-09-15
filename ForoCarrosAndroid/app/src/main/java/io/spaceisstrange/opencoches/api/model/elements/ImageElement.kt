package io.spaceisstrange.opencoches.api.model.elements

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

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

class ImageElement(val imageSource: String) : Element() {
    override fun getView(context: Context): View {
        val cached = cachedView

        if (cached != null) {
            return cached
        } else {
            val imageView = ImageView(context)

            // Cargamos la imagen con Glide
            Glide.with(context)
                    .load(imageSource)
                    .crossFade()
                    .into(imageView)

            // Cacheamos la view para la próxima
            cachedView = imageView

            return imageView
        }
    }
}