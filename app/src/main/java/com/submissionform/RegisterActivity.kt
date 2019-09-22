package com.submissionform

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import com.submissionform.Model.User
import com.submissionform.utilities.Common
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    var maxid:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
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
        btnRegister.setOnClickListener {
            Toast.makeText(this@RegisterActivity,""+checkBox.isChecked,Toast.LENGTH_LONG).show()
            if(editUsername.text.toString() == "" || editPassword.text.toString() == "" || editName.text.toString() == ""){
                Common.sharedInsance.showDialog(this@RegisterActivity,"Enter all the values")

            }else if(!checkBox.isChecked){
                Common.sharedInsance.showDialog(this@RegisterActivity,"Accept the term")

            }else {
                var user = User(
                    editUsername.text.toString(),
                    editPassword.text.toString(),
                    editName.text.toString(),
                    "true",
                    maxid.toString()

                )
                database.child("users").child(maxid.toString()).setValue(user)
                showDialog()
            }

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
    fun showDialog(){
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage("User registered Successfully")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Ok", DialogInterface.OnClickListener {
                    dialog, id ->
                var intent = Intent(this@RegisterActivity,LoginActivity::class.java)
                startActivity(intent)
                finish()
            })
            // negative button text and action
//            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
//                    dialog, id -> dialog.cancel()
//            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("")
        // show alert dialog
        alert.show()

    }
}
