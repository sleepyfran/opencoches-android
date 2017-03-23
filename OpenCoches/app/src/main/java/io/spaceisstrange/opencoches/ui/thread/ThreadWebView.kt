/*
 * Made with <3 by Fran González (@spaceisstrange)
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

package io.spaceisstrange.opencoches.ui.thread

import android.content.Context
import android.util.AttributeSet
import android.webkit.JavascriptInterface
import io.spaceisstrange.opencoches.data.api.ApiConstants
import io.spaceisstrange.opencoches.data.model.Post
import io.spaceisstrange.opencoches.ui.templates.PostsTemplate
import io.spaceisstrange.opencoches.ui.views.OpenCochesWebView

/**
 * WebView específica para la muestra de hilos.
 */
class ThreadWebView : OpenCochesWebView<List<Post>> {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * Scroll listener del WebView.
     */
    var onScroll: ((l: Int, t: Int, oldl: Int, oldt: Int) -> Unit)? = null

    /**
     * Método a llamar cuando el usuario presione en un usuario.
     */
    var onUserClick: ((posterId: String) -> Unit)? = null

    /**
     * Método a llamar cuando el usuario presione la respuesta rápida.
     */
    var onQuoteClick: ((postId: String) -> Unit)? = null

    init {
        // Nos registramos como interfaz de JavaScript
        addJavascriptInterface(this, "Android")
    }

    override fun onScrollChanged(x: Int, y: Int, oldX: Int, oldY: Int) {
        super.onScrollChanged(x, y, oldX, oldY)
        onScroll?.invoke(x, y, oldX, oldY)
    }

    override fun loadContent(content: List<Post>, onLoad: (() -> Unit)?) {
        this.onLoad = onLoad

        val contentHtml = PostsTemplate(context).render(content)
        loadDataWithBaseURL(ApiConstants.BASE_URL, contentHtml, "text/html", "utf-8", null)
    }

    @JavascriptInterface
    fun showProfile(posterId: String) {
        onUserClick?.invoke(posterId)
    }

    @JavascriptInterface
    fun quote(postId: String) {
        onQuoteClick?.invoke(postId)
    }
}