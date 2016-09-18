package io.spaceisstrange.opencoches.ui.activities

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.utils.LoginUtils
import io.spaceisstrange.opencoches.utils.SharedPreferencesUtils

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

open class BaseActivity : AppCompatActivity() {

    /**
     * Como su nombre indica muestra un título personalizado en la toolbar. Difícil. Magic potagic
     */
    fun showCustomTitleOnToolbar(title: String) {
        supportActionBar?.title = title
    }

    /**
     * Como su nombre indica muestra el nombre de usuario en la toolbar. Difícil. Magic potagic
     */
    fun showUsernameOnToolbar() {
        showCustomTitleOnToolbar(SharedPreferencesUtils.getUsername(this))
    }

    /**
     * Como su nombre indica muestra la "X" como acción del botón home de la toolbar
     */
    fun showCloseButtonOnToolbar() {
        val closeButton = ContextCompat.getDrawable(this, R.drawable.ic_close_white)
        supportActionBar?.setHomeAsUpIndicator(closeButton)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Muestra la carita cuca con el error. Esto es malo. Aunque sea cuca
     */
    fun showErrorMessage(errorView: View) {
        errorView.visibility = View.VISIBLE
    }

    /**
     * Oculta la carita cuca con el error. Esto es bueno
     */
    fun hideErrorMessage(errorView: View) {
        errorView.visibility = View.GONE
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
            LoginUtils.logOutUser(this)

            // Mostramos la activity de iniciar sesión limpiando la pila actual
            val loginIntent = Intent(this, LoginActivity::class.java)
            loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(loginIntent)
            finish()

            return true
        }

        // TODO: Hacerse cargo del resto del menú

        return super.onOptionsItemSelected(item)
    }
}