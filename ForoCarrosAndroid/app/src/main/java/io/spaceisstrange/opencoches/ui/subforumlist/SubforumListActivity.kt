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

package io.spaceisstrange.opencoches.ui.subforumlist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import io.spaceisstrange.opencoches.App
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.api.userdata.UserId
import io.spaceisstrange.opencoches.ui.common.baseactivity.BaseActivity
import io.spaceisstrange.opencoches.util.ActivityUtils
import kotlinx.android.synthetic.main.activity_subforum_list.*
import javax.inject.Inject

class SubforumListActivity : BaseActivity() {
    /**
     * Presenter asociado a la activity y al fragment
     */
    @Inject lateinit var subforumListPresenter: SubforumListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subforum_list)
        setSupportActionBar(toolbar)

        // Intentamos conseguir de nuevo el fragment anterior si existe
        var subforumListFragment =
                supportFragmentManager.findFragmentById(R.id.fragment) as? SubforumListFragment

        if (subforumListFragment == null) {
            // Sino, lo creamos el fragment y lo añadimos
            subforumListFragment = SubforumListFragment.newInstance()
            ActivityUtils.addFragmentToActivity(supportFragmentManager, subforumListFragment, R.id.fragment)
        }

        // Inyectamos las dependencias en la activity y el fragment
        DaggerSubforumListComponent.builder()
                .subforumListModule(SubforumListModule(subforumListFragment))
                .sharedPreferencesUtilsComponent((application as App).sharedPrefsComponent)
                .build()
                .inject(this)
    }
}