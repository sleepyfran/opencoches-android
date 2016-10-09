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

import io.spaceisstrange.opencoches.ui.common.BasePresenter
import io.spaceisstrange.opencoches.ui.common.BaseView

interface ReplyThreadContract {
    interface View : BaseView<ReplyThreadPresenter> {
        /**
         * Método a llamar cuando se quiera obtener lo introducido en el campo de texto
         */
        fun getReplyMessage(): String

        /**
         * Método a llamar cuando el campo del mensaje esté vacío
         */
        fun showEmptyReply()

        /**
         * Método a llamar cuando el mensaje no se haya podido enviar debido a un error
         */
        fun showCouldNotSendReply()

        /**
         * Método a llamar cuando haya un error general
         */
        fun showError(show: Boolean)
    }

    interface Presenter : BasePresenter {
        /**
         * Método a llamar por la activity cuando se pulse en el fab
         */
        fun sendReply()

        /**
         * Método a llamar cuando el usuario pulse el botón de enviar respuesta
         */
        fun sendReply(reply: String)
    }
}