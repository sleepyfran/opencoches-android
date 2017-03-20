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

package io.spaceisstrange.opencoches.ui.subforumlist

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.api.subforumlist.SubforumList
import io.spaceisstrange.opencoches.ui.common.BaseActivity
import io.spaceisstrange.opencoches.ui.subforum.SubforumActivity
import kotlinx.android.synthetic.main.activity_subforum_list.*

/**
 * Activity que muestra la lista de subforos disponibles en el foro. Dicha lista es cargada directamente desde el foro
 * en lugar de mostrar una lista "a pelo", por si al jefe le da por querer cambiarnos los subforos o trabajar un poco.
 */
class SubforumListActivity : BaseActivity() {
    /**
     * Adapter de la lista de subforos
     */
    val adapter = SubforumListAdapter({
        subforum ->

        // Cargamos el subforo seleccionado
        val subforumIntent = SubforumActivity.startIntent(this, subforum.title, subforum.link)
        startActivity(subforumIntent)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subforum_list)
        setSupportActionBar(toolbar)

        // Configuramos la RecyclerView
        subforumList.adapter = adapter
        subforumList.layoutManager = LinearLayoutManager(this)

        loadSubforums()
    }

    /**
     * Carga la lista de subforos y los añade a nuestro adapter.
     */
    fun loadSubforums() {
        SubforumList().observable().subscribe(
                {
                    subforums ->

                    showError(false)
                    showLoading(false)
                    adapter.update(subforums)
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
    fun showLoading(enabled: Boolean) {
        if (enabled) {
            subforumList?.visibility = View.GONE
            loading?.visibility = View.VISIBLE
        } else {
            subforumList?.visibility = View.VISIBLE
            loading?.visibility = View.GONE
        }
    }

    /**
     * Muestra u oculta el mensaje de error.
     */
    fun showError(show: Boolean) {
        if (show) {
            loading?.visibility = View.GONE
            subforumList?.visibility = View.GONE
            error?.visibility = View.VISIBLE
        } else {
            loading?.visibility = View.VISIBLE
            subforumList?.visibility = View.VISIBLE
            error?.visibility = View.GONE
        }
    }
}