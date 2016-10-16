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

import android.os.Bundle
import com.klinker.android.sliding.SlidingActivity
import io.spaceisstrange.opencoches.App
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.bus.Bus
import io.spaceisstrange.opencoches.data.bus.events.SmilySelectedEvent
import io.spaceisstrange.opencoches.util.ActivityUtils
import javax.inject.Inject

class SmiliesActivity : SlidingActivity() {
    /**
     * Presenter asociado a este diálogo
     */
    @Inject lateinit var smiliesPresenter: SmiliesPresenter

    /**
     * Bus al que notificar cuando seleccionemos un smily
     */
    @Inject lateinit var bus: Bus

    override fun init(savedInstanceState: Bundle?) {
        disableHeader()
        setContent(R.layout.activity_smilies)

        // Intentamos conseguir de nuevo el fragment anterior si existe
        var smiliesFragment = supportFragmentManager.findFragmentById(R.id.fragment) as? SmiliesFragment

        if (smiliesFragment == null) {
            // Sino, lo creamos el fragment y lo añadimos
            smiliesFragment = SmiliesFragment.newInstance()
            ActivityUtils.addFragmentToActivity(supportFragmentManager, smiliesFragment, R.id.fragment)
        }

        // Inyectamos la activity
        DaggerSmiliesComponent.builder()
                .busComponent((application as App).busComponent)
                .smiliesModule(SmiliesModule(this, smiliesFragment))
                .build()
                .inject(this)

        // Nos subscribimos al bus para finalizar la activity cuando el usuario pulse un smily
        bus.observable().subscribe(
                {
                    event ->

                    if (event is SmilySelectedEvent) {
                        runOnUiThread {
                            finish()
                        }
                    }
                },
                {
                    error ->

                    // Nada, silenciamos
                }
        )
    }
}