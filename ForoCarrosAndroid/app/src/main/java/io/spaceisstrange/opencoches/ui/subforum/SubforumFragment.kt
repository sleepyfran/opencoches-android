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

package io.spaceisstrange.opencoches.ui.subforum

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.model.Thread
import io.spaceisstrange.opencoches.ui.thread.ThreadActivity
import kotlinx.android.synthetic.main.fragment_subforum.*

class SubforumFragment : Fragment(), SubforumContract.View {
    /**
     * Presenter asociado a nuestro fragment
     */
    lateinit var subforumPresenter: SubforumPresenter

    /**
     * Adapter de los hilos del subforo
     */
    val adapter = SubforumAdapter({
        thread ->

        // Notificamos al presenter sobre el click
        this.subforumPresenter.openThread(thread)
    })

    /**
     * Variables para hacer el RecyclerView infinito
     */
    var previousTotalItemCount = 0
    var visibleItemThreshold = 15
    var firstVisibleItemPosition = 0
    var visibleItemCount = 0
    var totalItemCount = 0
    var loadingContent = true

    companion object {
        /**
         * Crea una nueva instancia del fragment
         */
        fun newInstance(): SubforumFragment {
            val fragment = SubforumFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_subforum, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuramos el RecyclerView
        rvSubforumThreads.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        rvSubforumThreads.layoutManager = layoutManager

        // Configuramos la RecyclerView para ser "infinita"
        // Basado en: http://stackoverflow.com/a/26561717
        rvSubforumThreads.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                    subforumPresenter.loadNextPage()
                }
            }
        })

        // Recargamos el contenido cuando el usuario haga un swipe to refresh
        srlSubforum.setOnRefreshListener {
            subforumPresenter.loadThreads()
        }

        // Iniciamos el presenter
        subforumPresenter.init()
    }

    override fun setPresenter(presenter: SubforumPresenter) {
        subforumPresenter = presenter
    }

    override fun showLoading(enabled: Boolean) {
        srlSubforum.isRefreshing = enabled
    }

    override fun showThreads(threads: List<Thread>) {
        adapter.update(threads)
    }

    override fun addThreads(threads: List<Thread>) {
        adapter.addThreads(threads)
    }

    override fun showThread(thread: Thread) {
        startActivity(ThreadActivity.getStartIntent(context, thread.title, thread.link, thread.pages))
    }

    override fun showError(show: Boolean) {
        if (show) {
            rvSubforumThreads.visibility = View.GONE
            vError.visibility = View.VISIBLE
        } else {
            rvSubforumThreads.visibility = View.VISIBLE
            vError.visibility = View.GONE
        }
    }
}