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

package io.spaceisstrange.opencoches.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.klinker.android.sliding.SlidingActivity
import io.spaceisstrange.opencoches.App
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.AccountManager
import io.spaceisstrange.opencoches.util.ActivityUtils
import javax.inject.Inject

class ProfileActivity : SlidingActivity() {
    /**
     * Presenter asociado a la activity y el fragment
     */
    @Inject lateinit var profilePresenter: ProfilePresenter

    companion object {
        /**
         * Clave asociada al link de donde se cargará el usuario
         */
        val USER_ID = "userLink"

        /**
         * Retorna un Intent con los parámetros necesarios para inicializar la activity
         */
        fun getStartIntent(context: Context, userId: String): Intent {
            val startIntent = Intent(context, ProfileActivity::class.java)
            startIntent.putExtra(USER_ID, userId)
            return startIntent
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        // Intentamos obtener los datos de los extras del intent
        val userId = intent.extras?.getString(USER_ID) ?: intent.dataString

        // Dado que esta activity puede ser abierta por el usuario sin estar logueado lo comprobamos primero
        val sharedPrefs = (application as App).sharedPrefsComponent.getSharedPreferencesUtils()

        if (AccountManager.isUserLoggedIn(sharedPrefs)) {
            // Intentamos loguear al usuario. Si ocurre algún error lo enviamos a la pantalla de login
            AccountManager.loginWithSavedCredentials(sharedPrefs,
                    {
                        success ->

                        if (!success) {
                            ActivityUtils.showLogin(this)
                        }

                        // Configuramos la activity
                        disableHeader()
                        setContent(R.layout.activity_profile)

                        // Intentamos conseguir de nuevo el fragment anterior si existe
                        var profileFragment = supportFragmentManager.findFragmentById(R.id.fragment) as? ProfileFragment

                        if (profileFragment == null) {
                            // Sino, lo creamos y añadimos
                            profileFragment = ProfileFragment.newInstance()
                            ActivityUtils.addFragmentToActivity(supportFragmentManager, profileFragment, R.id.fragment)
                        }

                        // Inyectamos la activity
                        DaggerProfileComponent.builder()
                                .profileModule(ProfileModule(profileFragment, userId))
                                .build()
                                .inject(this)
                    })
        } else {
            ActivityUtils.showLogin(this)
        }
    }
}