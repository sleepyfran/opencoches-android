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

package io.spaceisstrange.opencoches.ui.subforumlist

import io.spaceisstrange.opencoches.data.model.Subforum
import io.spaceisstrange.opencoches.ui.common.BasePresenter
import io.spaceisstrange.opencoches.ui.common.BaseView

/**
 * Métodos a implementar en la View y el Presenter
 */
interface SubforumListContract {
    interface View : BaseView<SubforumListPresenter> {
        /**
         * Método a llamar cuando se carga/deja de cargar la lista de subforos
         */
        fun showLoading(enabled: Boolean)

        /**
         * Método a llamar cuando se terminan de cargar los datos
         */
        fun showSubforums(subforums: List<Subforum>)

        /**
         * Método a llamar cuando el usuario selecciona un subforo
         */
        fun showSubforum(subforum: Subforum)

        /**
         * Método a llamar para cuando se produce algún error crítico
         */
        fun showError()
    }

    interface Presenter : BasePresenter {
        /**
         * Método a llamar cuando queramos cargar los subforos
         */
        fun loadSubforums()

        /**
         * Método a llamar cuando el usuario selecciona un subforo
         */
        fun openSubforum(subforum: Subforum)
    }
}