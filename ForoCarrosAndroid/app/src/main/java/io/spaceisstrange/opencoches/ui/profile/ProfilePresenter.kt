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

import com.google.firebase.crash.FirebaseCrash
import io.spaceisstrange.opencoches.data.api.ApiUtils
import io.spaceisstrange.opencoches.data.api.profile.Profile
import io.spaceisstrange.opencoches.util.RegexUtils
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class ProfilePresenter @Inject constructor(val view: ProfileContract.View, var userId: String) : ProfileContract.Presenter {
    /**
     * CompositeSubscription donde agregar todos los observables que vayamos utilizando
     */
    lateinit var compositeSubscription: CompositeSubscription

    @Inject
    override fun setup() {
        view.setPresenter(this)
    }

    override fun init() {
        // Inicializamos la CompositeSubscription
        compositeSubscription = CompositeSubscription()

        // Vemos si nos han pasado el ID del usuario o una URL
        if (userId.startsWith("http://")) {
            userId = ApiUtils.getIdFromUrl(userId)
                    ?: throw IllegalArgumentException("La URL que me has dado no es válida, loco")
        }

        // Cargamos la información sobre el usuario
        loadUserInformation()
    }

    override fun loadUserInformation() {
        view.showLoading(true)

        // Cargamos la información del usuario
        Profile(userId).observable().subscribe(
                {
                    userData ->

                    view.showError(false)
                    view.showLoading(false)
                    view.showUserInfo(userData)
                },
                {
                    error ->

                    // Reportamos el error
                    FirebaseCrash.report(error)

                    // Mostramos la carita triste :(
                    view.showError(true)
                }
        )
    }

    override fun finish() {
        compositeSubscription.unsubscribe()
    }
}