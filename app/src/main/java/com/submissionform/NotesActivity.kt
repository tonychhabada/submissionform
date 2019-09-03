package com.submissionform

import androidx.room.Room
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.submissionform.Model.InputDataModel
import com.submissionform.Model.NewNote
import com.submissionform.Model.Notes
import com.submissionform.Room.AppDatabase
import com.submissionform.adapter.FormsAdapter
import com.submissionform.adapter.NotesAdapter
import kotlinx.android.synthetic.main.activity_form_list.*
import kotlinx.android.synthetic.main.activity_notes.*

class NotesActivity : AppCompatActivity() {
    private lateinit var  notes: List<NewNote>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        setTitle("Notes")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        val notes: ArrayList<Notes> = ArrayList()

//        notes.add(Notes("","Notes","testt"))
//        notes.add(Notes("","Notes","testt"))
//        notes.add(Notes("","Notes","testt"))
//        listViewNotes.adapter = NotesAdapter(this,notes);
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.new_crm_form, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.save -> {
                var intent = Intent(this,NewNotesActivity::class.java)
                startActivity(intent)

            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        AsyncTask.execute {
            // Insert Data
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "submission"
            ).build()


            notes = db.inputDao().notes
            runOnUiThread {

                listViewNotes.adapter = NotesAdapter(this,notes)
            }

        }
    }
}
