package io.spaceisstrange.opencoches.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.drawable.Drawable
import com.bumptech.glide.load.resource.drawable.GlideDrawable

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

/**
 * Drawable a utilizar en la carga de imágenes de posts
 * Sacado de: https://github.com/goofyz/testGlide/blob/master/src/com/example/testGlide/UrlDrawable.java
 */
class UrlDrawable(context: Context) : Drawable() {

    var drawable: GlideDrawable? = null

    override fun setAlpha(alpha: Int) {
        if (drawable != null) {
            drawable!!.alpha = alpha
        }
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        if (drawable != null) {
            drawable!!.colorFilter = colorFilter
        }
    }

    override fun getOpacity(): Int {
        if (drawable != null) {
            return drawable!!.opacity
        }

        return 0
    }

    override fun draw(canvas: Canvas) {
       if (drawable != null) {
           val paint = Paint()
           paint.color = Color.GREEN
           canvas.drawRect(drawable?.bounds, paint)
           drawable!!.draw(canvas)

           if (!drawable!!.isRunning) {
               drawable!!.start()
           }
       }
    }
}