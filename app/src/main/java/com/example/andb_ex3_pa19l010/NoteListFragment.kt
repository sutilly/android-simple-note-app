package com.example.andb_ex3_pa19l010

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class NoteListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        val rootView = inflater.inflate(R.layout.fragment_note_list, container, false)
        val rv = rootView.findViewById<RecyclerView>(R.id.rv_noteList)


        val notesList = (activity as MainActivity?)!!.loadSharedPrefsData()
        rv.adapter = NoteListAdapter(notesList)
        rv.layoutManager =
            LinearLayoutManager(rootView.context, LinearLayoutManager.VERTICAL, false)
        rv.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        return rootView
    }

}