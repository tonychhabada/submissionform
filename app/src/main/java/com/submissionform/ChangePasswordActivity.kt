package com.submissionform

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.firebase.database.*
import com.submissionform.Model.Documents
import com.submissionform.Model.User
import com.submissionform.adapter.MyDocumentsAdapter
import com.submissionform.utilities.Common
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_my_documents.*

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        setTitle("Change Password")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance().reference

        btnChangePassword.setOnClickListener {
            if(editCurrentPassword.text.toString() == "" || editNewPassword.text.toString() == "" || editConfirmPassword.text.toString() == ""){
                Common.sharedInsance.showDialog(this@ChangePasswordActivity,"All fields are required")
            }else if(editNewPassword.text.toString() != editConfirmPassword.text.toString()){
                Common.sharedInsance.showDialog(this@ChangePasswordActivity,"Passwords do not matched")
            }else {
                val query = database.child("users").orderByChild("userid")
                    .equalTo(Common.sharedInsance.getListPreference(this, "userid"))
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            var cd = dataSnapshot.children
                            for (data in dataSnapshot.children) {
                                var password = data.child("password").value.toString()
                                if (editCurrentPassword.text.toString() != password) {
                                    Common.sharedInsance.showDialog(
                                        this@ChangePasswordActivity,
                                        "Invalid Password"
                                    )
                                }else{
                                    var user = User(
                                        data.child("username").value.toString(),
                                       editNewPassword.text.toString(),
                                        data.child("name").value.toString(),
                                        data.child("userEnabled").value as String,
                                        Common.sharedInsance.getListPreference(this@ChangePasswordActivity, "userid")
                                    )
                                    editCurrentPassword.setText("")
                                    editConfirmPassword.setText("")
                                    editNewPassword.setText("")
                                    editCurrentPassword.hideKeyboard()
                                    editConfirmPassword.hideKeyboard()
                                    editNewPassword.hideKeyboard()
                                    database.child("users").child(Common.sharedInsance.getListPreference(this@ChangePasswordActivity, "userid")).setValue(user)
                                    Common.sharedInsance.showDialog(
                                        this@ChangePasswordActivity,
                                        "Password Changed Successfully"
                                    )
                                }


                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                })
            }
        }

    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
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
}
