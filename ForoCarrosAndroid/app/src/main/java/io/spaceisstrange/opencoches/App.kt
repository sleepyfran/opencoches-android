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

package io.spaceisstrange.opencoches

import android.app.Application
import io.spaceisstrange.opencoches.data.sharedpreferences.DaggerSharedPreferencesUtilsComponent
import io.spaceisstrange.opencoches.data.sharedpreferences.SharedPreferencesUtilsComponent
import io.spaceisstrange.opencoches.data.sharedpreferences.SharedPreferencesUtilsModule

class App : Application() {
    /**
     * Componente con las SharedPreferences
     */
    lateinit var sharedPrefsComponent: SharedPreferencesUtilsComponent

    override fun onCreate() {
        super.onCreate()

        sharedPrefsComponent = DaggerSharedPreferencesUtilsComponent.builder()
                .appModule(AppModule(applicationContext))
                .sharedPreferencesUtilsModule(SharedPreferencesUtilsModule())
                .build()
    }
}