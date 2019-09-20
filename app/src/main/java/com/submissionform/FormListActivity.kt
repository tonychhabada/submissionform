package com.submissionform

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import com.submissionform.Model.InputDataModel
import com.submissionform.adapter.FormsAdapter
import kotlinx.android.synthetic.main.activity_form_list.*
import com.google.firebase.database.*
import com.submissionform.Model.Lead
import com.submissionform.utilities.Common

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
        list.clear()
        val reference = FirebaseDatabase.getInstance().reference
        val query = reference.child("leads").orderByChild("userid").equalTo(Common.sharedInsance.getListPreference(this,"userid"))
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (data in dataSnapshot.children) {
                        var lead = Lead(data.child("fullName").value.toString(),data.child("email").value.toString(),data.child("phone").value.toString(),data.child("loanNotes").value.toString(),data.child("applicationComplete").value.toString(),data.child("referralSource").value.toString(),data.child("userid").value.toString(),data.child("createdDate").value.toString(),data.key!!,data.child("referralSourceOther").value.toString())
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
