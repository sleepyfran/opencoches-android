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

package io.spaceisstrange.opencoches.ui.views

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * Clase base para todos los WebViews utilizados en la aplicación.
 */
abstract class OpenCochesWebView<in T> : WebView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * Método a llamar cuando la página termine de cargar.
     */
    var onLoad: (() -> Unit)? = null

    init {
        // Hacemos el fondo transparente para que sea del color de la App
        setBackgroundColor(Color.TRANSPARENT)

        // Habilitamos JavaScript y nos declaramos como interfaz
        settings.javaScriptEnabled = true

        setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                // Dejamos que el sistema se encargue de la url
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

                return true
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    return shouldOverrideUrlLoading(view, request.url.toString())
                } else {
                    return super.shouldOverrideUrlLoading(view, request)
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                onLoad?.invoke()
            }
        })
    }

    /**
     * Ejecuta un método de JavaScript en el webview.
     */
    fun execute(method: String) {
        loadUrl("javascript:" + method)
    }

    /**
     * Carga el contenido HTML especificado en el webview.
     */
    abstract fun loadContent(content: T, onLoad: (() -> Unit)? = null)
}