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

package io.spaceisstrange.opencoches.ui.subforum

import io.spaceisstrange.opencoches.data.model.Thread
import io.spaceisstrange.opencoches.ui.common.BasePresenter
import io.spaceisstrange.opencoches.ui.common.BaseView

/**
 * Métodos a implementar en la View y el Presenter
 */
interface SubforumContract {
    interface View : BaseView<SubforumPresenter> {
        /**
         * Método a llamar cuando se carga/deja de cargar la lista de hilos
         */
        fun showLoading(enabled: Boolean)

        /**
         * Método a llamar cuando se hayan cargado los hilos del subforo
         */
        fun showThreads(threads: List<Thread>)

        /**
         * Método a llamar cuando se hayan cargado más hilos del subforo
         */
        fun addThreads(threads: List<Thread>)

        /**
         * Método a llamar cuando el usuario seleccione un hilo
         */
        fun showThread(thread: Thread)

        /**
         * Método a llamar para cuando se produce algún error crítico
         */
        fun showError(show: Boolean)
    }

    interface Presenter : BasePresenter {
        /**
         * Método a llamar cuando queramos cargar los hilos del subforo
         */
        fun loadThreads()

        /**
         * Método a llamar cuando queramos cargar la siguiente página del subforo
         */
        fun loadNextPage()

        /**
         * Método a llamar cuando el usuario selecciona un hilo
         */
        fun openThread(thread: Thread)
    }
}