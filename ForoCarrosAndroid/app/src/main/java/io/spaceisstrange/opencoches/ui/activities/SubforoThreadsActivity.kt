package io.spaceisstrange.opencoches.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.adapters.SubforoThreadsAdapter
import io.spaceisstrange.opencoches.api.model.SubforoThread
import io.spaceisstrange.opencoches.api.net.ApiConstants
import io.spaceisstrange.opencoches.api.rx.FCSubforoThreadsObservable
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

    /**
     * Adapter de los hilos
     */
    val threadsAdapter = SubforoThreadsAdapter()

    /**
     * Carga los hilos del subforo especificado en la página especificada y realiza la acción
     * que se especifica en el método pasado cuando se haya terminado de cargar el contenido
     */
    fun loadThreads(subforoLink: String, onLoad: (threads: MutableList<SubforoThread>) -> Unit) {
        FCSubforoThreadsObservable.create(subforoLink, subforoActualPage).subscribe(
                {
                    threads ->

                    // Ocultamos la carita del error si no lo está ya
                    hideErrorMessage(vError)

                    // Hemos terminado de cargar el contenido
                    srlThreadList.isRefreshing = false
                    onLoad(threads)
                },
                {
                    error ->

                    srlThreadList.isRefreshing = false

                    // Mostramos la carita cuca con el error
                    threadsAdapter.updateThreads(mutableListOf())
                    showErrorMessage(vError)
                }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subforo_threads)
        setSupportActionBar(toolbar)

        // Obtenemos el título del foro y el link de los extras del intent
        val subforoTitle = intent?.extras?.getString(ApiConstants.THREAD_TITLE_KEY)!!
        val subforoLink = intent?.extras?.getString(ApiConstants.THREAD_LINK_KEY)!!

        // Mostramos el título del foro en la toolbar
        showCustomTitleOnToolbar(subforoTitle)

        // Configuramos el onClick del adapter para que abra el hilo que el usuario ha pulsado
        threadsAdapter.onClick = {
            thread ->

            val threadIntent = Intent(this, ThreadActivity::class.java)
            threadIntent.putExtra(ApiConstants.THREAD_TITLE_KEY, thread.title)
            threadIntent.putExtra(ApiConstants.THREAD_LINK_KEY, thread.link)
            startActivity(threadIntent)
        }

        rvThreadList.adapter = threadsAdapter
        val layoutManager = LinearLayoutManager(this)
        rvThreadList.layoutManager = layoutManager

        // Obtenemos los hilos del subforo y populamos la recycler view con ellos
        srlThreadList.isRefreshing = true
        loadThreads(subforoLink, { threads -> threadsAdapter.updateThreads(threads) })

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

                    srlThreadList.isRefreshing = true
                    loadThreads(subforoLink, {
                        threads ->

                        threadsAdapter.addThreads(threads, {
                            thread ->

                            // Evitamos los stickies ya que los tenemos añadidos ya
                            if (thread.isSticky) false
                            else true
                        })
                    })
                }
            }
        })

        // Ponemos a 1 el contador de la página y volvemos a cargar el contenido con el swipe
        srlThreadList.setOnRefreshListener {
            subforoActualPage = 1

            loadThreads(subforoLink, { threads -> threadsAdapter.updateThreads(threads) })
        }
    }
}