package de.fbirk.doubleout.ui.game.main

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import de.fbirk.doubleout.R
import java.lang.ClassCastException
import java.lang.IllegalStateException

class GameFinishedDialogFragment : DialogFragment() {
    internal lateinit var listener: GameFinishedDialogListener

    interface GameFinishedDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //try {
        //  listener = context as GameFinishedDialogListener
        //} catch (e: ClassCastException) {
        //    throw ClassCastException(("$context must implement GameFinishedDialogListener"))
        //}
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            var builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.txt_dialog_message)
                //  .setPositiveButton(R.string.btn_dialog_again,
                //        DialogInterface.OnClickListener { dialog, id ->
                //            //listener.onDialogPositiveClick(this)
                //        })
                .setNegativeButton(R.string.btn_dialog_back,
                    DialogInterface.OnClickListener { dialog, id ->
                        //listener.onDialogNegativeClick(this)
                        findNavController().navigate(R.id.action_gameMainView_to_mainFragment)
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}