/*
 * Made with <3 by Fran González (@spaceisstrange)
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

package io.spaceisstrange.opencoches.data.api.smilies

import io.spaceisstrange.opencoches.data.api.ApiConstants
import io.spaceisstrange.opencoches.data.api.BaseGetRequest
import io.spaceisstrange.opencoches.data.api.transformations.HtmlToSmilies
import io.spaceisstrange.opencoches.data.model.Smily
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Clase para cargar los smilies de ForoCoches.
 */
class Smilies : BaseGetRequest() {
    /**
     * Retorna un observable para obtener los smilies del foro :roto2:.
     */
    fun observable(): Observable<List<Smily>> {
        return Observable.fromCallable({
            getSmilies()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Retorna una lista con los smilies disponibles actualmente en ForoCoches.
     */
    private fun getSmilies(): List<Smily> {
        val smiliesCache = SmiliesCache.smiliesCache

        if (smiliesCache == null) {
            // No tenemos los smilies guardados, así que los descargamos y cacheamos
            val request = super.doRequest()
            SmiliesCache.smiliesCache = HtmlToSmilies.transform(request.parse())
        }

        return SmiliesCache.smiliesCache!!
    }

    override fun getUrl(): String {
        return ApiConstants.BASE_URL + ApiConstants.SMILIES_URL
    }
}