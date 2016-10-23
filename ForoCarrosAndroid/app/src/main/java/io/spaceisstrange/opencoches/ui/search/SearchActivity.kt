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

package io.spaceisstrange.opencoches.ui.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import io.spaceisstrange.opencoches.App
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.ui.common.baseactivity.BaseActivity
import io.spaceisstrange.opencoches.util.ActivityUtils
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : BaseActivity() {
    /**
     * Presenter asociado a la activity y al fragment
     */
    @Inject lateinit var searchPresenter: SearchPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)

        // Intentamos conseguir de nuevo el fragment anterior si existe
        var searchFragment = supportFragmentManager.findFragmentById(R.id.fragment) as? SearchFragment

        if (searchFragment == null) {
            // Sino, lo creamos el fragment y lo añadimos
            searchFragment = SearchFragment.newInstance()
            ActivityUtils.addFragmentToActivity(supportFragmentManager, searchFragment, R.id.fragment)
        }

        // Inyectamos las dependencias de la activity
        DaggerSearchComponent.builder()
                .searchModule(SearchModule(searchFragment))
                .sharedPreferencesUtilsComponent((application as App).sharedPrefsComponent)
                .build()
                .inject(this)

        // Nos subscribimos a la barra de búsqueda para saber cuándo se ha realizado una
        fsvSearch.setOnSearchListener(object : FloatingSearchView.OnSearchListener {
            override fun onSearchAction(currentQuery: String?) {
                if (currentQuery != null) {
                    // Notificamos al fragment
                    searchFragment?.onSearch?.invoke(currentQuery)
                }
            }

            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
                // Nada de nada
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // No queremos el menú de la activity base
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // No queremos el menú de la activity base
        return true
    }
}