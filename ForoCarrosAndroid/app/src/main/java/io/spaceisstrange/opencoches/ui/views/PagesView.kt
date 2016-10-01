package io.spaceisstrange.opencoches.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import io.spaceisstrange.opencoches.R
import kotlinx.android.synthetic.main.view_pages.view.*

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

class PagesView : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_pages, this, true)

        btnFirstPage.setOnClickListener {
            currentPage = 1
            onFirstPage?.invoke()
        }

        btnPreviousPage.setOnClickListener {
            currentPage--
            onPreviousPage?.invoke()
        }

        btnNextPage.setOnClickListener {
            currentPage++
            onNextPage?.invoke()
        }

        btnLastPage.setOnClickListener {
            currentPage = totalPageCount
            onLastPage?.invoke()
        }
    }

    /**
     * Callbacks para los botones
     */
    var onFirstPage: (() -> Unit)? = null
    var onPreviousPage: (() -> Unit)? = null
    var onNextPage: (() -> Unit)? = null
    var onLastPage: (() -> Unit)? = null

    /**
     * Variables de las páginas actuales y totales
     */
    var currentPage: Int = 1
    var totalPageCount: Int = 1

    /**
     * Actualiza el campo de página
     */
    private fun updatePages() {
        context.getString(R.string.thread_pages_count, currentPage, totalPageCount)
    }

    /**
     * Actualiza el número actual de la página
     */
    fun updateCurrentPage(page: Int) {
        currentPage = page
        updatePages()
    }

    /**
     * Actualiza el número total de páginas
     */
    fun updateTotalPageCount(totalPages: Int) {
        totalPageCount = totalPages
        updatePages()
    }
}