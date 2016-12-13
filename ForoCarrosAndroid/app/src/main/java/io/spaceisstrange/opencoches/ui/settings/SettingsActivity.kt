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

package io.spaceisstrange.opencoches.ui.settings

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AlertDialog
import android.view.Menu
import de.psdev.licensesdialog.LicensesDialog
import io.spaceisstrange.opencoches.App
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.AccountManager
import io.spaceisstrange.opencoches.ui.common.baseactivity.BaseActivity
import io.spaceisstrange.opencoches.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)

        // Configuramos la toolbar
        supportActionBar?.title = getString(R.string.settings_title)
        showCloseButtonOnToolbar()

        // Ponemos elevación a las tarjetas
        ViewCompat.setElevation(llSettingsVisual, 5.toFloat())
        ViewCompat.setElevation(llSettingsGeneral, 5.toFloat())
        ViewCompat.setElevation(llSettingsAbout, 5.toFloat())

        // Cargamos la versión de la aplicación
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            tvAboutAppVersion.text = getString(R.string.settings_about_version_release, packageInfo.versionName)
        } catch (e: PackageManager.NameNotFoundException) {
            // Esto no debería jamás en la vida...
            e.printStackTrace()
            // ...pero un printStackTrace jamás hizo daño a nadie
        }

        // Cerramos sesión cuando se pulse el dichoso botón
        btnSettingsSignOut.setOnClickListener { onSignOut() }

        // Mostramos las licencias cuando se pulse el botón
        llSettingsLicenses.setOnClickListener {
            LicensesDialog.Builder(this)
                    .setNotices(R.raw.notices)
                    .build()
                    .show()
        }

        // Inyectamos la activity
        DaggerSettingsComponent.builder()
                .sharedPreferencesUtilsComponent((application as App).sharedPrefsComponent)
                .build()
                .inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    fun onSignOut() {
        // Mostramos un diálogo preguntando por confirmación
        AlertDialog.Builder(this)
                .setMessage(getString(R.string.settings_dialog_sign_out_title))
                .setPositiveButton(getString(R.string.general_yes), {
                    dialog, integer ->

                    // Borramos los datos guardados
                    AccountManager.deleteSession(sharedPreferences)

                    // Mostramos la pantalla de inicio
                    val loginIntent = Intent(this, LoginActivity::class.java)
                    loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(loginIntent)
                    finish()
                })
                .setNegativeButton(getString(R.string.general_no), null)
                .create()
                .show()
    }
}