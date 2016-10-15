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

package io.spaceisstrange.opencoches.ui.views.editor.smilies

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.model.Smily
import kotlinx.android.synthetic.main.fragment_smilies.*
import kotlinx.android.synthetic.main.fragment_smilies.view.*

class SmiliesFragment : Fragment(), SmiliesContract.View {
    /**
     * Presenter asociado a este diálogo
     */
    lateinit var smiliesPresenter: SmiliesPresenter

    /**
     * View del fragment
     */
    lateinit var fragmentView: View

    companion object {
        /**
         * Crea una nueva instancia con los parámetros necesarios para el dialog
         */
        fun newInstance(): SmiliesFragment {
            val fragment = SmiliesFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_smilies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentView = view

        // Configuramos la WebView
        wvSmiliesContent.onSmilyClick = {
            smilyCode ->

            smiliesPresenter.selectSmily(smilyCode)
        }

        // Inicializamos el presenter
        smiliesPresenter.init()
    }

    override fun setPresenter(presenter: SmiliesPresenter) {
        smiliesPresenter = presenter
    }

    override fun showSmilies(smilies: List<Smily>) {
        wvSmiliesContent.loadContent(smilies)
    }

    override fun showLoading(show: Boolean) {
        if (show) {
            fragmentView.wvSmiliesContent?.visibility = View.GONE
            fragmentView.loading?.visibility = View.VISIBLE
        } else {
            fragmentView.wvSmiliesContent?.visibility = View.VISIBLE
            fragmentView.loading?.visibility = View.GONE
        }
    }

    override fun showError(show: Boolean) {
        if (show) {
            fragmentView.wvSmiliesContent?.visibility = View.GONE
            fragmentView.vError?.visibility = View.VISIBLE
        } else {
            fragmentView.wvSmiliesContent?.visibility = View.VISIBLE
            fragmentView.vError?.visibility = View.GONE
        }
    }
}