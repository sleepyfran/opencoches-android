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

package io.spaceisstrange.opencoches.ui.common

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.view.Menu
import android.view.MenuItem
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.ui.favorites.FavoritesActivity
import io.spaceisstrange.opencoches.ui.search.SearchActivity
import io.spaceisstrange.opencoches.ui.settings.SettingsActivity

/**
 * Activity que sirve de base para todas las demás activities que quieran implementar la Toolbar por defecto.
 */
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.default_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val selectedId = item?.itemId

        if (selectedId == android.R.id.home) {
            onBackPressed()
            return true
        } else if (selectedId == R.id.menu_settings) {
            // Iniciamos la activity de ajustes
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        } else if (selectedId == R.id.menu_search) {
            // Iniciamos la activity de búsqueda
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        } else if (selectedId == R.id.menu_favorites) {
            // Iniciamos la activity de favoritos
            val favoritesIntent = Intent(this, FavoritesActivity::class.java)
            startActivity(favoritesIntent)
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * Establece el título que se muestra en la Toolbar de la Activity.
     */
    fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    /**
     * Como su nombre indica, muestra la "X" como acción del botón home de la toolbar.
     */
    fun showToolbarCloseButton() {
        val closeButton = ContextCompat.getDrawable(this, R.drawable.ic_close_white)
        supportActionBar?.setHomeAsUpIndicator(closeButton)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}