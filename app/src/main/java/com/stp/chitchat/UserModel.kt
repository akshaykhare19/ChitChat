package com.stp.chitchat

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

data class UserModel(
    var phoneNumber: String? = "",
    var userName: String = "",
    var userImage: String = "",
    var userBio: String = "",
    val userId: String = "",
    val online: String = "Offline",
    val typing: String = "false"
) {

    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, imageUrl: String?) {
            imageUrl?.let {
                Glide.with(view.context).load(imageUrl).into(view)
            }
        }
    }

}
