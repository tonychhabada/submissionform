package com.submissionform

import androidx.room.Room
import android.content.DialogInterface
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.submissionform.Model.NewNote
import com.submissionform.Room.AppDatabase
import kotlinx.android.synthetic.main.activity_new_notes.*
import kotlinx.android.synthetic.main.custom_notes_row.*

class NewNotesActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_notes)
        setTitle("New Note")
        database = FirebaseDatabase.getInstance().reference
        btnSubmit.setOnClickListener {
            if(edtNotes.text.equals("") || edtNotesTitle.equals("")){
                showDialog("All the fields are required")

            }else{

                AsyncTask.execute {
                    // Insert Data
                    val db = Room.databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java, "submission"
                    ).build()

                    var newNote = NewNote();

                    newNote.notesTitle = edtNotesTitle.text.toString()
                    newNote.note = edtNotes.text.toString()

                    db.inputDao().insertNewNote(newNote)

                }
                edtNotesTitle.setText("")
                edtNotes.setText("")
                showDialog("New note saved")
            }
        }
    }

    fun showDialog(message:String){
        AlertDialog.Builder(this)
//            .setTitle("Delete entry")
            .setMessage(message)

            // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, which ->
                // Continue with delete operation
            })

            // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()

    }

}
