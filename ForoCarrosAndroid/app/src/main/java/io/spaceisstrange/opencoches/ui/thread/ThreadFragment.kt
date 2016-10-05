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
import io.spaceisstrange.opencoches.App
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.model.Post
import io.spaceisstrange.opencoches.util.PreCacheLayoutManager
import kotlinx.android.synthetic.main.fragment_thread.*
import javax.inject.Inject

class ThreadFragment : Fragment(), ThreadContract.View {
    /**
     * Presenter asociado a nuestra view
     */
    @Inject lateinit var threadPresenter: ThreadPresenter

    /**
     * Adapter de los posts
     */
    lateinit var threadAdapter: ThreadAdapter

    /**
     * Información general del post a pasar al presenter
     */
    var currentPage: Int = 1
    lateinit var link: String

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
        return inflater.inflate(R.layout.fragment_thread, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inyectamos el fragment
        DaggerThreadComponent.builder()
                .threadModule(ThreadModule(this, link, currentPage))
                .sharedPreferencesUtilsComponent((activity.application as App).sharedPrefsComponent)
                .build()
                .inject(this)

        // Configuramos la RecyclerView
        threadAdapter = ThreadAdapter()
        val layoutManager = PreCacheLayoutManager(context)
        rvThreadPostList.adapter = threadAdapter
        rvThreadPostList.layoutManager = layoutManager

        // Establecemos el onClick
        threadAdapter.onUserClick = {
            // Nada por ahora
        }

        // Inicializamos el presenter
        threadPresenter.init()
    }

    override fun setPresenter(presenter: ThreadPresenter) {
        threadPresenter = presenter
    }

    override fun showPage(posts: List<Post>) {
        threadAdapter.updatePosts(posts)
    }

    override fun showError(show: Boolean) {
        if (show) {
            rvThreadPostList.visibility = View.GONE
            llReply.visibility = View.GONE
            vError.visibility = View.VISIBLE
        } else {
            rvThreadPostList.visibility = View.VISIBLE
            llReply.visibility = View.VISIBLE
            vError.visibility = View.GONE
        }
    }
}