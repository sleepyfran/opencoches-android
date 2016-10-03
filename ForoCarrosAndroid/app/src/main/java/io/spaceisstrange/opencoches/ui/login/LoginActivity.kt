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

package io.spaceisstrange.opencoches.ui.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.spaceisstrange.opencoches.AppModule
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.util.ActivityUtils
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {
    /**
     * Presenter asociado a la activity y al fragment
     */
    @Inject lateinit var loginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Creamos el fragment del Login
        val loginFragment = LoginFragment.newInstance()

        // Añadimos el fragment
        ActivityUtils.addFragmentToActivity(supportFragmentManager, loginFragment, R.id.fragment)

        // Inyectamos las dependencias de la activity y el fragment
        DaggerLoginComponent.builder()
                .loginModule(LoginModule(loginFragment))
                .appModule(AppModule(applicationContext))
                .build()
                .inject(this)
    }
}