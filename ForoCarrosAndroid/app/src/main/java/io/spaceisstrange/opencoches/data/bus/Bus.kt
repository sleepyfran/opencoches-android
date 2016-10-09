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

package io.spaceisstrange.opencoches.data.bus

import io.spaceisstrange.opencoches.data.bus.events.Event
import rx.Observable
import rx.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Bus para comunicarnos entre fragments, activities y services
 */
@Singleton
class Bus @Inject constructor() {
    /**
     * Publish Subject nos permite recibir y pasar objetos a la vez, así que lo usaremos como
     * nuestro bus personal
     */
    private val bus: PublishSubject<Event> = PublishSubject.create()

    /**
     * Método a llamar cuando queramos pasar algo a través del bus. Lo que se pase por el bus
     * tiene que ser una implementación de la interfaz Event
     */
    fun publish(event: Event) {
        bus.onNext(event)
    }

    /**
     * Método a llamar para obtener un observable y subscribirnos así a los eventos que pasen
     * a través del bus
     */
    fun observable(): Observable<Event> {
        return bus
    }
}