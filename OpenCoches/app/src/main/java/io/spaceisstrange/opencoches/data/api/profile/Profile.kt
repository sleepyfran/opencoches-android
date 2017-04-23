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

package io.spaceisstrange.opencoches.data.api.profile

import io.spaceisstrange.opencoches.data.api.ApiConstants
import io.spaceisstrange.opencoches.data.api.BaseGetRequest
import io.spaceisstrange.opencoches.data.api.transformations.HtmlToProfile
import io.spaceisstrange.opencoches.data.model.Profile
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Clase para obtener el perfil de un usuario dado su ID.
 */
class Profile(val userId: String) : BaseGetRequest() {
    /**
     * Retorna un observable para obtener la información de un usuario.
     */
    fun observable(): Observable<Profile> {
        return Observable.fromCallable({
            getProfile()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Obtiene los datos de un usuario como su información, sus posts, su fecha de registro, etc.
     */
    private fun getProfile(): Profile {
        val response = super.doRequest()

        return HtmlToProfile.transform(response.parse())
    }

    override fun getUrl(): String {
        return ApiConstants.BASE_URL + ApiConstants.USER_PROFILE_URL + userId
    }
}