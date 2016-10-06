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

package io.spaceisstrange.opencoches.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.ui.subforumlist.SubforumListActivity
import io.spaceisstrange.opencoches.util.SnackbarUtils
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(), LoginContract.View {
    /**
     * Presenter asociado al login
     */
    lateinit var loginPresenter: LoginPresenter

    companion object {
        /**
         * Crea una nueva instancia del fragment
         */
        fun newInstance(): LoginFragment {
            val fragment = LoginFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogin.setOnClickListener {
            val username = etLoginUsername.text.toString()
            val password = etLoginPassword.text.toString()

            // Nos logueamos
            loginPresenter.login(username, password)
        }

        // Iniciamos el presenter
        loginPresenter.init()
    }

    override fun onDestroy() {
        super.onDestroy()
        loginPresenter.finish()
    }

    override fun setPresenter(presenter: LoginPresenter) {
        loginPresenter = presenter
    }

    override fun showLoading(enabled: Boolean) {
        if (enabled) {
            llLogin?.visibility = View.GONE
            loading?.visibility = View.VISIBLE
        } else {
            llLogin?.visibility = View.VISIBLE
            loading?.visibility = View.GONE
        }
    }

    override fun showSubforumList() {
        val subforumIntent = Intent(activity, SubforumListActivity::class.java)
        startActivity(subforumIntent)
    }

    override fun showUsernameError() {
        etLoginUsername?.error = getString(R.string.login_error_username)
    }

    override fun showPasswordError() {
        etLoginPassword?.error = getString(R.string.login_error_password)
    }

    override fun showWrongDataError() {
        SnackbarUtils.makeSnackbar(view!!,
                getString(R.string.error_login_message),
                Snackbar.LENGTH_LONG).show()
    }
}