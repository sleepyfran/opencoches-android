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

package io.spaceisstrange.opencoches.ui.thread

import android.content.Context
import android.util.AttributeSet
import android.webkit.JavascriptInterface
import io.spaceisstrange.opencoches.data.api.ApiConstants
import io.spaceisstrange.opencoches.data.model.Post
import io.spaceisstrange.opencoches.ui.templates.PostsTemplate
import io.spaceisstrange.opencoches.ui.views.OpenCochesWebView

class ThreadWebView : OpenCochesWebView<List<Post>> {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        // Nos registramos como interfaz de JavaScript
        addJavascriptInterface(this, "Android")
    }

    override fun loadContent(content: List<Post>) {
        val contentHtml = PostsTemplate(context).render(content)
        loadDataWithBaseURL(ApiConstants.BASE_URL, contentHtml, "text/html", "utf-8", null)
    }

    @JavascriptInterface
    fun showProfile(username: String, id: String, pictureSrc: String) {
        onUserClick?.invoke(username, id, pictureSrc)
    }
}