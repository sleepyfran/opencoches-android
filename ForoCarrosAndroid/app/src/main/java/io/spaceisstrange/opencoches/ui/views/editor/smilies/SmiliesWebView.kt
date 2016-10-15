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

package io.spaceisstrange.opencoches.ui.views.editor.smilies

import android.content.Context
import android.util.AttributeSet
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import io.spaceisstrange.opencoches.data.api.ApiConstants
import io.spaceisstrange.opencoches.data.model.Smily
import io.spaceisstrange.opencoches.ui.templates.SmiliesTemplate
import io.spaceisstrange.opencoches.ui.views.OpenCochesWebView

class SmiliesWebView : OpenCochesWebView<List<Smily>> {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * Método a llamar cuando el usuario presione en un smily
     */
    lateinit var onSmilyClick: (smilyCode: String) -> Unit

    init {
        // Nos registramos como interfaz de JavaScript
        addJavascriptInterface(this, "Android")

        // Configuramos la WebView para funcionar correctamente con la SlidingActivity
        setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String?) {
                val initialHeight = view.measuredHeight
                view.minimumHeight = initialHeight
                super.onPageFinished(view, url)
            }
        })
    }

    override fun loadContent(content: List<Smily>, onLoad: (() -> Unit)?) {
        this.onLoad = onLoad

        val contentHtml = SmiliesTemplate(context).render(content)
        loadDataWithBaseURL(ApiConstants.BASE_URL, contentHtml, "text/html", "utf-8", null)
    }

    @JavascriptInterface
    fun smilySelected(smilyCode: String) {
        onSmilyClick(smilyCode)
    }
}