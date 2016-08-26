package io.spaceisstrange.forocarrosandroid.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.spaceisstrange.forocarrosandroid.R
import io.spaceisstrange.forocarrosandroid.adapters.SubforosAdapter
import io.spaceisstrange.forocarrosandroid.api.rx.FCSubforosObservable
import kotlinx.android.synthetic.main.fragment_subforos.*

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

class SubforosFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_subforos, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuramos la RecyclerView
        val subforoAdapter = SubforosAdapter()
        rvSubList.adapter = subforoAdapter
        rvSubList.layoutManager = LinearLayoutManager(context)

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
}