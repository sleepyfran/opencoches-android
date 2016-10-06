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

package io.spaceisstrange.opencoches.ui.subforumlist

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.model.Subforum
import io.spaceisstrange.opencoches.ui.subforum.SubforumActivity
import kotlinx.android.synthetic.main.fragment_subforum_list.*

class SubforumListFragment : Fragment(), SubforumListContract.View {
    /**
     * Presenter asociado a la nuestro fragment
     */
    lateinit var subsforumListPresenter: SubforumListPresenter

    /**
     * Adapter de los subforos
     */
    val adapter = SubforumListAdapter({
        subforum ->

        // Notificamos al presenter sobre el click
        this.subsforumListPresenter.openSubforum(subforum)
    })

    companion object {
        /**
         * Crea una nueva instancia del fragment
         */
        fun newInstance(): SubforumListFragment {
            val fragment = SubforumListFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_subforum_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuramos el RecyclerView
        rvSubforumList.adapter = adapter
        rvSubforumList.layoutManager = LinearLayoutManager(context)

        // Iniciamos el presenter
        subsforumListPresenter.init()
    }

    override fun onDestroy() {
        super.onDestroy()
        subsforumListPresenter.finish()
    }

    override fun setPresenter(presenter: SubforumListPresenter) {
        subsforumListPresenter = presenter
    }

    override fun showLoading(enabled: Boolean) {
        if (enabled) {
            rvSubforumList?.visibility = View.GONE
            loading?.visibility = View.VISIBLE
        } else {
            rvSubforumList?.visibility = View.VISIBLE
            loading?.visibility = View.GONE
        }
    }

    override fun showSubforums(subforums: List<Subforum>) {
        adapter.update(subforums)
    }

    override fun showSubforum(subforum: Subforum) {
        // Mostramos el subforo
        startActivity(SubforumActivity.getStartIntent(context, subforum.link))
    }

    override fun showError(show: Boolean) {
        if (show) {
            loading?.visibility = View.GONE
            rvSubforumList?.visibility = View.GONE
            vError?.visibility = View.VISIBLE
        } else {
            loading?.visibility = View.VISIBLE
            rvSubforumList?.visibility = View.VISIBLE
            vError?.visibility = View.GONE
        }
    }
}