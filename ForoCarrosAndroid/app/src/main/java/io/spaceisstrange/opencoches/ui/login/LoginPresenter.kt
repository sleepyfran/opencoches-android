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

import android.text.TextUtils
import io.spaceisstrange.opencoches.data.AccountManager
import io.spaceisstrange.opencoches.data.api.login.Login
import io.spaceisstrange.opencoches.data.api.userdata.UserId
import io.spaceisstrange.opencoches.data.sharedpreferences.SharedPreferencesUtils
import rx.subscriptions.CompositeSubscription
import java.net.SocketTimeoutException
import javax.inject.Inject

class LoginPresenter @Inject constructor(val view: LoginContract.View,
                                         val sharedPreferences: SharedPreferencesUtils) : LoginContract.Presenter {
    /**
     * CompositeSubscription donde agregar todos los observables que vayamos utilizando
     */
    lateinit var compositeSubscription: CompositeSubscription

    @Inject
    override fun setup() {
        // Nos declaramos presenters de la view
        view.setPresenter(this)
    }

    override fun init() {
        // Inicializamos la CompositeSubscription
        compositeSubscription = CompositeSubscription()
    }

    override fun login(username: String, password: String) {
        if (TextUtils.isEmpty(username)) {
            view.showUsernameError()
            return
        }

        if (TextUtils.isEmpty(password)) {
            view.showPasswordError()
            return
        }

        view.showLoading(true)

        // Intentamos loguear al usuario
        AccountManager.login(sharedPreferences, username, password,
                {
                    result, error ->

                    if (result) {
                        view.showSubforumList()
                    } else {
                        // Se ha producido un error
                        view.showLoading(false)
                        view.showWrongDataError()
                    }
                })
    }

    override fun finish() {
        compositeSubscription.unsubscribe()
    }
}