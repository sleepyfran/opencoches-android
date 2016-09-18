package io.spaceisstrange.opencoches.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View

import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.adapters.SubforosAdapter
import io.spaceisstrange.opencoches.api.net.ApiConstants
import io.spaceisstrange.opencoches.api.rx.FCSubforosObservable
import kotlinx.android.synthetic.main.activity_subforos.*

class SubforosActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subforos)
        setSupportActionBar(toolbar)

        // Ponemos de título el nombre del usuario
        showUsernameOnToolbar()

        // Configuramos la RecyclerView
        val subforoAdapter = SubforosAdapter()

        // Configuramos el onClick del adapter para que abra el subforo que el usuario ha pulsado
        subforoAdapter.onClick = {
            subforo ->

            val subforoIntent = Intent(this, SubforoThreadsActivity::class.java)
            subforoIntent.putExtra(ApiConstants.THREAD_TITLE_KEY, subforo.title)
            subforoIntent.putExtra(ApiConstants.THREAD_LINK_KEY, subforo.link)
            startActivity(subforoIntent)
        }

        rvSubList.adapter = subforoAdapter
        rvSubList.layoutManager = LinearLayoutManager(this)

        // Obtenemos la lista de subforos y populamos el RecyclerView con ella
        FCSubforosObservable.create().subscribe(
                {
                    subforos ->

                    // Ocultamos la carita del error si no lo está ya
                    hideErrorMessage(vError)

                    subforoAdapter.updateSubforos(subforos)
                },
                {
                    error ->

                    // Mostramos la carita cuca con el error
                    subforoAdapter.updateSubforos(mutableListOf())
                    showErrorMessage(vError)
                }
        )
    }
}