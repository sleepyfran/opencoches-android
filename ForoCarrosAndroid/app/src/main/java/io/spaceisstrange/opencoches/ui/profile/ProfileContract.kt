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

import io.spaceisstrange.opencoches.data.model.UserData
import io.spaceisstrange.opencoches.ui.common.BasePresenter
import io.spaceisstrange.opencoches.ui.common.BaseView

interface ProfileContract {
    interface View : BaseView<ProfilePresenter> {
        /**
         * Método a llamar cuando se haya cargado la información del usuario
         */
        fun showUserInfo(userData: UserData)

        /**
         * Método a llamar cuando se estén cargando los datos
         */
        fun showLoading(show: Boolean)

        /**
         * Método a llamar cuando se produzca un error. Esto es malo
         */
        fun showError(show: Boolean)
    }

    interface Presenter : BasePresenter {

        /**
         * Método a llamar cuando se quiera cargar más información sobre el usuario
         */
        fun loadUserInformation()
    }
}