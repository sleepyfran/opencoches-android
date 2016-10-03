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

import io.spaceisstrange.opencoches.ui.common.BasePresenter
import io.spaceisstrange.opencoches.ui.common.BaseView

/**
 * Métodos a implementar por la View y el Presenter
 */
interface LoginContract {
    interface View : BaseView<LoginPresenter> {
        /**
         * Método a llamar cuando se esté cargando o se deje de cargar
         */
        fun showLoading(enabled: Boolean)

        /**
         * Método a llamar cuando se haya logueado correctamente al usuario
         */
        fun showSubforumList()

        /**
         * Método a llamar cuando el campo de usuario está vacío
         */
        fun showUsernameError()

        /**
         * Método a llamar cuando el campo de contraseña esté vacío
         */
        fun showPasswordError()

        /**
         * Método a llamar cuando los datos sean incorrectos. Esto es malo
         */
        fun showWrongDataError()
    }

    interface Presenter : BasePresenter {
        /**
         * Método a llamar cuando queramos loguearnos con los datos actuales
         */
        fun login(username: String, password: String)
    }
}