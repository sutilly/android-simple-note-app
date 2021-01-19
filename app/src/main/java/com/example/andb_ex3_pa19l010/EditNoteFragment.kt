package com.example.andb_ex3_pa19l010

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.fragment_edit_note.*
import java.util.*

private const val CHANNEL_ID = "42"
private const val REQUEST_CODE = 101

class EditNoteFragment : Fragment() {

    val passedNoteId: EditNoteFragmentArgs by navArgs()
    lateinit var passedNote: Note

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        val rootView = inflater.inflate(R.layout.fragment_edit_note, container, false)

        val toolbar = rootView.findViewById<Toolbar>(R.id.toolbar_editFragment)
        toolbar.title = "Edit Note"

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        passedNote = mainActivity.getNoteFromId(passedNoteId.noteId)

        populateTextViews(passedNote)
        addAllTextChangeListeners()

        fab_saveNote.setOnClickListener() {
            updateOrCreateItem(passedNote)
            createNotificationChannel()
            showNotification(passedNote)
        }

        btn_chooseDate.setOnClickListener() {
            showDatePickerDialog()
        }

        iv_noteImage.setOnClickListener() {
            checkForPermission()
        }
    }

    private fun populateTextViews(note: Note) {
        et_title.setText(note.title)
        et_content.setText(note.content)
        et_date.text = note.date
        iv_noteImage.setImageURI(Uri.parse(note.imgSrc))
    }


    fun addAllTextChangeListeners() {

        et_title.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                passedNote.title = et_title.text.toString()
            }
        })

        et_content.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                passedNote.content = et_content.text.toString()
            }
        })

        et_date.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                passedNote.date = et_date.text.toString()
            }
        })
    }

    private fun showDatePickerDialog() {
        val mainActivity = activity as MainActivity
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()
        picker.show(mainActivity.supportFragmentManager, picker.toString())

        picker.addOnPositiveButtonClickListener {
            val chosenDate = Date(it)
            val formattedDate = formattingDates(chosenDate)
            et_date.text = formattedDate
        }
    }

    private fun updateOrCreateItem(note: Note) {

        val notesList = (activity as MainActivity).loadSharedPrefsData()
        if (notesList.find { it.id == note.id } != null) {
            updateListItem(note)
        } else {
            createListItem(note)
        }

        val navCtrl = Navigation.findNavController(requireView())
        navCtrl.navigate(EditNoteFragmentDirections.actionEditNoteFragmentToNoteListFragment())
    }

    private fun updateListItem(editedNote: Note) {
        val notesList = (activity as MainActivity).loadSharedPrefsData()
        for (note in notesList) {
            if (note.id == editedNote.id) {
                note.title = editedNote.title
                note.content = editedNote.content
                note.date = editedNote.date
                note.imgSrc = editedNote.imgSrc
            }
        }
        (activity as MainActivity).saveDataToSharedPrefs(notesList)
    }

    private fun createListItem(editedNote: Note) {
        val notesList = (activity as MainActivity).loadSharedPrefsData()
        notesList.add(editedNote)
        (activity as MainActivity).saveDataToSharedPrefs(notesList)
    }

    private fun createNotificationChannel() {
        val notificationChannel =
            NotificationChannel(CHANNEL_ID, "Channel Name", NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun showNotification(note: Note) {

        val source =
            ImageDecoder.createSource(requireActivity().contentResolver, Uri.parse(note.imgSrc))
        val bitmap = ImageDecoder.decodeBitmap(source)

        val notification = NotificationCompat.Builder(requireActivity(), CHANNEL_ID)
            .setContentTitle(note.title)
            .setContentText(note.content)
            .setSmallIcon(R.drawable.ic_baseline_note_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setLargeIcon(bitmap)
            .setAutoCancel(true)
            .build()
        val notificationManager = NotificationManagerCompat.from(requireContext())
        notificationManager.notify(42, notification)
    }

    // Permission + Picture Intent

    private fun checkForPermission() {
        when {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                showPhotoDialog()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                showExplanationDialog()
            }
            else -> {
                requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showPhotoDialog()
            } else {
                showExplanationDialog()
            }
            return
        }
    }

    private fun showPhotoDialog() {

        val options = arrayOf("Choose from Gallery", "Take a picture")

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add a photo")
        builder.setItems(options) { _, which ->

            when (options[which]) {
                "Choose from Gallery" -> choosePhotoFromGallery()
                "Take a picture" -> takePhoto()
            }
        }
        builder.show()
    }

    private fun choosePhotoFromGallery() {
        val chooseFromGallery = Intent(Intent.ACTION_OPEN_DOCUMENT)
        chooseFromGallery.type = "image/*"
        startActivityForResult(chooseFromGallery, REQUEST_CODE)
    }

    private fun takePhoto() {
        Toast.makeText(requireContext(), "This function is not yet implemented!", Toast.LENGTH_LONG)
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101) {
            if (data != null) {
                val contentURI = data.data
                passedNote.imgSrc = contentURI.toString()
                iv_noteImage.setImageURI(contentURI)
            }
        }
    }

    private fun showExplanationDialog() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Please grant permission")
        builder.setMessage("We need your permission to access your media library, otherwise we can't provide personalized note pictures.")
        builder.setPositiveButton("OK") { _, _ ->
            requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE
            )
        }
        builder.setNegativeButton("NO") { _, _ ->
            Toast.makeText(requireContext(), getString(R.string.reaction_decline_permission), Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }


}





