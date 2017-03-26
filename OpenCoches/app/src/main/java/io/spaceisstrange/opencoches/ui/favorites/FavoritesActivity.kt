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

package io.spaceisstrange.opencoches.ui.favorites

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.klinker.android.sliding.SlidingActivity
import io.realm.Realm
import io.realm.RealmChangeListener
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.database.DatabaseManager
import io.spaceisstrange.opencoches.ui.thread.ThreadActivity
import kotlinx.android.synthetic.main.activity_favorites.*

/**
 * Activity para mostrar los hilos favoritos del usuario.
 */
class FavoritesActivity : SlidingActivity() {
    /**
     * Adapter de los hilos favoritos del usuario.
     */
    val adapter = FavoritesAdapter(
            {
                // Cuando el usuario hace click normal
                thread ->

                // Iniciamos la activity de hilos con el hilo que se ha pulsado
                val threadIntent = ThreadActivity.startIntent(this, thread.link!!)
                startActivity(threadIntent)
            },
            {
                // Cuando el usuario hace un "long click"
                thread ->

                // Borramos el hilo de la base de datos previa confirmación del usuario
                AlertDialog.Builder(this)
                        .setTitle(R.string.favorites_remove_dialog_title)
                        .setMessage(R.string.favorites_remove_dialog_message)
                        .setPositiveButton(R.string.favorites_remove_dialog_yes, {
                            dialog, which ->

                            DatabaseManager.removeFavoriteThread(thread)
                        })
                        .setNegativeButton(R.string.favorites_remove_dialog_no, {
                            dialog, which ->

                            // Nada
                        }).show()
            })

    /**
     * Listener de la base de datos.
     */
    lateinit var realmListener: RealmChangeListener<Realm>

    override fun init(savedInstanceState: Bundle?) {
        setContent(R.layout.activity_favorites)
        title = getString(R.string.favorites_title)

        // Configuramos la lista de hilos favoritos
        favorites.adapter = adapter
        favorites.layoutManager = LinearLayoutManager(this)

        // Cargamos los hilos de la base de datos
        update()

        // Establecemos un listener en la base de datos para recargar los datos cuando nos notifique
        realmListener = RealmChangeListener { realm -> update() }
        DatabaseManager.addListener(realmListener)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Eliminamos el listener
        DatabaseManager.removeListener(realmListener)
    }

    /**
     * Actualiza el contenido del adapter de los favoritos.
     */
    fun update() {
        val threads = DatabaseManager.favoriteThreads()

        if (threads.isNotEmpty()) {
            showEmpty(false)
            adapter.update(threads)
        } else {
            // Como no hay nada añadido, mostramos el mensaje de vacío
            showEmpty(true)
        }
    }

    /**
     * Muestra u oculta el mensaje de que esto está vacío.
     */
    fun showEmpty(show: Boolean) {
        if (show) {
            favorites.visibility = View.GONE
            empty.visibility = View.VISIBLE
        } else {
            favorites.visibility = View.VISIBLE
            empty.visibility = View.GONE
        }
    }
}