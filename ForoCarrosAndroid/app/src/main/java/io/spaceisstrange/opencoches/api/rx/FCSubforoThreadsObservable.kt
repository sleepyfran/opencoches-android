package io.spaceisstrange.opencoches.api.rx

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

import io.spaceisstrange.opencoches.api.model.SubforoThread
import io.spaceisstrange.opencoches.api.net.FCSubforoThreads
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class FCSubforoThreadsObservable {
    companion object {
        fun create(subforoLink: String, page: Int? = null): Observable<MutableList<SubforoThread>> {
            return Observable.fromCallable(
                    {
                        FCSubforoThreads(subforoLink, page).getThreads()
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }
}