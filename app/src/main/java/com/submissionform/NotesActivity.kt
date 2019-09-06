package com.submissionform

import androidx.room.Room
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.submissionform.Model.Lead

import com.submissionform.Model.Notes
import com.submissionform.adapter.NotesAdapter
import com.submissionform.utilities.Common

import kotlinx.android.synthetic.main.activity_notes.*

class NotesActivity : AppCompatActivity() {
    var list = java.util.ArrayList<Notes>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        setTitle("Notes")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        listViewNotes.setOnItemClickListener { parent, view, position, id ->
            var intent = Intent(this,NewNotesActivity::class.java)
            intent.putExtra("update",true)
            NewNotesActivity.note = list.get(position)

            startActivity(intent)
        }

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



        list.clear()
        val reference = FirebaseDatabase.getInstance().reference
        val query = reference.child("notes").orderByChild("userid").equalTo(Common.sharedInsance.getListPreference(this,"userid"))
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    var cd = dataSnapshot.children
                    for (data in dataSnapshot.children) {
                        var notes = Notes(data.key.toString(),data.child("title").value.toString(),data.child("note").value.toString(),data.child("createdDate").value.toString())
                        list.add(notes)
                        listViewNotes.adapter = NotesAdapter(this@NotesActivity,list)

                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}
