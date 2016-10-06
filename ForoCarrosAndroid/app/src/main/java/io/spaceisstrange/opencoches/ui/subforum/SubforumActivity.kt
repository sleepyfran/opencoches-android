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

package io.spaceisstrange.opencoches.ui.subforum

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.spaceisstrange.opencoches.App
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.ui.common.baseactivity.BaseActivity
import io.spaceisstrange.opencoches.util.ActivityUtils
import kotlinx.android.synthetic.main.activity_subforum_list.*
import javax.inject.Inject

class SubforumActivity : BaseActivity() {
    companion object {
        /**
         * Clave asociada al extra
         */
        val SUBFORUM_LINK = "subforumLink"

        /**
         * Retorna un Intent con los parámetros necesarios para inicializar la activity
         */
        fun getStartIntent(context: Context, subforumLink: String): Intent {
            val startIntent = Intent(context, SubforumActivity::class.java)
            startIntent.putExtra(SUBFORUM_LINK, subforumLink)
            return startIntent
        }
    }

    /**
     * Presenter asociado a la activity y al fragment
     */
    @Inject lateinit var subforumPresenter: SubforumPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subforum)
        setSupportActionBar(toolbar)

        // Cargamos el link del subforo de los extras del intent
        val subforumLink = intent.extras?.getString(SUBFORUM_LINK)
                ?: throw IllegalArgumentException("No soy mago, no puedo cargar el subforo sin link")

        // Intentamos conseguir de nuevo el fragment anterior si existe
        var subforumFragment = supportFragmentManager.findFragmentById(R.id.fragment) as? SubforumFragment

        if (subforumFragment == null) {
            // Sino, lo creamos el fragment y lo añadimos
            subforumFragment = SubforumFragment.newInstance()
            ActivityUtils.addFragmentToActivity(supportFragmentManager, subforumFragment, R.id.fragment)
        }

        DaggerSubforumComponent.builder()
                .subforumModule(SubforumModule(subforumFragment, subforumLink))
                .sharedPreferencesUtilsComponent((application as App).sharedPrefsComponent)
                .build()
                .inject(this)
    }
}