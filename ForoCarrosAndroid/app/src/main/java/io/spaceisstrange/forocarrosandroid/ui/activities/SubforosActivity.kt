package io.spaceisstrange.forocarrosandroid.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem

import io.spaceisstrange.forocarrosandroid.R
import io.spaceisstrange.forocarrosandroid.adapters.SubforosAdapter
import io.spaceisstrange.forocarrosandroid.api.rx.FCSubforosObservable
import io.spaceisstrange.forocarrosandroid.utils.SharedPreferencesUtils
import kotlinx.android.synthetic.main.activity_subforos.*

class SubforosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subforos)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        // Ponemos de título el nombre del usuario
        supportActionBar?.title = SharedPreferencesUtils.getUsername(this)

        // Configuramos la RecyclerView
        val subforoAdapter = SubforosAdapter()
        rvSubList.adapter = subforoAdapter
        rvSubList.layoutManager = LinearLayoutManager(this)

        // Obtenemos la lista de subforos y populamos el RecyclerView con ella
        FCSubforosObservable.create().subscribe(
                {
                    subforos ->

                    subforoAdapter.updateSubforos(subforos)
                },
                {
                    error ->

                    // TODO: Hacerse cargo de los malditos errors
                }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.default_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val selectedId = item?.itemId

        // TODO: Hacerse cargo del menú

        return super.onOptionsItemSelected(item)
    }
}
