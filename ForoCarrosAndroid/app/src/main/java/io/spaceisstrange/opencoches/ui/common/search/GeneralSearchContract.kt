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

package io.spaceisstrange.opencoches.ui.common.search

import io.spaceisstrange.opencoches.data.model.SearchQuery
import io.spaceisstrange.opencoches.data.model.Thread
import io.spaceisstrange.opencoches.ui.common.BasePresenter
import io.spaceisstrange.opencoches.ui.common.BaseView
import io.spaceisstrange.opencoches.ui.common.search.GeneralSearchPresenter

/**
 * Métodos a implementar en la View y el Presenter
 */
interface GeneralSearchContract {
    interface View : BaseView<GeneralSearchPresenter> {
        /**
         * Método a llamar cuando tengamos el número total de páginas de la búsqueda
         */
        fun setSearchPages(pages: Int)

        /**
         * Método a llamar cuando los datos de la búsqueda estén cargados
         */
        fun showSearchResults(results: List<Thread>)

        /**
         * Método a llamar cuando tenemos más datos de la búsqueda
         */
        fun showMoreSearchResults(results: List<Thread>)

        /**
         * Método a llamar cuando se nos devuelva un resultado vacío en la búsqueda
         */
        fun showNoResults(show: Boolean)

        /**
         * Método a llamar cuando se estén cargando los resultados de la búsqueda
         */
        fun showLoading(show: Boolean)

        /**
         * Método a llamar cuando se produzca un error. Esto es malo
         */
        fun showError(show: Boolean)
    }

    interface Presenter : BasePresenter {
        /**
         * Método a llamar cuando se quiera realizar una búsqueda
         */
        fun search(query: String)

        /**
         * Método a llamar cuando se quiera realizar una búsqueda personalizada
         */
        fun search(searchQuery: SearchQuery)

        /**
         * Método a llamar cuando queramos cargar el contenido de la siguiente página de la búsqueda
         */
        fun loadNextSearchPage()
    }
}