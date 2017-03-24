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

package io.spaceisstrange.opencoches.ui.views.editor.smilies

import android.os.Bundle
import android.view.View
import com.klinker.android.sliding.SlidingActivity
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.api.smilies.Smilies
import io.spaceisstrange.opencoches.data.bus.Bus
import io.spaceisstrange.opencoches.data.bus.events.SmilySelectedEvent
import kotlinx.android.synthetic.main.activity_smilies.*

/**
 * Activity que se encarga de mostrar la lista de smilies para introducirla en el editor de texto.
 */
class SmiliesActivity : SlidingActivity() {
    override fun init(savedInstanceState: Bundle?) {
        disableHeader()
        setContent(R.layout.activity_smilies)

        // Configuramos la WebView
        smiliesContent.onSmilyClick = {
            smilyCode ->

            Bus.instance.publish(SmilySelectedEvent(smilyCode))
        }

        // Cargamos los datos
        loadSmilies()
    }

    /**
     * Carga los smilies y los muestra en pantalla.
     */
    fun loadSmilies() {
        showLoading(true)

        Smilies().observable().subscribe(
                {
                    smilies ->

                    showError(false)
                    smiliesContent.loadContent(smilies, {
                        showLoading(false)
                    })
                },
                {
                    error ->

                    showError(true)
                }
        )
    }

    /**
     * Muestra u oculta la animación de carga.
     */
    fun showLoading(show: Boolean) {
        if (show) {
            smiliesContent?.visibility = View.GONE
            loading?.visibility = View.VISIBLE
        } else {
            smiliesContent?.visibility = View.VISIBLE
            loading?.visibility = View.GONE
        }
    }

    /**
     * Muestra u oculta el mensaje de error.
     */
    fun showError(show: Boolean) {
        if (show) {
            smiliesContent?.visibility = View.GONE
            error?.visibility = View.VISIBLE
        } else {
            smiliesContent?.visibility = View.VISIBLE
            error?.visibility = View.GONE
        }
    }
}