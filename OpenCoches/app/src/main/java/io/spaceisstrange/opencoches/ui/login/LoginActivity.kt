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

package io.spaceisstrange.opencoches.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.AccountManager
import io.spaceisstrange.opencoches.ui.subforumlist.SubforumListActivity
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Activity que permitirá al usuario iniciar sesión en la aplicación. Guardaremos sus datos en la base de datos
 * si el inicio de sesión es satisfactorio para así poder iniciar sesión automáticamente la próxima vez
 * que se abra la aplicación.
 */
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Intentamos iniciar sesión cuando el usuario pulse el botón
        login.setOnClickListener { attemptLogin() }
    }

    /**
     * Recoge los datos que ha puesto el usuario e intenta iniciar sesión en la página. Si el inicio de sesión
     * ha sido satisfactorio, guarda los datos en el base de datos y muestra la lista de subforos.
     */
    fun attemptLogin() {
        val username = loginUsername.text.toString()
        val password = loginPassword.text.toString()

        if (TextUtils.isEmpty(username)) {
            showUsernameError()
            return
        }

        if (TextUtils.isEmpty(password)) {
            showPasswordError()
            return
        }

        showLoading(true)

        AccountManager.login(username, password).subscribe(
                {
                    loggedIn ->

                    if (loggedIn) {
                        val subforumListIntent = Intent(this, SubforumListActivity::class.java)
                        startActivity(subforumListIntent)
                    } else {
                        showLoading(false)
                        showWrongDataError()
                    }
                },
                {
                    error ->

                    showLoading(false)
                    showWrongDataError()
                }
        )
    }

    /**
     * Muestra u oculta la animación de carga.
     */
    fun showLoading(show: Boolean) {
        if (show) {
            loginDetails?.visibility = View.GONE
            loading?.visibility = View.VISIBLE
        } else {
            loginDetails?.visibility = View.VISIBLE
            loading?.visibility = View.GONE
        }
    }

    /**
     * Muestra un error en la caja de nombre de usuario.
     */
    fun showUsernameError() {
        loginUsername?.error = getString(R.string.login_error_username)
    }

    /**
     * Muestra un error en la caja de contraseña.
     */
    fun showPasswordError() {
        loginPassword?.error = getString(R.string.login_error_password)
    }

    /**
     * Muestra un error indicando que los datos son incorrectos.
     */
    fun showWrongDataError() {
        val view = findViewById(android.R.id.content)
        Snackbar.make(view, getString(R.string.error_login_message), Snackbar.LENGTH_LONG).show()
    }
}