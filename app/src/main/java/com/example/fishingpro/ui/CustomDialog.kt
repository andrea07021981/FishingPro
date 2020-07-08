package com.example.fishingpro.ui

import android.app.Activity
import android.app.Dialog
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.fishingpro.R

class CustomDialog(
    private var activity: Activity?
) {

    //TODO clear class
    private var dialog: Dialog? = null

    fun showDialog() {
        dialog = Dialog(requireNotNull(activity))
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //...set cancelable false so that it's never get hidden
        dialog!!.setCancelable(false)
        //...that's the layout i told you will inflate later
        dialog!!.setContentView(R.layout.custom_loading)

        //...initialize the imageView form infalted layout
        val gifImageView: ImageView = dialog!!.findViewById(R.id.custom_loading_imageView)

        //...now load that gif which we put inside the drawble folder here with the help of Glide
        Glide.with(activity!!)
            .load(R.drawable.loading)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading)
            .centerCrop())
            .into(gifImageView)

        //...finaly show it
        dialog!!.show()
    }

    //..also create a method which will hide the dialog when some work is done
    fun hideDialog() {
        dialog!!.dismiss()
    }
}