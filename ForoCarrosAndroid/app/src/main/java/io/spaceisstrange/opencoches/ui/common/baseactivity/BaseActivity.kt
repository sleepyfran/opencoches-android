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

package io.spaceisstrange.opencoches.ui.common.baseactivity

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.CookiesCache
import io.spaceisstrange.opencoches.data.sharedpreferences.SharedPreferencesUtils
import io.spaceisstrange.opencoches.ui.login.LoginActivity
import javax.inject.Inject

open class BaseActivity : AppCompatActivity() {
    /**
     * SharedPreferences de la aplicación
     */
    @Inject lateinit var sharedPreferences: SharedPreferencesUtils

    /**
     * Como su nombre indica muestra la "X" como acción del botón home de la toolbar
     */
    fun showCloseButtonOnToolbar() {
        val closeButton = ContextCompat.getDrawable(this, R.drawable.ic_close_white)
        supportActionBar?.setHomeAsUpIndicator(closeButton)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
        } else if (selectedId == R.id.menu_sign_out) {
            // Borramos los datos guardados
            sharedPreferences.removePreferences()
            CookiesCache.cookies = null

            // Mostramos la pantalla de inicio
            val loginIntent = Intent(this, LoginActivity::class.java)
            loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(loginIntent)
            finish()

            return true
        }

        // TODO: Hacerse cargo del resto del menú

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}