package com.submissionform

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import com.submissionform.Model.Lead
import com.submissionform.Model.NewNotes
import com.submissionform.Model.Notes
import com.submissionform.utilities.Common
import kotlinx.android.synthetic.main.activity_crmform.*
import kotlinx.android.synthetic.main.activity_new_notes.*
import kotlinx.android.synthetic.main.activity_new_notes.btnSubmit
import java.text.SimpleDateFormat
import java.util.*

class NewNotesActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    var maxid:Long = 0
    var update = false;
    companion object{

        lateinit var note: Notes;
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_notes)
        setTitle("New Note")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        database = FirebaseDatabase.getInstance().reference
        database.addValueEventListener( object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    maxid = p0.childrenCount

                }
            }


        })
        if(intent.hasExtra("update")) {
            update = true;
            btnSubmit.setText("Update")
            edtNotes.setText(note.notesContent)
            edtNotesTitle.setText(note.notesTitle)
        }
        btnSubmit.setOnClickListener {
            val pattern = "EEE dd, MMM yyyy hh:mm:ss "
            val simpleDateFormat = SimpleDateFormat(pattern)
            val date = simpleDateFormat.format(Date())
            if(edtNotes.text.equals("") || edtNotesTitle.equals("")) {
                showDialog("All the fields are required")

            }else if(update) {
                if (!Common.sharedInsance.getBooleanPreference(this, "userEnabled")) {
                    Common.sharedInsance.showDialog(
                        this@NewNotesActivity,
                        "You have been disabled, contact admin to enable."
                    )

                } else {
                    var newNote = NewNotes(
                        edtNotesTitle.text.toString(),
                        edtNotes.text.toString(),
                        Common.sharedInsance.getListPreference(this@NewNotesActivity, "userid"),
                        date
                    )

                    database.child("notes").child(note.id).setValue(newNote)
                    Common.sharedInsance.showDialog(this@NewNotesActivity, "Notes Updated")
                    edtNotesTitle.setText("")
                    edtNotes.setText("")
                    edtNotesTitle.hideKeyboard()
                    edtNotes.hideKeyboard()
                }
            }else {

                if (!Common.sharedInsance.getBooleanPreference(this, "userEnabled")) {
                    Common.sharedInsance.showDialog(
                        this@NewNotesActivity,
                        "You have been disabled, contact admin to enable."
                    )

                } else {
                    var newNote = NewNotes(
                        edtNotesTitle.text.toString(),
                        edtNotes.text.toString(),
                        Common.sharedInsance.getListPreference(this@NewNotesActivity, "userid"),
                        date
                    )
                    database.child("notes").child(maxid.toString()).setValue(newNote)
                    Common.sharedInsance.showDialog(this@NewNotesActivity, "Notes Saved")
                    edtNotesTitle.setText("")
                    edtNotes.setText("")
                    edtNotesTitle.hideKeyboard()
                    edtNotes.hideKeyboard()
                    showDialog("New note saved")
                }
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
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {




            onBackPressed()
            return true


        return super.onOptionsItemSelected(item)
    }

}
