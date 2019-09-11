package com.submissionform

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.submissionform.Model.Documents
import com.submissionform.Model.MyDocuments
import com.submissionform.Model.Notes
import com.submissionform.adapter.MyDocumentsAdapter
import com.submissionform.adapter.NotesAdapter
import com.submissionform.utilities.Common
import kotlinx.android.synthetic.main.activity_my_documents.*
import kotlinx.android.synthetic.main.activity_notes.*

class MyDocumentsActivity : AppCompatActivity() {
    var list = java.util.ArrayList<Documents>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_documents)
        setTitle("Documents")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

        listViewDocuments.setOnItemClickListener { parent, view, position, id ->
            var intent = Intent(this,DocumentDetailsActivity::class.java)
            intent.putExtra("imageUrl",list.get(position).documentURL)
            intent.putExtra("id",list.get(position).documentId)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
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
        val query = reference.child("documents").orderByChild("userid").equalTo(Common.sharedInsance.getListPreference(this,"userid"))
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    var cd = dataSnapshot.children
                    for (data in dataSnapshot.children) {
                        var documents = Documents(data.key.toString(),data.child("documentUrl").value.toString())
                        list.add(documents)
                        listViewDocuments.adapter = MyDocumentsAdapter(this@MyDocumentsActivity,list)

                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}
