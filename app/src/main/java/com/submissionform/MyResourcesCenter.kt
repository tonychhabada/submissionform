package com.submissionform

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.submissionform.Model.Notes
import com.submissionform.Model.Resources
import com.submissionform.adapter.NotesAdapter
import com.submissionform.adapter.ReourcesAdapter
import com.submissionform.utilities.Common
import kotlinx.android.synthetic.main.activity_my_resources_center.*
import kotlinx.android.synthetic.main.activity_new_resource.*
import kotlinx.android.synthetic.main.activity_notes.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import java.lang.Exception


class MyResourcesCenter : AppCompatActivity() {
    var list = java.util.ArrayList<Resources>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_resources_center)
        title = "My Resources"

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);


    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item?.itemId == R.id.save){
            var intent = Intent(this,NewResourceActivity::class.java)
            startActivity(intent)

        }else {


            onBackPressed()
            return true

        }
        return super.onOptionsItemSelected(item)
    }
    override fun onResume() {
        super.onResume()
//        AsyncTask.execute {
//            // Insert Data
//            val db = Room.databaseBuilder(
//                applicationContext,
//                AppDatabase::class.java, "submission"
//            ).build()
//
//
////            notes = db.inputDao().notes
////            runOnUiThread {
////
////                listViewNotes.adapter = NotesAdapter(this,notes)
////            }
//
//        }


        list.clear()
        val reference = FirebaseDatabase.getInstance().reference
        val query = reference.child("resources").orderByChild("userid").equalTo(Common.sharedInsance.getListPreference(this,"userid"))
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    var cd = dataSnapshot.children
                    for (data in dataSnapshot.children) {
//                        var notes = Notes(data.key.toString(),data.child("title").value.toString(),data.child("note").value.toString())
                        var resources = Resources(data.child("userid").value.toString(),data.child("resourceLink").value.toString(),data.child("resourceLinkDescription").value.toString())
                        list.add(resources)
                        listViewResources.adapter = ReourcesAdapter(this@MyResourcesCenter,list)

                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        listViewResources.setOnItemClickListener(object : OnItemClickListener {

            override fun onItemClick(
                parent: AdapterView<*>, view: View,
                position: Int, id: Long
            ) {

                try {
                    var resource = list.get(position)
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(resource.resourceLink))
                    startActivity(browserIntent)
                }catch(e:Exception){


                }
            }

        })

//        listViewResources.setOnItem {
//
//            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))
//            startActivity(browserIntent)
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.new_crm_form, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
