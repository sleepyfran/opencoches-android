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

package io.spaceisstrange.opencoches.ui.profile

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.model.UserData
import io.spaceisstrange.opencoches.ui.common.bottomsheet.BottomSheetDialog
import io.spaceisstrange.opencoches.ui.common.bottomsheet.BottomSheetItem
import kotlinx.android.synthetic.main.bottom_sheet_user.view.*
import javax.inject.Inject

class ProfileDialog : BottomSheetDialog(), ProfileContract.View {
    /**
     * Presenter asociado a nuestra view
     */
    @Inject lateinit var profilePresenter: ProfilePresenter

    /**
     * View del dialog
     */
    lateinit var dialogView: View

    /**
     * Nombre de usuario del usuario (jé)
     */
    lateinit var userUsername: String

    /**
     * ID del usuario
     */
    lateinit var userLink: String

    /**
     * Imagen de perfil del usuario
     */
    lateinit var picture: String

    companion object {
        fun newInstance(userUsername: String, userId: String, picture: String): ProfileDialog {
            val dialog = ProfileDialog()
            dialog.userUsername = userUsername
            dialog.userLink = userId
            dialog.picture = picture
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = android.support.design.widget.BottomSheetDialog(context, theme)
        dialogView = View.inflate(context, R.layout.bottom_sheet_user, null)

        // Inyectamos el Dialog
        DaggerProfileComponent.builder()
                .profileModule(ProfileModule(this, userLink))
                .build()
                .inject(this)

        // Configuramos el contenido
        dialogView.rvBottomSheetContent.setHasFixedSize(true)
        dialogView.rvBottomSheetContent.layoutManager = LinearLayoutManager(context)
        adapter.updateItems(getItems())
        dialogView.rvBottomSheetContent.adapter = adapter

        // Mostramos los datos que ya sabemos del usuario
        dialogView.tvUserUsername.text = getString(R.string.user_profile_username, userUsername)

        Glide.with(context)
                .load("http:" + picture)
                .crossFade()
                .placeholder(android.R.drawable.progress_horizontal)
                .error(R.drawable.ic_error_white)
                .into(dialogView.ivUserPicture)

        dialog.setContentView(dialogView)
        behavior = BottomSheetBehavior.from(dialogView.parent as View)

        // Cargamos los datos restantes
        profilePresenter.init()

        return dialog
    }

    override fun onClick(selectedItem: BottomSheetItem) {
        super.onClick(selectedItem)

        // TODO: Añadir como amigo y mandar mensajes
        if (selectedItem.drawable == R.drawable.ic_mail_white) {
            // TODO: Mostrar la activity de envío de mensajes
        }
    }

    override fun getItems(): MutableList<BottomSheetItem> {
        return mutableListOf(
                BottomSheetItem(R.drawable.ic_add_circle_white, R.string.user_actions_add_friend),
                BottomSheetItem(R.drawable.ic_mail_white, R.string.user_actions_send_message)
        )
    }

    override fun getTitle(): String? {
        return null
    }

    override fun setPresenter(presenter: ProfilePresenter) {
        // Nada
    }

    override fun showUserInfo(userData: UserData) {
        // Mostramos la última vez sólo si está disponible
        if (userData.lastActivity != "") {
            dialogView.tvUserLastActivity.visibility = View.VISIBLE
        }

        dialogView.tvUserRegistrationDate.visibility = View.VISIBLE
        dialogView.tvUserTotalPosts.visibility = View.VISIBLE

        dialogView.tvUserLastActivity.text = userData.lastActivity
        dialogView.tvUserRegistrationDate.text = userData.registrationDate
        dialogView.tvUserTotalPosts.text = userData.totalPosts
    }

    override fun showLoading(show: Boolean) {
        if (show) {
            dialogView.pbLoading.visibility = View.VISIBLE
        } else {
            dialogView.pbLoading.visibility = View.GONE
        }
    }

    override fun showError(show: Boolean) {
        if (show) {
            dialogView.pbLoading.visibility = View.GONE
            dialogView.llBottomSheetUserInfo.visibility = View.GONE
            dialogView.rvBottomSheetContent.visibility = View.GONE
            dialogView.vError.visibility = View.VISIBLE
        } else {
            dialogView.llBottomSheetUserInfo.visibility = View.VISIBLE
            dialogView.rvBottomSheetContent.visibility = View.VISIBLE
            dialogView.vError.visibility = View.GONE
        }
    }
}