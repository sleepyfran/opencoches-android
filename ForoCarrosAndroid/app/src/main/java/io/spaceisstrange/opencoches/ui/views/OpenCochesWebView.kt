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

package io.spaceisstrange.opencoches.ui.views

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import io.spaceisstrange.opencoches.data.api.ApiConstants

class OpenCochesWebView : WebView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * Carga el contenido HTML especificado en el webview
     */
    fun loadContent(content: String) {
        // Reemplazamos las URLs "falsas" de FC
        val htmlContent = content.replace("//st.forocoches.com/", "http://st.forocoches.com/")

        val headHtml = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">" +
                "<style>img {max-width: 100%; width:auto; height: auto;}</style>" +
                "</head>"
        val contentHtml = "$headHtml<body>$htmlContent</body>"
        loadDataWithBaseURL(ApiConstants.BASE_URL,contentHtml, "text/html", "utf-8", null)
    }
}