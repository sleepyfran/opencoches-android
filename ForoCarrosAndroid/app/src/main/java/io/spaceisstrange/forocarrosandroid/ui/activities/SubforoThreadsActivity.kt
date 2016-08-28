package io.spaceisstrange.forocarrosandroid.ui.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.spaceisstrange.forocarrosandroid.R
import io.spaceisstrange.forocarrosandroid.adapters.SubforoThreadsAdapter
import io.spaceisstrange.forocarrosandroid.api.net.ApiConstants
import io.spaceisstrange.forocarrosandroid.api.rx.FCSubforoThreadsObservable
import kotlinx.android.synthetic.main.activity_subforo_threads.*

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

class SubforoThreadsActivity : BaseActivity() {

    /**
     * Página actual del subforo
     */
    var subforoActualPage = 1

    /**
     * Variables para hacer el RecyclerView infinito
     */
    var previousTotalItemCount = 0
    var visibleItemThreshold = 15
    var firstVisibleItemPosition = 0
    var visibleItemCount = 0
    var totalItemCount = 0
    var loadingContent = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subforo_threads)
        setSupportActionBar(toolbar)

        // Obtenemos el título del foro y el link de los extras del intent
        val subforoTitle = intent?.extras?.getString(ApiConstants.THREAD_TITLE_KEY)!!
        val subforoLink = intent?.extras?.getString(ApiConstants.THREAD_LINK_KEY)!!

        // Mostramos el título del foro en la toolbar
        showCustomTitleOnToolbar(subforoTitle)

        // Configuramos la RecyclerView
        val threadsAdapter = SubforoThreadsAdapter()

        // Configuramos el onClick del adapter para que abra el hilo que el usuario ha pulsado
        threadsAdapter.onClick = {
            thread ->

            // TODO: Completar
        }

        rvThreadList.adapter = threadsAdapter
        val layoutManager = LinearLayoutManager(this)
        rvThreadList.layoutManager = layoutManager

        // Obtenemos los hilos del subforo y populamos la recycler view con ellos
        FCSubforoThreadsObservable.create(subforoLink, subforoActualPage).subscribe(
                {
                    threads ->

                    threadsAdapter.updateThreads(threads)
                },
                {
                    error ->

                    // TODO: Hacerse cargo de los malditos errores
                }
        )

        // Configuramos la RecyclerView para ser "infinita"
        // Basado en: http://stackoverflow.com/a/26561717
        rvThreadList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                    subforoActualPage++

                    FCSubforoThreadsObservable.create(subforoLink, subforoActualPage).subscribe(
                            {
                                threads ->

                                threadsAdapter.addThreads(threads, {
                                    thread ->

                                    // Evitamos los stickies ya que los tenemos añadidos ya
                                    if (thread.isSticky) false
                                    else true
                                })
                            },
                            {
                                error ->

                                // TODO: Hacerse cargo de los malditos errores
                            }
                    )
                }
            }
        })
    }
}