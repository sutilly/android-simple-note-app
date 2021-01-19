package com.example.andb_ex3_pa19l010

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_search_dialog.view.*
import java.util.*

class SearchDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {

        val searchInput = layoutInflater.inflate(R.layout.fragment_search_dialog, null)
        val activity = activity as MainActivity

        fun searchNote(title: String?) {
            val notes = activity.loadSharedPrefsData()
            for (note in notes)
                if (note.title?.toLowerCase(Locale.ROOT) == title) {
                return activity.navigateToEditFragment(note.id)
            }
            Snackbar.make(
                activity.findViewById(android.R.id.content),
                R.string.no_result,
                Snackbar.LENGTH_LONG
            )
                .show()

        }

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.search_title)
            .setIcon(R.drawable.ic_baseline_search_28)
            .setView(searchInput)
            .setCancelable(false)
            .setPositiveButton(R.string.search_posBtn) { _, _ ->
                val searchTerm = searchInput.et_searchTerm.text.toString().toLowerCase(Locale.ROOT)
                searchNote(searchTerm)
            }
            .setNegativeButton(R.string.search_negBtn) { _, _ ->
                this.dismiss()
            }
            .create()
    }

}