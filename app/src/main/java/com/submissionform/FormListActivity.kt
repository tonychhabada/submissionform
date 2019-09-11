package com.submissionform

import androidx.room.Room
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import com.submissionform.Model.InputDataModel
import com.submissionform.Room.AppDatabase
import com.submissionform.adapter.FormsAdapter
import kotlinx.android.synthetic.main.activity_crmform.*
import kotlinx.android.synthetic.main.activity_form_list.*

import android.view.ContextMenu.ContextMenuInfo
import android.widget.Toast
import com.google.firebase.database.*
import com.submissionform.Model.Lead
import com.submissionform.utilities.Common
import kotlinx.android.synthetic.main.activity_login.*


class FormListActivity : AppCompatActivity() {
    var list = ArrayList<Lead>();
private lateinit var  forms: List<InputDataModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_list)
        setTitle("Leads List")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);




        formListView.setOnItemClickListener { parent, view, position, id ->
            var intent = Intent(this,CRMFormActivity::class.java)
            intent.putExtra("update",true)
            CRMFormActivity.form = list.get(position)
            startActivity(intent)
        }

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
//            forms = db.inputDao().all
//            runOnUiThread {
//
//                formListView.adapter = FormsAdapter(this,forms)
//            }
//
//        }
list.clear()
        val reference = FirebaseDatabase.getInstance().reference
        val query = reference.child("leads").orderByChild("userid").equalTo(Common.sharedInsance.getListPreference(this,"userid"))
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    var cd = dataSnapshot.children
                    for (data in dataSnapshot.children) {

                        var lead = Lead(data.child("fullName").value.toString(),data.child("email").value.toString(),data.child("phone").value.toString(),data.child("loanNotes").value.toString(),data.child("applicationComplete").value.toString(),data.child("referralSource").value.toString(),data.child("userid").value.toString(),data.child("createdDate").value.toString(),data.key!!,data.child("referralSourceOther").value.toString(),data.child("whetherReferralSourceOther").value as Boolean)
                        list.add(lead)

                    }
                    formListView.adapter = FormsAdapter(this@FormListActivity,list)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.new_crm_form, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item?.itemId == R.id.save){
            var intent = Intent(this,CRMFormActivity::class.java)
            startActivity(intent)

        }else {


                onBackPressed()
                return true

        }
        return super.onOptionsItemSelected(item)
    }



}
