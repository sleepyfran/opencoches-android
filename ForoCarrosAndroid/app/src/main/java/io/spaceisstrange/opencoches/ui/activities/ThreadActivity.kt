package io.spaceisstrange.opencoches.ui.activities

import android.animation.Animator
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AccelerateInterpolator
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.adapters.PostAdapter
import io.spaceisstrange.opencoches.api.model.Post
import io.spaceisstrange.opencoches.api.net.ApiConstants
import io.spaceisstrange.opencoches.api.rx.FCThreadObservable
import io.spaceisstrange.opencoches.api.rx.FCThreadPagesObservable
import io.spaceisstrange.opencoches.ui.dialogs.UserProfileDialog
import io.spaceisstrange.opencoches.utils.PreCacheLayoutManager
import kotlinx.android.synthetic.main.activity_thread.*

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

class ThreadActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread)
        setSupportActionBar(toolbar)

        // Obtenemos el título y link del hilo de los extras
        val threadTitle = intent?.extras?.getString(ApiConstants.THREAD_TITLE_KEY)!!
        val threadLink = intent?.extras?.getString(ApiConstants.THREAD_LINK_KEY)!!

        // Mostramos la X en la toolbar
        showCloseButtonOnToolbar()

        // Mostramos el título del hilo
        showCustomTitleOnToolbar(threadTitle)

        // Cargamos el número total de páginas del hilo
        FCThreadPagesObservable.create(threadLink).subscribe(
                {
                    pages ->


                },
                {
                    error ->

                    // Silenciamos los errores :)
                }
        )
    }
}