package com.submissionform

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.firebase.database.*
import com.submissionform.Model.Resources
import com.submissionform.utilities.Common
import kotlinx.android.synthetic.main.activity_new_resource.*

class NewResourceActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    var maxid:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_resource)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        setTitle("My Resources")

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

        btnSubmit.setOnClickListener {

            if(edtResourcesLink.text.equals("")){
                Common.sharedInsance.showDialog(this@NewResourceActivity,"Enter resource link")

            }else if(Patterns.WEB_URL.matcher(edtResourcesLink.text).matches()){



                var resource  = Resources(Common.sharedInsance.getListPreference(this@NewResourceActivity,"userid"),edtResourcesLink.text.toString(),edtResourcesLinkDescription.text.toString())
                database.child("resources").child(maxid.toString()).setValue(resource)
                Common.sharedInsance.showDialog(this@NewResourceActivity,"Saved")
                edtResourcesLink.setText("")
                edtResourcesLinkDescription.setText("")
            }else{
                edtResourcesLink.hideKeyboard()
                edtResourcesLinkDescription.hideKeyboard()
                Common.sharedInsance.showDialog(this@NewResourceActivity,"Enter valid resource link")

            }
        }
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
