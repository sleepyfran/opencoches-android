package io.spaceisstrange.forocarrosandroid.ui.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar

import io.spaceisstrange.forocarrosandroid.R
import io.spaceisstrange.forocarrosandroid.ui.fragments.SubforosFragment

class LandingActivity : BaseActivity() {
    companion object {
        /**
         * Constructor alternativo por si queremos mostrar un fragment que no sea el de los
         * subforos por defecto
         */
        fun newInstance(fragment: Fragment): LandingActivity {
            val activity = LandingActivity()
            activity.fragment = fragment
            return activity
        }
    }

    // Mostramos por defecto la lista de subforos
    var fragment: Fragment = SubforosFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        // Ponemos de título el nombre del usuario
        mostrarNombreUsuarioEnToolbar()

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, SubforosFragment())
                .commit()
    }
}
