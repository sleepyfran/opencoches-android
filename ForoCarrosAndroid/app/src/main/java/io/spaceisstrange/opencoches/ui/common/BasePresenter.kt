package io.spaceisstrange.opencoches.ui.common

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

/**
 * Interfaz que representa la base de los Presenters de la aplicación
 */
interface BasePresenter {
    /**
     * Método a llamar cada vez que se inicialice el Presenter
     */
    fun init()

    /**
     * Método a llamar cada vez que se vaya a cerrar la activity o fragment actual
     */
    fun finish()
}