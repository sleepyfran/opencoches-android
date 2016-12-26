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

package io.spaceisstrange.opencoches.ui.newthread

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import io.spaceisstrange.opencoches.App
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.ui.common.baseactivity.BaseActivity
import io.spaceisstrange.opencoches.util.ActivityUtils
import kotlinx.android.synthetic.main.activity_new_thread.*
import javax.inject.Inject

class NewThreadActivity : BaseActivity() {
    /**
     * Presenter asociado al fragment
     */
    @Inject lateinit var newThreadPresenter: NewThreadPresenter

    companion object {
        /**
         * Clave asociada al link del subforo donde vamos a crear el nuevo hilo
         */
        val SUBFORUM_LINK = "subforumLink"

        /**
         * Clave asociada al nombre del subforo donde vamos a crear el nuevo hilo
         */
        val SUBFORUM_NAME = "subforumName"

        /**
         * Retorna un Intent con los parámetros necesarios para inicializar la activity
         */
        fun getStartIntent(context: Context, subforumLink: String, subforumName: String = ""): Intent {
            val startIntent = Intent(context, NewThreadActivity::class.java)
            startIntent.putExtra(SUBFORUM_LINK, subforumLink)
            startIntent.putExtra(SUBFORUM_NAME, subforumName)
            return startIntent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_thread)
        setSupportActionBar(toolbar)

        // Intentamos conseguir el link del subforo donde vamos a crear el hilo
        val subforumLink = intent?.extras?.getString(SUBFORUM_LINK)
                ?: throw IllegalArgumentException("Necesito el link del subforo para trabajar")

        // Intentamos conseguir el nombre del subforo. Siempre va a existir aunque sea vacío
        val subforumName = intent?.extras?.getString(SUBFORUM_NAME)

        // Configuramos la toolbar
        supportActionBar?.title = getString(R.string.new_thread_toolbar_title, subforumName)
        showCloseButtonOnToolbar()

        // Intentamos conseguir de nuevo el fragment anterior si existe
        var subforumFragment = supportFragmentManager.findFragmentById(R.id.fragment) as? NewThreadFragment

        if (subforumFragment == null) {
            // Sino, lo creamos el fragment y lo añadimos
            subforumFragment = NewThreadFragment.newInstance()
            ActivityUtils.addFragmentToActivity(supportFragmentManager, subforumFragment, R.id.fragment)
        }

        // Inyectamos la activity
        DaggerNewThreadComponent.builder()
                .sharedPreferencesUtilsComponent((application as App).sharedPrefsComponent)
                .newThreadModule(NewThreadModule(subforumFragment, subforumLink))
                .build()
                .inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // No queremos el menú base
        return true
    }
}