package com.example.andb_ex3_pa19l010

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        val navController = Navigation.findNavController(this, R.id.fl_fragmentContainer)
        setupActionBar(navController)

        // check if loading of default data is necessary
        checkSharedPrefs()
    }

    private fun setupActionBar(navController: NavController) {
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.editNoteFragment -> navigateToEditFragment(0)
            R.id.search -> displaySearchDialog()
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    internal fun navigateToEditFragment(id: Int) {

        val navController = Navigation.findNavController(this, R.id.fl_fragmentContainer)
        navController.navigate(
            NoteListFragmentDirections.actionNoteListFragmentToEditNoteFragment(id)
        )
    }

    internal fun saveDataToSharedPrefs(notes: ArrayList<Note>) {
        val sharedPreferences = getSharedPreferences("Notes", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json: String = gson.toJson(notes)
        editor.putString("note list", json)
        editor.apply()
    }

    internal fun loadSharedPrefsData(): ArrayList<Note> {
        val sharedPreferences = getSharedPreferences("Notes", Context.MODE_PRIVATE)
        val gson = Gson()
        val json: String? = sharedPreferences.getString("note list", null)
        val type = object : TypeToken<ArrayList<Note>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun checkSharedPrefs() {
        val sharedPrefs: SharedPreferences = getSharedPreferences("Notes", Context.MODE_PRIVATE)
        if (!sharedPrefs.contains("note list")) {
            saveDataToSharedPrefs(DataManager.notesList)
        }
    }

    fun getNoteFromId(id: Int): Note {
        val data = loadSharedPrefsData()
        var requiredNote = Note(
            getString(R.string.new_note_title),
            getString(R.string.new_note_content),
            formattingDates(Date()),
            createID(),
            "android.resource://com.example.andb_ex3_pa19l010/" + R.drawable.note
        )
        for (note in data) {
            if (note.id == id) {
                requiredNote = note
            }
        }
        return requiredNote
    }

    private fun displaySearchDialog() {
        SearchDialogFragment().show(supportFragmentManager, "search dialog")
    }


}


