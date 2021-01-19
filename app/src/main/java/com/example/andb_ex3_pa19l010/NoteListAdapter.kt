package com.example.andb_ex3_pa19l010

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView

class NoteListAdapter(private val notes: ArrayList<Note>) :
    RecyclerView.Adapter<NoteListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_listitem, parent, false) as LinearLayout
        return NoteListViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        val note = notes.get(position)
        holder.layout.findViewById<TextView>(R.id.tv_noteTitle).text = note.title
        holder.layout.findViewById<TextView>(R.id.tv_noteDate).text = note.date
        holder.layout.setOnClickListener() {
            val navCtrl = Navigation.findNavController(holder.layout)
            navCtrl.navigate(
                NoteListFragmentDirections.actionNoteListFragmentToEditNoteFragment(
                    note.id
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

}

class NoteListViewHolder(val layout: LinearLayout) : RecyclerView.ViewHolder(layout)
