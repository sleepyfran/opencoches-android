package io.spaceisstrange.opencoches.ui.activities

import android.os.Bundle
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.adapters.PostAdapter
import io.spaceisstrange.opencoches.api.net.ApiConstants
import io.spaceisstrange.opencoches.api.rx.FCThreadObservable
import io.spaceisstrange.opencoches.utils.PreCacheLayoutManager
import kotlinx.android.synthetic.main.activity_thread.*

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

class ThreadActivity : BaseActivity() {
    /**
     * Adapter del post encargado de darnos esas preciosas vistas en forma de post
     */
    val postAdapter = PostAdapter()

    /**
     * Carga el hilo del link especificado y añade el contenido al adapter
     */
    fun loadThread(link: String) {
        FCThreadObservable.create(link).subscribe(
                {
                    posts ->

                    // ¡Hemos cargado el hilo!
                    srlPostList.isRefreshing = false
                    postAdapter.updatePosts(posts)
                },
                {
                    error ->

                    // Whoops!
                    srlPostList.isRefreshing = false

                    error.printStackTrace()

                    // TODO: Hacerse cargo, una vez más, de los malditos errores
                }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread)
        setSupportActionBar(toolbar)

        // Obtenemos el título y link del hilo de los extras
        val threadTitle = intent?.extras?.getString(ApiConstants.THREAD_TITLE_KEY)!!
        val threadLink = intent?.extras?.getString(ApiConstants.THREAD_LINK_KEY)!!

        // Mostramos la X en la toolbar
        showCloseButtonOnToolbar()

        // Mostramos el título del hilo
        showCustomTitleOnToolbar(threadTitle)

        // Configuramos la RecyclerView
        rvPostList.adapter = postAdapter
        rvPostList.layoutManager = PreCacheLayoutManager(this)

        // Mostramos el hilo como cargando
        srlPostList.isRefreshing = true

        // Obtenemos los posts del hilo y populamos la RecyclerView con ellos
        loadThread(threadLink)

        // Cargamos también el hilo cuando el usuario haga un swipe to refresh
        srlPostList.setOnRefreshListener {
            loadThread(threadLink)
        }
    }
}