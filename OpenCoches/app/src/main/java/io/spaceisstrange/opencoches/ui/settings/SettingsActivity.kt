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

package io.spaceisstrange.opencoches.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AlertDialog
import android.view.Menu
import de.psdev.licensesdialog.LicensesDialog
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.AccountManager
import io.spaceisstrange.opencoches.data.database.DatabaseManager
import io.spaceisstrange.opencoches.ui.common.BaseActivity
import io.spaceisstrange.opencoches.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_settings.*

/**
 * Activity que muestra los ajustes de la aplicación.
 */
class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)

        // Configuramos la toolbar
        supportActionBar?.title = getString(R.string.settings_title)
        showToolbarCloseButton()

        // Establecemos elevación a las tarjetas
        ViewCompat.setElevation(settingsVisual, 5.toFloat())
        ViewCompat.setElevation(settingsGeneral, 5.toFloat())
        ViewCompat.setElevation(settingsAbout, 5.toFloat())
        updateUi()

        settingsShowSticky.setOnClickListener { showSticky.isChecked = !showSticky.isChecked }
        showSticky.setOnCheckedChangeListener { buttonView, isChecked -> onShowSticky(isChecked) }

        signOut.setOnClickListener { onSignOut() }

        settingsLicenses.setOnClickListener { onLicenses() }
        settingsGhRepository.setOnClickListener { onRepository() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    /**
     * Actualiza los datos mostrados por pantalla desde la base de datos y la información de la app.
     */
    fun updateUi() {
        // Cargamos la versión de la aplicación
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        appVersion.text = getString(R.string.settings_about_version_release, packageInfo.versionName)

        val settings = DatabaseManager.settings()

        showSticky.isChecked = settings.showSticky!!
    }

    /**
     * Llamado cuando el campo de mostrar con chincheta es pulsado.
     */
    fun onShowSticky(isChecked: Boolean) {
        DatabaseManager.realmInstance().executeTransaction {
            val settings = DatabaseManager.settings()
            settings.showSticky = isChecked
        }
    }

    /**
     * Llamado cuando el botón de cerrar sesión es pulsado.
     */
    fun onSignOut() {
        // Mostramos un diálogo preguntando por confirmación
        AlertDialog.Builder(this)
                .setMessage(getString(R.string.settings_dialog_sign_out_title))
                .setPositiveButton(getString(R.string.general_yes), {
                    dialog, integer ->

                    // Borramos los datos guardados
                    AccountManager.deleteSession()

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

    /**
     * Llamado cuando el botón de las licencias es pulsado.
     */
    fun onLicenses() {
        LicensesDialog.Builder(this)
                .setNotices(R.raw.notices)
                .build()
                .show()
    }

    /**
     * Llamado cuando el botón de ver el código fuente es pulsado.
     */
    fun onRepository() {
        val repositoryIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/spaceisstrange/opencoches"))
        startActivity(repositoryIntent)
    }
}