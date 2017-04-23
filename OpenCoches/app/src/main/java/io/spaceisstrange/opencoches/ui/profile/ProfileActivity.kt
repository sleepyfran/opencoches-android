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

package io.spaceisstrange.opencoches.ui.profile

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.klinker.android.sliding.SlidingActivity
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.api.profile.Profile
import kotlinx.android.synthetic.main.activity_profile.*

/**
 * Activity que muestra el perfil de un usuario.
 */
class ProfileActivity : SlidingActivity() {
    companion object {
        /**
         * Clave asociada al link de donde se cargará el usuario.
         */
        val USER_ID = "userLink"

        /**
         * Retorna un Intent con los parámetros necesarios para inicializar la activity.
         */
        fun getStartIntent(context: Context, userId: String): Intent {
            val startIntent = Intent(context, ProfileActivity::class.java)
            startIntent.putExtra(USER_ID, userId)
            return startIntent
        }
    }

    /**
     * ID del usuario del cual queremos cargar el perfil.
     */
    var userId: String = ""

    override fun init(savedInstanceState: Bundle?) {
        // Intentamos obtener los datos de los extras del intent
        userId = intent.extras?.getString(USER_ID) ?:
                throw IllegalArgumentException("Necesito un ID de usuario para funcionar.")

        // Configuramos la activity
        setContent(R.layout.activity_profile)
        title = "Cargando..."

        loadProfile()
    }

    /**
     * Carga los datos del perfil y los muestra.
     */
    fun loadProfile() {
        showLoading(true)

        // Cargamos la información del usuario
        Profile(userId).observable().subscribe(
                {
                    userData ->

                    showError(false)
                    showLoading(false)
                    showUserInfo(userData)
                },
                {
                    error ->

                    // Mostramos la carita triste :(
                    showError(true)
                }
        )
    }

    /**
     * Muestra los datos del usuario en la activity.
     */
    fun showUserInfo(profile: io.spaceisstrange.opencoches.data.model.Profile) {
        // Mostramos la última vez sólo si está disponible
        if (profile.lastActivity != "") {
            userLastActivity.visibility = View.VISIBLE
        }

        userRegistrationDate.visibility = View.VISIBLE
        userTotalPosts.visibility = View.VISIBLE

        userLastActivity.text = profile.lastActivity
        userRegistrationDate.text = profile.registrationDate
        userTotalPosts.text = profile.totalPosts

        title = profile.username

        // Cargamos la imagen
        Glide.with(this)
                .load(profile.photoSrc)
                .asBitmap()
                .into(object : SimpleTarget<Bitmap>(300, 300) {
                    override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                        // Mostramos la imagen cargada en la header de la activity
                        setImage(resource)
                    }
                })
    }

    /**
     * Muestra u oculta la animación de carga.
     */
    fun showLoading(show: Boolean) {
        if (show) {
            userInfo?.visibility = View.GONE
            loading?.visibility = View.VISIBLE
        } else {
            userInfo.visibility = View.VISIBLE
            loading?.visibility = View.GONE
        }
    }

    /**
     * Muestra u oculta la sección de error.
     */
    fun showError(show: Boolean) {
        if (show) {
            loading?.visibility = View.GONE
            userInfo?.visibility = View.GONE
            error?.visibility = View.VISIBLE
        } else {
            userInfo?.visibility = View.VISIBLE
            error?.visibility = View.GONE
        }
    }
}