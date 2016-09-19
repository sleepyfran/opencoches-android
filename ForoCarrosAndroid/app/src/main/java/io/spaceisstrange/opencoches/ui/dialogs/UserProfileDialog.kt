package io.spaceisstrange.opencoches.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.api.rx.FCUserObservable
import io.spaceisstrange.opencoches.ui.BottomSheetItem
import kotlinx.android.synthetic.main.bottom_sheet_user.view.*

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

class UserProfileDialog : BottomSheetDialog() {

    companion object {
        fun newInstance(userUsername: String, userId: String, picture: String): UserProfileDialog {
            val dialog = UserProfileDialog()
            dialog.userUsername = userUsername
            dialog.userId = userId
            dialog.picture = picture
            return dialog
        }
    }

    /**
     * Nombre de usuario del usuario (jé)
     */
    lateinit var userUsername: String

    /**
     * ID del usuario
     */
    lateinit var userId: String

    /**
     * Imagen de perfil del usuario
     */
    lateinit var picture: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = android.support.design.widget.BottomSheetDialog(context, theme)
        val view = View.inflate(context, R.layout.bottom_sheet_user, null)

        // Configuramos el contenido
        view.rvBottomSheetContent.setHasFixedSize(true)
        view.rvBottomSheetContent.layoutManager = LinearLayoutManager(context)
        adapter.updateItems(getItems())
        view.rvBottomSheetContent.adapter = adapter

        // Cargamos los datos del usuario
        view.tvUserUsername.text = getString(R.string.user_profile_username, userUsername)

        Glide.with(context)
                .load("http:" + picture)
                .crossFade()
                .placeholder(android.R.drawable.progress_horizontal)
                .error(R.drawable.ic_error_black)
                .into(view.ivUserPicture)

        FCUserObservable.create(userId).subscribe(
                {
                    userData ->

                    // Mostramos la información y ocultamos la carga
                    view.pbLoading.visibility = View.GONE

                    // Mostramos la última vez sólo si está disponible
                    if (userData.lastActivity != "") {
                        view.tvUserLastActivity.visibility = View.VISIBLE
                    }

                    view.tvUserRegistrationDate.visibility = View.VISIBLE
                    view.tvUserTotalPosts.visibility = View.VISIBLE

                    view.tvUserLastActivity.text = userData.lastActivity
                    view.tvUserRegistrationDate.text = userData.registrationDate
                    view.tvUserTotalPosts.text = userData.totalPosts
                },
                {
                    error ->

                    // Si hay algún error cerramos el diálogo
                    dismiss()
                }
        )

        dialog.setContentView(view)
        behavior = BottomSheetBehavior.from(view.parent as View)

        return dialog
    }

    override fun onClick(selectedItem: BottomSheetItem) {
        super.onClick(selectedItem)

        // TODO: Añadir como amigo y mandar mensajes
    }

    override fun getItems(): MutableList<BottomSheetItem> {
        return mutableListOf(
                BottomSheetItem(R.drawable.ic_add_circle_black, R.string.user_actions_add_friend),
                BottomSheetItem(R.drawable.ic_mail_black, R.string.user_actions_send_message)
        )
    }

    override fun getTitle(): String? {
        return null
    }
}