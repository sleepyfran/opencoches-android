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

package io.spaceisstrange.opencoches.ui.replythread

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.util.SnackbarUtils
import kotlinx.android.synthetic.main.fragment_reply_thread.*

class ReplyThreadFragment : Fragment(), ReplyThreadContract.View {
    /**
     * Presenter asociado a nuestro fragment
     */
    lateinit var replyThreadPresenter: ReplyThreadPresenter

    companion object {
        /**
         * Crea una nueva instancia del fragment
         */
        fun newInstance(): ReplyThreadFragment {
            val fragment = ReplyThreadFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reply_thread, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Iniciamos el presenter
        replyThreadPresenter.init()
    }

    override fun setPresenter(presenter: ReplyThreadPresenter) {
        replyThreadPresenter = presenter
    }

    override fun getReplyMessage(): String {
        return evEditor.text()
    }

    override fun showEmptyReply() {
        evEditor.showError(getString(R.string.thread_reply_empty_body))
    }

    override fun showCouldNotSendReply() {
        SnackbarUtils.makeSnackbar(view!!, getString(R.string.thread_reply_error), Snackbar.LENGTH_SHORT).show()
    }

    override fun showError(show: Boolean) {
        if (show) {
            evEditor.visibility = View.GONE
            vError.visibility = View.VISIBLE
        } else {
            evEditor.visibility = View.VISIBLE
            vError.visibility = View.GONE
        }
    }
}