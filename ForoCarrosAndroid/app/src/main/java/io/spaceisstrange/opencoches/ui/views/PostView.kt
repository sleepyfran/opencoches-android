package io.spaceisstrange.opencoches.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import io.spaceisstrange.opencoches.api.model.elements.Element
import kotlinx.android.synthetic.main.list_item_post.view.*

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

class PostView : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * Añade el contenido del post a la view
     */
    fun addContent(postContent: MutableList<Element>) {
        // Limpiamos el contenido actual
        removeAllViews()

        for (content in postContent) {
            val contentView = content.getView(context)

            if (contentView.parent != null) {
                (contentView.parent as ViewGroup).removeView(contentView)
            }

            addView(contentView)
        }
    }
}