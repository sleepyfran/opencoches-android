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

package io.spaceisstrange.opencoches.data.database

import io.realm.Realm
import io.realm.RealmChangeListener
import io.spaceisstrange.opencoches.data.database.model.FavoriteThread
import io.spaceisstrange.opencoches.data.database.model.Settings
import io.spaceisstrange.opencoches.data.database.model.UserData

/**
 * Gestor de nuestra base de datos, donde guardaremos todos los datos necesarios para el funcionamiento
 * de la aplicación.
 */
class DatabaseManager {
    companion object {
        /**
         * Retorna la instancia de Realm para cuando sea necesaria.
         */
        fun realmInstance(): Realm {
            return Realm.getDefaultInstance()
        }

        /**
         * Verifica si tenemos datos del usuario guardados en la base de datos.
         */
        fun hasUserData(): Boolean {
            val realm: Realm = Realm.getDefaultInstance()

            val hasUserData = !realm.where(UserData::class.java)
                    .findAll()
                    .isEmpty()

            return hasUserData
        }

        /**
         * Guarda los datos del usuario en la base de datos.
         */
        fun saveUserData(userData: UserData) {
            val realm: Realm = Realm.getDefaultInstance()

            realm.executeTransaction {
                realm.copyToRealmOrUpdate(userData)
            }
        }

        /**
         * Retorna los datos del usuario de la base de datos.
         */
        fun userData(): UserData? {
            val realm: Realm = Realm.getDefaultInstance()

            return realm.where(UserData::class.java)
                    .findFirst()
        }

        /**
         * Borra los datos del usuario de la base de datos.
         */
        fun removeUserData() {
            val realm: Realm = Realm.getDefaultInstance()

            val userData = realm.where(UserData::class.java).findAll()

            realm.executeTransaction {
                userData.deleteAllFromRealm()
            }
        }

        /**
         * Guarda un nuevo hilo favorito en la base de datos.
         */
        fun saveFavoriteThread(thread: FavoriteThread) {
            val realm: Realm = Realm.getDefaultInstance()

            realm.executeTransaction {
                realm.copyToRealmOrUpdate(thread)
            }
        }

        /**
         * Retorna los hilos favoritos de la base de datos.
         */
        fun favoriteThreads(): List<FavoriteThread> {
            val realm: Realm = Realm.getDefaultInstance()

            return realm.where(FavoriteThread::class.java)
                    .findAll()
        }

        /**
         * Elimina el hilo especificado de la base de datos.
         */
        fun removeFavoriteThread(thread: FavoriteThread) {
            val realm: Realm = Realm.getDefaultInstance()

            val results = realm.where(FavoriteThread::class.java)
                    .equalTo("id", thread.id)
                    .findAll()

            realm.executeTransaction {
                results.deleteAllFromRealm()
            }
        }

        /**
         * Verifica si tenemos datos del usuario guardados en la base de datos.
         */
        fun hasSettings(): Boolean {
            val realm: Realm = Realm.getDefaultInstance()

            val hasSettings = !realm.where(Settings::class.java)
                    .findAll()
                    .isEmpty()

            return hasSettings
        }

        /**
         * Guarda los ajustes en la base de datos.
         */
        fun saveSettings(settings: Settings) {
            val realm: Realm = Realm.getDefaultInstance()

            realm.executeTransaction {
                realm.copyToRealmOrUpdate(settings)
            }
        }

        /**
         * Retorna los ajustes de la base de datos.
         */
        fun settings(): Settings {
            val realm: Realm = Realm.getDefaultInstance()

            return realm.where(Settings::class.java)
                    .findFirst()
        }

        /**
         * Añade un listener a la base de datos.
         */
        fun addListener(listener: RealmChangeListener<Realm>) {
            val realm: Realm = Realm.getDefaultInstance()

            realm.addChangeListener(listener)
        }

        /**
         * Elimina un listener de la base de datos.
         */
        fun removeListener(listener: RealmChangeListener<Realm>) {
            val realm: Realm = Realm.getDefaultInstance()

            realm.removeChangeListener(listener)
        }

        /**
         * Borra todos los datos almacenados en la base de datos.
         */
        fun nuke() {
            val realm: Realm = Realm.getDefaultInstance()

            realm.executeTransaction {
                realm.deleteAll()
            }
        }
    }
}