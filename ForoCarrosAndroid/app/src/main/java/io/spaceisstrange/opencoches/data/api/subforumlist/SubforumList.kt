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

package io.spaceisstrange.opencoches.data.api.subforumlist

import io.spaceisstrange.opencoches.data.api.ApiConstants
import io.spaceisstrange.opencoches.data.api.BaseGetRequest
import io.spaceisstrange.opencoches.data.api.transformations.HtmlToSubforumList
import io.spaceisstrange.opencoches.data.model.Subforum
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class SubforumList : BaseGetRequest() {
    /**
     * Retorna un observable para obtener los subforos
     */
    fun observable(): Observable<List<Subforum>> {
        return Observable.fromCallable({
            getSubforums()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Obtiene la página de inicio de FC y la parsea para obtener los subforos actuales. Podría
     * perfectamente poner todos a las bravas, pero es un coñazo y además necesitamos obtener el
     * último post que se ha hecho en dicho subforo y la hora
     */
    private fun getSubforums(): List<Subforum> {
        val response = super.doRequest()

        return HtmlToSubforumList.transform(response.parse())
    }

    override fun getUrl(): String {
        return ApiConstants.BASE_URL
    }
}