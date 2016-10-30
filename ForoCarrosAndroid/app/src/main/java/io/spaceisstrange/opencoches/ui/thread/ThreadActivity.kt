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

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import io.spaceisstrange.opencoches.App
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.AccountManager
import io.spaceisstrange.opencoches.data.api.ApiConstants
import io.spaceisstrange.opencoches.data.api.ApiUtils
import io.spaceisstrange.opencoches.data.bus.Bus
import io.spaceisstrange.opencoches.data.bus.events.PageScrolledEvent
import io.spaceisstrange.opencoches.data.bus.events.RepliedToThreadEvent
import io.spaceisstrange.opencoches.data.bus.events.ThreadPageSearchEvent
import io.spaceisstrange.opencoches.ui.common.baseactivity.BaseActivity
import io.spaceisstrange.opencoches.ui.replythread.ReplyThreadActivity
import io.spaceisstrange.opencoches.util.ActivityUtils
import io.spaceisstrange.opencoches.util.IntentUtils
import kotlinx.android.synthetic.main.activity_thread.*

class ThreadActivity : BaseActivity() {
    companion object {
        /**
         * Clave asociada al link
         */
        val THREAD_LINK = "threadLink"

        /**
         * Retorna un Intent con los parámetros necesarios para inicializar la activity
         */
        fun getStartIntent(context: Context,
                           link: String): Intent {
            val startIntent = Intent(context, ThreadActivity::class.java)
            startIntent.putExtra(THREAD_LINK, link)
            return startIntent
        }
    }

    /**
     * Adapter del ViewPager
     */
    lateinit var pagerAdapter: ThreadPagerAdapter

    /**
     * Link del hilo a utilizar en las opciones del menú
     */
    lateinit var link: String

    /**
     * Bus de la aplicación
     */
    lateinit var bus: Bus

    /**
     * Página actual del hilo en la que nos encontramos
     */
    var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread)
        setSupportActionBar(toolbar)
        showCloseButtonOnToolbar()

        // Dado que esta activity puede ser abierta por el usuario sin estar logueado lo comprobamos primero
        val sharedPrefs = (application as App).sharedPrefsComponent.getSharedPreferencesUtils()

        if (AccountManager.isUserLoggedIn(sharedPrefs)) {
            // Intentamos loguear al usuario. Si ocurre algún error lo enviamos a la pantalla de login
            AccountManager.loginWithSavedCredentials(sharedPrefs,
                    {
                        success ->

                        if (!success) {
                            ActivityUtils.showLogin(this)
                        }

                        // Obtenemos el título, link y páginas de los extras del intent
                        var threadLink = intent.extras?.getString(THREAD_LINK) ?: intent.dataString

                        // Dado que los links que nos vienen de un intent filter llevan la URL completa (con http://forocoches...etc)
                        // y para no hacer un cambio completo de la forma en la que tratamos las URLs en las llamadas a la web
                        // mejor curarnos de espanto y quitarle el prefijo
                        threadLink = ApiUtils.removePrefixFromUrl(threadLink)

                        // Inyectamos la activity
                        bus = (application as App).busComponent.getBus()

                        // Guardamos el hilo del link
                        link = threadLink

                        // Deshabilitamos los botones de navegaciones hasta que carguemos los datos del hilo
                        setButtonsEnabled(false)
                        supportActionBar?.title = getString(R.string.general_loading)

                        // Cargamos los datos del hilo
                        ThreadPresenter.loadThreadInfo(link, {
                            thread ->

                            initActivity(thread.title, thread.pages)
                        })
                    })
        } else {
            ActivityUtils.showLogin(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.thread_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val selectedId = item?.itemId

        if (selectedId == R.id.menu_open_in_browser) {
            // Abrimos el enlace del hilo actual
            val browserIntent = IntentUtils.createBrowserIntentChooser(this, ApiConstants.BASE_URL + link)

            if (browserIntent != null) {
                startActivity(browserIntent)
            } else {
                Toast.makeText(this, "No hay ningún navegador instalado", Toast.LENGTH_LONG).show()
            }
        } else if (selectedId == R.id.menu_search_in_thread) {
            // Notificamos el bus sobre la pulsación en la búsqueda para que se muestre en el fragment actual
            bus.publish(ThreadPageSearchEvent(link))
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * Inicializa la activity tras cargar los datos generales del hilo
     */
    fun initActivity(threadTitle: String, threadPages: Int) {
        // Ponemos el título del hilo en la toolbar
        supportActionBar?.title = threadTitle

        // Inicializamos el view pager
        pagerAdapter = ThreadPagerAdapter(supportFragmentManager, link, threadPages)
        vpThreadPages.adapter = pagerAdapter

        // Iniciamos la activity de respuesta al hilo cuando el usuario pulse el FAB
        fab.setOnClickListener {
            startActivity(ReplyThreadActivity.getStartIntent(this, threadTitle, link))
        }

        // Nos subscribimos a los eventos del bus para recibir cuando el usuario ha respondido
        bus.observable().subscribe(
                {
                    event ->

                    // Si es el hilo actual, comprobamos si hay páginas nuevas y nos desplazamos
                    // hasta la última página
                    if (event is RepliedToThreadEvent && event.isSameThread(link)) {
                        pagerAdapter.updatePages(event.newPageCount)
                        vpThreadPages.currentItem = pagerAdapter.totalPages - 1

                        // Notificamos al bus para que el presenter pueda actualizarse
                        bus.publish(PageScrolledEvent(link))
                    }
                },
                {
                    error ->

                    // Nada, silenciamos
                }
        )

        // Actualizamos las páginas cuando nos movamos por el ViewPager
        tvThreadPages.text = getString(R.string.thread_pages_count, currentPage, pagerAdapter.count)
        vpThreadPages.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                // Actualizamos el conteo de páginas
                currentPage = position + 1
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
            currentPage = 1
            vpThreadPages.currentItem = currentPage - 1
        }

        btnThreadPreviousPage.setOnClickListener {
            if (currentPage > 1) {
                currentPage -= 1
                vpThreadPages.currentItem = currentPage - 1
            }
        }

        btnThreadNextPage.setOnClickListener {
            if (currentPage < pagerAdapter.count) {
                currentPage += 1
                vpThreadPages.currentItem = currentPage - 1
            }
        }

        btnThreadLastPage.setOnClickListener {
            vpThreadPages.currentItem = pagerAdapter.count - 1
        }

        // Habilitamos los botones de navegación
        setButtonsEnabled(true)
    }

    /**
     * Habilita/Deshabilita los botones de navegación de la activity
     */
    fun setButtonsEnabled(enabled: Boolean) {
        btnThreadFirstPage.isEnabled = enabled
        btnThreadLastPage.isEnabled = enabled
        btnThreadNextPage.isEnabled = enabled
        btnThreadPreviousPage.isEnabled = enabled
    }
}