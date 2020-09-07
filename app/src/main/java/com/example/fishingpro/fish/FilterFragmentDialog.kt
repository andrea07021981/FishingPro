package com.example.fishingpro.fish

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.fishingpro.R
import java.util.zip.Inflater

class FilterFragmentDialog(
    private val dismissListener: OnDialogDismissListener
) : DialogFragment(), AdapterView.OnItemSelectedListener {

    var monthSelected: Long = 0

    companion object {
        private val TAG = FilterFragmentDialog::class.java.simpleName
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alert = AlertDialog.Builder(requireActivity())
            .setPositiveButton("OK") { _, _ ->
                dismissListener.dismissWithParams(bundleOf("MONTH" to monthSelected))
            }
            .setNegativeButton("CANCEL") { dialogInterface, _ ->
                dialogInterface.cancel()
            }

        val inflate = layoutInflater.inflate(R.layout.fragment_filter, null)
        alert.setView(inflate)
        inflate.findViewById<Spinner>(R.id.month_spinner).onItemSelectedListener = this
        return alert.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onItemSelected(adapterView: AdapterView<*>?, p1: View?, pos: Int, id: Long) {
        Log.d(TAG, "Value selected: ${adapterView?.adapter?.getItem(pos)}")
        monthSelected = id
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        monthSelected = 0L
    }

    class OnDialogDismissListener(
        val onDismissListener: (Bundle) -> Unit
    ) {
        fun dismissWithParams(params: Bundle) = onDismissListener(params)
    }
}