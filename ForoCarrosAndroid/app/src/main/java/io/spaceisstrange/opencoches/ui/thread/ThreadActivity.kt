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

package io.spaceisstrange.opencoches.ui.thread

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.model.Post
import io.spaceisstrange.opencoches.ui.common.baseactivity.BaseActivity
import io.spaceisstrange.opencoches.ui.replythread.ReplyThreadActivity
import kotlinx.android.synthetic.main.activity_thread.*

class ThreadActivity : BaseActivity() {
    companion object {
        /**
         * Clave asociada al título
         */
        val THREAD_TITLE = "threadTitle"

        /**
         * Clave asociada al link
         */
        val THREAD_LINK = "threadLink"

        /**
         * Clave asociada al número de páginas
         */
        val THREAD_PAGES = "threadPages"

        /**
         * Clave asociada a la página actual
         */
        val THREAD_CURRENT_PAGE = "threadCurrentPage"

        /**
         * Retorna un Intent con los parámetros necesarios para inicializar la activity
         */
        fun getStartIntent(context: Context,
                           title: String,
                           link: String,
                           pages: Int,
                           currentPage: Int = 1): Intent {
            val startIntent = Intent(context, ThreadActivity::class.java)
            startIntent.putExtra(THREAD_TITLE, title)
            startIntent.putExtra(THREAD_LINK, link)
            startIntent.putExtra(THREAD_PAGES, pages)
            startIntent.putExtra(THREAD_CURRENT_PAGE, currentPage)
            return startIntent
        }
    }

    /**
     * Adapter del ViewPager
     */
    lateinit var pagerAdapter: ThreadPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread)
        setSupportActionBar(toolbar)
        showCloseButtonOnToolbar()

        // Obtenemos el título, link y páginas de los extras del intent
        val threadTitle = intent.extras?.getString(THREAD_TITLE)
                ?: throw IllegalArgumentException("Necesitamos el título del hilo, Houston")
        val threadLink = intent.extras?.getString(THREAD_LINK)
                ?: throw IllegalArgumentException("No soy mago, no puedo abrir un hilo sin el link")
        val threadPages = intent.extras?.getInt(THREAD_PAGES)
                ?: throw IllegalArgumentException("No soy mago, no puedo adivinar el número de páginas del hilo")
        var threadCurrentPage = intent.extras?.getInt(THREAD_CURRENT_PAGE)!!

        // Ponemos el título del hilo en la toolbar
        supportActionBar?.title = threadTitle

        // Inicializamos el view pager
        pagerAdapter = ThreadPagerAdapter(supportFragmentManager, threadLink, threadPages)
        vpThreadPages.adapter = pagerAdapter
        vpThreadPages.currentItem = threadCurrentPage - 1

        // Iniciamos la activity de respuesta al hilo cuando el usuario pulse el FAB
        fab.setOnClickListener {
            startActivity(ReplyThreadActivity.getStartIntent(this, threadTitle, threadLink))
        }

        // Actualizamos las páginas cuando nos movamos por el ViewPager
        tvThreadPages.text = getString(R.string.thread_pages_count, threadCurrentPage, pagerAdapter.count)
        vpThreadPages.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                // Actualizamos el conteo de páginas
                threadCurrentPage = position + 1
                tvThreadPages.text = getString(R.string.thread_pages_count, position + 1, pagerAdapter.count)
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Nada
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Nada
            }
        })

        // Establecemos las opciones de los botones de navegación
        btnThreadFirstPage.setOnClickListener {
            threadCurrentPage = 1
            vpThreadPages.currentItem = threadCurrentPage - 1
        }

        btnThreadPreviousPage.setOnClickListener {
            if (threadCurrentPage > 1) {
                threadCurrentPage -= 1
                vpThreadPages.currentItem = threadCurrentPage - 1
            }
        }

        btnThreadNextPage.setOnClickListener {
            if (threadCurrentPage < pagerAdapter.count) {
                threadCurrentPage += 1
                vpThreadPages.currentItem = threadCurrentPage - 1
            }
        }

        btnThreadLastPage.setOnClickListener {
            vpThreadPages.currentItem = pagerAdapter.count - 1
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // TODO: Poner el menú específico de los threads
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // TODO: Hacerse cargo el menú
        return super.onOptionsItemSelected(item)
    }
}