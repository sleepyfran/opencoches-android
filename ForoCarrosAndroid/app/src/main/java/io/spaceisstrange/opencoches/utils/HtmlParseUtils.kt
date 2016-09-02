package io.spaceisstrange.opencoches.utils

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.LevelListDrawable
import android.text.Html
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget

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

class HtmlParseUtils {
    companion object {
        /**
         * Retorna un ImageGetter para ser utilizado con el Html.fromHtml para mostrar imágenes
         */
        fun getImageGetter(container: TextView): Html.ImageGetter {
            return Html.ImageGetter {
                source ->

                val drawableList = LevelListDrawable()

                // Ponemos un placeholder para mostrarse mientras se cargan las imágenes
                val placeholder = container.context.resources.getDrawable(android.R.drawable.progress_horizontal)
                drawableList.addLevel(0, 0, placeholder)
                drawableList.setBounds(0, 0, placeholder.intrinsicWidth, placeholder.intrinsicHeight)

                // Cargamos la imagen con Glide
                Glide.with(container.context)
                        .load(source)
                        .asBitmap()
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                                // Obtenemos el drawable y lo añadimos a la lista
                                val bitmapDrawable = BitmapDrawable(container.resources, resource)
                                drawableList.addLevel(1, 1, bitmapDrawable)
                                drawableList.setBounds(0, 0,
                                        bitmapDrawable.intrinsicWidth,
                                        bitmapDrawable.intrinsicHeight)

                                // Actualizamos el nivel y forzamos un refresco del TextView
                                drawableList.level = 1
                                val forceRefreshText = container.text
                                container.text = forceRefreshText
                            }
                        })

                drawableList
            }
        }
    }
}