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

package io.spaceisstrange.opencoches.ui.newthread

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.ui.thread.ThreadActivity
import io.spaceisstrange.opencoches.util.SnackbarUtils
import kotlinx.android.synthetic.main.fragment_new_thread.*

class NewThreadFragment : Fragment(), NewThreadContract.View {
    /**
     * Presenter asociado con nuestro fragment
     */
    lateinit var newThreadPresenter: NewThreadPresenter

    companion object {
        /**
         * Crea una nueva instancia del fragment
         */
        fun newInstance(): NewThreadFragment {
            val fragment = NewThreadFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_thread, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Enviar el hilo cuando el usuario pulse el correspondiente botón
        btnNewThreadSend.setOnClickListener {
            val title = etNewThreadTitle.text.toString()
            val body = evEditor.text()

            // El título y el cuerpo del hilo no puede estar vacíos
            if (TextUtils.isEmpty(title)) {
                etNewThreadTitle.error = getString(R.string.new_thread_title_error)
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(body)) {
                Toast.makeText(context, getString(R.string.new_thread_body_error), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Si to' está correcto, lo enviamos
            newThreadPresenter.makeThread(title, body)

            // Deshabilitamos el botón de envío hasta nueva orden
            enableSendButton(false)
        }

        // Inicializamos el presenter
        newThreadPresenter.init()
    }

    override fun setPresenter(presenter: NewThreadPresenter) {
        newThreadPresenter = presenter
    }

    override fun showThreadSubmitted(threadLink: String) {
        startActivity(ThreadActivity.getStartIntent(context, threadLink))
        activity.finish()
    }

    override fun enableSendButton(enable: Boolean) {
        btnNewThreadSend.isEnabled = enable
    }

    override fun showError() {
        // Mostramos una snackbar con el error
        val fragmentView = view ?: return
        SnackbarUtils.makeSnackbar(fragmentView, getString(R.string.new_thread_error), Snackbar.LENGTH_LONG).show()

        // Volvemos a habilitar el botón de envío
        enableSendButton(true)
    }
}