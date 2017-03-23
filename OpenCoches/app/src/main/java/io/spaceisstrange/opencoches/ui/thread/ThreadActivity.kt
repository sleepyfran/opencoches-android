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

package io.spaceisstrange.opencoches.ui.thread

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.AccountManager
import io.spaceisstrange.opencoches.data.api.ApiUtils
import io.spaceisstrange.opencoches.data.api.thread.ThreadInfo
import io.spaceisstrange.opencoches.data.bus.Bus
import io.spaceisstrange.opencoches.data.bus.events.PageScrolledEvent
import io.spaceisstrange.opencoches.data.bus.events.RepliedToThreadEvent
import io.spaceisstrange.opencoches.ui.common.BaseActivity
import io.spaceisstrange.opencoches.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_thread.*

/**
 * Activity encargada de mostrar todas las páginas de un hilo junto con su navegación.
 */
class ThreadActivity : BaseActivity() {
    companion object {
        /**
         * Clave asociada al link
         */
        val THREAD_LINK = "threadLink"

        /**
         * Retorna un Intent con los parámetros necesarios para inicializar la activity
         */
        fun startIntent(context: Context, link: String): Intent {
            val startIntent = Intent(context, ThreadActivity::class.java)
            startIntent.putExtra(THREAD_LINK, link)
            return startIntent
        }
    }

    /**
     * Link del hilo que vamos a cargar.
     */
    lateinit var link: String

    /**
     * Adapter del ViewPager.
     */
    lateinit var pagerAdapter: ThreadPagerAdapter

    /**
     * Página actual en el hilo.
     */
    var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread)
        setSupportActionBar(toolbar)
        showToolbarCloseButton()

        // Dado que esta activity puede ser abierta sin necesidad de que el usuario esté logueado
        // lo comprobamos antes de continuar. Si no está logueado, lo mandamos a hacerlo
        if (AccountManager.isUserLoggedIn()) {
            attemptLogin()
        } else {
            showLogin()
        }

        // Configuramos el bus de la aplicación
        Bus.instance.observable().subscribe(
                {
                    event ->

                    // Si es el hilo actual, comprobamos si hay páginas nuevas y nos desplazamos
                    // hasta la última página
                    if (event is RepliedToThreadEvent && event.isSameThread(link)) {
                        pagerAdapter.updatePages(event.newPageCount)
                        threadContent.currentItem = pagerAdapter.totalPages - 1

                        // Notificamos al bus para poder hacer scroll hasta el final de la página
                        Bus.instance.publish(PageScrolledEvent(link))
                    }
                },
                {
                    error ->

                    // Nada, silenciamos
                }
        )
    }

    /**
     * Intenta loguear al usuario para poder continuar con la ejecución.
     */
    fun attemptLogin() {
        AccountManager.loginWithSavedCredentials().subscribe(
                {
                    loggedIn ->

                    if (!loggedIn) showLogin()

                    // Obtenemos los datos de los extras del intent
                    var link = intent.extras?.getString(THREAD_LINK) ?: intent.dataString

                    // Dado que los links que nos vienen de un intent filter llevan la URL completa
                    // (con http://forocoches...etc) y para no hacer un cambio completo de la forma
                    // en la que tratamos las URLs en las llamadas a la web mejor curarnos de espanto
                    // y quitarle el prefijo
                    link = ApiUtils.removePrefixFromUrl(link)

                    // Guardamos el link del hilo ya preparado
                    this.link = link

                    // Terminamos de configurar la activity
                    buttonsEnabled(false)
                    setToolbarTitle(getString(R.string.general_loading))

                    // Cargamos los datos del hilo
                    loadThread()
                }
        )
    }

    /**
     * Carga los datos y el contenido del hilo y lo muestra en la activity.
     */
    fun loadThread() {
        // Cargamos primero los datos del hilo
        ThreadInfo(link).observable().subscribe(
                {
                    thread ->

                    // Terminamos de configurar la activity
                    setToolbarTitle(thread.title)
                    pagerAdapter = ThreadPagerAdapter(supportFragmentManager, thread.link, thread.pages, thread.title)
                    threadContent.adapter = pagerAdapter
                    setupActivity()
                },
                {
                    error ->

                    // TODO: Hacer algo con el error
                }
        )
    }

    /**
     * Configura el ViewPager de la activity y el resto de botones necesarios tras cargar
     * la información del hilo.
     */
    fun setupActivity() {
        // Actualizamos la página actual al movernos por el ViewPager
        updatePageCount()
        threadContent.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                // Actualizamos el conteo de páginas
                currentPage = position + 1
                updatePageCount()
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Nada
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Nada
            }
        })

        // Configuramos el fab para iniciar la activity de respuesta
        fab.setOnClickListener {
            // TODO: Iniciar la activity de respuesta al pulsar el fab
        }

        // Establecemos las opciones de los botones de navegación
        threadFirstPage.setOnClickListener {
            currentPage = 1
            threadContent.currentItem = currentPage - 1
        }

        threadPreviousPage.setOnClickListener {
            if (currentPage > 1) {
                currentPage -= 1
                threadContent.currentItem = currentPage - 1
            }
        }

        threadNextPage.setOnClickListener {
            if (currentPage < pagerAdapter.count) {
                currentPage += 1
                threadContent.currentItem = currentPage - 1
            }
        }

        threadLastPage.setOnClickListener {
            threadContent.currentItem = pagerAdapter.count - 1
        }

        // Habilitamos los botones de navegación
        buttonsEnabled(true)
    }

    /**
     * Muestra la activity de inicio de sesión.
     */
    fun showLogin() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }

    /**
     * Actualiza el contador de páginas de la barra de navegación.
     */
    fun updatePageCount() {
        threadPages.text = getString(R.string.thread_pages_count, currentPage, pagerAdapter.count)
    }

    /**
     * Habilita o deshabilita los botones de navegación.
     */
    fun buttonsEnabled(enabled: Boolean) {
        threadFirstPage.isEnabled = enabled
        threadLastPage.isEnabled = enabled
        threadNextPage.isEnabled = enabled
        threadPreviousPage.isEnabled = enabled
    }
}