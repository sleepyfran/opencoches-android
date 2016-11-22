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

package io.spaceisstrange.opencoches.ui.thread

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tinsuke.icekick.freezeInstanceState
import com.tinsuke.icekick.state
import com.tinsuke.icekick.unfreezeInstanceState
import io.spaceisstrange.opencoches.App
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.bus.Bus
import io.spaceisstrange.opencoches.data.bus.events.ThreadPageSearchEvent
import io.spaceisstrange.opencoches.data.model.Post
import io.spaceisstrange.opencoches.ui.profile.ProfileActivity
import io.spaceisstrange.opencoches.util.ColorUtils
import kotlinx.android.synthetic.main.activity_thread.*
import kotlinx.android.synthetic.main.fragment_thread.*
import javax.inject.Inject

class ThreadFragment : Fragment(), ThreadContract.View {
    /**
     * Presenter asociado a nuestra view
     */
    @Inject lateinit var threadPresenter: ThreadPresenter

    /**
     * Bus de la aplicación
     */
    @Inject lateinit var bus: Bus

    /**
     * Información general del post a pasar al presenter
     */
    var currentPage: Int by state(1)
    var link: String? by state()

    companion object {
        /**
         * Crea una nueva instancia del fragment
         */
        fun newInstance(currentPage: Int, link: String): ThreadFragment {
            val fragment = ThreadFragment()
            fragment.currentPage = currentPage
            fragment.link = link
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_thread, container, false)

        // Intentamos restaurar los valores anteriores, si los hay
        unfreezeInstanceState(savedInstanceState)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inyectamos el fragment
        DaggerThreadComponent.builder()
                .threadModule(ThreadModule(this, link!!, currentPage))
                .busComponent((activity.application as App).busComponent)
                .build()
                .inject(this)

        // Configuramos el swipe to refresh
        srlThread.setColorSchemeColors(*ColorUtils.getSwipeRefreshLayoutColors())

        // Inicializamos el presenter
        threadPresenter.init()

        // Recargamos la página actual cuando el usuario haga swipe
        srlThread.setOnRefreshListener {
            threadPresenter.loadPage()
        }

        // Ocultamos el fab de respuesta de respuesta al hacer scroll
        wvPostContent.onScroll = {
            x, y, oldX, oldY ->

            if (oldY - y <= 0) {
                // Ocultamos el fab
                activity.fab.hide()
            } else {
                // Mostramos el fab
                activity.fab.show()
            }
        }

        // Mostramos el perfil del usuario cuando se pulse sobre su imagen en la WebView
        wvPostContent.onUserClick = {
            posterId ->

            startActivity(ProfileActivity.getStartIntent(context, posterId))
        }

        // Mostramos la barra de búsqueda cuando el usuario clique en el botón de búsqueda
        bus.observable().subscribe(
                {
                    event ->

                    if (event is ThreadPageSearchEvent && event.link == link) {
                        wvPostContent.showFindDialog("", false)
                    }
                },
                {
                    error ->

                    // Nada por lo pronto
                }
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        freezeInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        threadPresenter.finish()
    }

    override fun setPresenter(presenter: ThreadPresenter) {
        threadPresenter = presenter
    }

    override fun showPage(posts: List<Post>, onLoad: (() -> Unit)?) {
        // Actualizamos el contenido del web view
        wvPostContent.loadContent(posts, onLoad)
    }

    override fun scrollToBottom() {
        wvPostContent?.pageDown(true)
    }

    override fun showLoading(show: Boolean) {
        if (show) {
            wvPostContent?.visibility = View.GONE
            activity?.fab?.hide()
            srlThread?.isRefreshing = true
        } else {
            wvPostContent?.visibility = View.VISIBLE
            activity?.fab?.show()
            srlThread?.isRefreshing = false
        }
    }

    override fun showNotAvailable(show: Boolean) {
        if (show) {
            wvPostContent?.visibility = View.GONE
            activity?.fab?.hide()
            vNotAvailable?.visibility = View.VISIBLE
        } else {
            wvPostContent?.visibility = View.VISIBLE
            activity?.fab?.show()
            vNotAvailable?.visibility = View.GONE
        }
    }

    override fun showError(show: Boolean) {
        if (show) {
            wvPostContent?.visibility = View.GONE
            activity?.fab?.hide()
            vError?.visibility = View.VISIBLE
        } else {
            wvPostContent?.visibility = View.VISIBLE
            activity?.fab?.show()
            vError?.visibility = View.GONE
        }
    }
}