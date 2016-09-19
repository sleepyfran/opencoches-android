package io.spaceisstrange.opencoches.ui.activities

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.adapters.PostAdapter
import io.spaceisstrange.opencoches.api.model.Post
import io.spaceisstrange.opencoches.api.net.ApiConstants
import io.spaceisstrange.opencoches.api.rx.FCThreadObservable
import io.spaceisstrange.opencoches.api.rx.FCThreadPagesObservable
import io.spaceisstrange.opencoches.ui.dialogs.UserProfileDialog
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
     * Layout manager de la recycler view
     */
    val layoutManager = PreCacheLayoutManager(this)

    /**
     * Página actual del hilo
     */
    var threadActualPage = 1

    /**
     * Variables para hacer el RecyclerView infinito
     */
    var previousTotalItemCount = 0
    var visibleItemThreshold = 15
    var firstVisibleItemPosition = 0
    var visibleItemCount = 0
    var totalItemCount = 0
    var loadingContent = true

    /**
     * Carga el hilo del link especificado y añade el contenido al adapter
     */
    fun loadThread(link: String, page: Int?, onLoad: (posts: MutableList<Post>) -> Unit) {
        FCThreadObservable.create(link, page).subscribe(
                {
                    posts ->

                    // Ocultamos la carita del error si no lo está ya
                    hideErrorMessage(vError)

                    // ¡Hemos cargado el hilo!
                    srlPostList.isRefreshing = false
                    onLoad(posts)
                },
                {
                    error ->

                    srlPostList.isRefreshing = false

                    // Mostramos la carita cuca con el error
                    postAdapter.updatePosts(mutableListOf())
                    showErrorMessage(vError)
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
        rvPostList.layoutManager = layoutManager

        // Mostramos el hilo como cargando
        srlPostList.isRefreshing = true

        // Configuramos la opción del onClick sobre la foto del usuario
        postAdapter.onUserClick = {
            post ->

            // Mostramos el Bottom Sheet con las opciones
            UserProfileDialog.newInstance(post.posterUsername, post.posterId, post.posterPictureLink)
                    .show(supportFragmentManager, null)
        }

        // Obtenemos los posts del hilo y populamos la RecyclerView con ellos
        loadThread(threadLink, null, { posts -> postAdapter.updatePosts(posts) })

        // Cargamos también el hilo cuando el usuario haga un swipe to refresh
        srlPostList.setOnRefreshListener {
            loadThread(threadLink, null, { posts -> postAdapter.updatePosts(posts) })
        }

        // Configuramos la RecyclerView para ser "infinita"
        // Basado en: http://stackoverflow.com/a/26561717
        rvPostList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                totalItemCount = layoutManager.itemCount
                visibleItemCount = recyclerView.childCount

                if (loadingContent) {
                    if (totalItemCount > previousTotalItemCount) {
                        loadingContent = false
                        previousTotalItemCount = totalItemCount
                    }
                }

                if (!loadingContent && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItemPosition + visibleItemThreshold)) {
                    // Estamos en el final, así que cargamos más contenido de la siguiente página
                    loadingContent = true
                    threadActualPage++

                    // Obtenemos el número total de páginas que tiene el hilo. Puede haber cambiado
                    // según hacíamos scroll, por lo que tenemos que comprobarlo cada vez que
                    // se recarga
                    FCThreadPagesObservable.create(threadLink).subscribe(
                            {
                                pages ->

                                if (threadActualPage <= pages) {
                                    // Cargamos más posts si hay más páginas
                                    srlPostList.isRefreshing = true

                                    loadThread(threadLink, threadActualPage, { posts -> postAdapter.addPosts(posts) })
                                }
                            },
                            {
                                error ->

                                // Silenciamos los errores :)
                            }
                    )
                }
            }
        })
    }
}