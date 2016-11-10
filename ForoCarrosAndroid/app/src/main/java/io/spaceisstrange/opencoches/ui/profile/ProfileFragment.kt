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

package io.spaceisstrange.opencoches.ui.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.model.UserData
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), ProfileContract.View {
    /**
     * Presenter asociado a esta view
     */
    lateinit var profilePresenter: ProfilePresenter

    companion object {
        /**
         * Crea una nueva instancia del fragment
         */
        fun newInstance(): ProfileFragment {
            val fragment = ProfileFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cargamos los datos del usuario
        profilePresenter.init()
    }

    override fun setPresenter(presenter: ProfilePresenter) {
        profilePresenter = presenter
    }

    override fun showUserInfo(userData: UserData) {
        // Mostramos la última vez sólo si está disponible
        if (userData.lastActivity != "") {
            tvUserLastActivity.visibility = View.VISIBLE
        }

        tvUserRegistrationDate.visibility = View.VISIBLE
        tvUserTotalPosts.visibility = View.VISIBLE

        tvUserLastActivity.text = userData.lastActivity
        tvUserRegistrationDate.text = userData.registrationDate
        tvUserTotalPosts.text = userData.totalPosts

        // Lo pasamos a la activity para mostrar los datos restantes
        (activity as ProfileActivity).showUserData(userData)
    }

    override fun showLoading(show: Boolean) {
        if (show) {
            pbLoading?.visibility = View.VISIBLE
        } else {
            pbLoading?.visibility = View.GONE
        }
    }

    override fun showError(show: Boolean) {
        if (show) {
            pbLoading?.visibility = View.GONE
            llUserInfo?.visibility = View.GONE
            vError?.visibility = View.VISIBLE
        } else {
            llUserInfo?.visibility = View.VISIBLE
            llUserInfo?.visibility = View.VISIBLE
            vError?.visibility = View.GONE
        }
    }
}