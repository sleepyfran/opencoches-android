package io.spaceisstrange.opencoches.ui.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.adapters.PostAdapter
import io.spaceisstrange.opencoches.api.net.ApiConstants
import io.spaceisstrange.opencoches.api.rx.FCThreadObservable
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
        val closeButton = ContextCompat.getDrawable(this, R.drawable.ic_close_white)
        supportActionBar?.setHomeAsUpIndicator(closeButton)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Mostramos el título del hilo
        supportActionBar?.title = threadTitle

        // Configuramos la RecyclerView
        val postAdapter = PostAdapter()
        rvPostList.adapter = postAdapter
        rvPostList.layoutManager = PreCacheLayoutManager(this)

        // Obtenemos los posts del hilo y populamos la RecyclerView con ellos
        FCThreadObservable.create(threadLink).subscribe(
                {
                    posts ->

                    postAdapter.updatePosts(posts)
                },
                {
                    error ->

                    error.printStackTrace()

                    // TODO: Hacerse cargo, una vez más, de los malditos errores
                }
        )
    }
}