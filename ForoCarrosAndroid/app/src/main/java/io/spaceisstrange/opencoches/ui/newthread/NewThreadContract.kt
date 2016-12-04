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

import io.spaceisstrange.opencoches.ui.common.BasePresenter
import io.spaceisstrange.opencoches.ui.common.BaseView

interface NewThreadContract {
    interface View : BaseView<NewThreadPresenter> {
        /**
         * A llamar cuando el hilo se haya enviado correctamente a FC
         */
        fun showThreadSubmitted(threadLink: String)

        /**
         * Habilita/Deshabilita el botón de envío
         */
        fun enableSendButton(enable: Boolean)

        /**
         * A llamar cuando se produzca algún error mientras se intenta enviar el hilo a FC
         */
        fun showError()
    }

    interface Presenter : BasePresenter {
        /**
         * Crea un nuevo hilo y lo intenta enviar a ForoCoches
         */
        fun makeThread(title: String, content: String)
    }
}