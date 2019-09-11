package com.submissionform

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import com.submissionform.Model.User
import com.submissionform.utilities.Common
import kotlinx.android.synthetic.main.activity_login.editPassword
import kotlinx.android.synthetic.main.activity_register.*


class LoginActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var id = Common.sharedInsance.getListPreference(this, "userid")
        if (Common.sharedInsance.getListPreference(this, "userid") == "") {
            setContentView(R.layout.activity_login)

        database = FirebaseDatabase.getInstance().reference
        val reference = FirebaseDatabase.getInstance().reference
//        val ti = object : GenericTypeIndicator<ArrayList<HashMap<String?, String?>>>() {}


//        var orders_Reference = database.child("users");
//        orders_Reference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (data in dataSnapshot.children) {
//                    if (data.key == "username") {
//                        val orderNumber = data.value!!.toString()
//                        Log.d("Specific Node Value", orderNumber)
//                    }
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {}
//        })
        btnLogin.setOnClickListener {
            //
            if (editUserName.text.toString() == "" || editPassword.text.toString() == "") {
                Common.sharedInsance.showDialog(this@LoginActivity, "Enter All Values")

            } else {
                val query = reference.child("users")
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        var exists = false;
                        if (dataSnapshot.exists()) {
                            // dataSnapshot is the "issue" node with all children with id 0
                            for (data in dataSnapshot.children) {
                                val password = data.child("password").value
                                val username = data.child("username").value
                                if (username == editUserName.text.toString() && password == editPassword.text.toString()) {
                                    Common.sharedInsance.saveStringPreference(
                                        data.key,
                                        this@LoginActivity,
                                        "userid"
                                    )
                                    exists = true;
                                    var intent =
                                        Intent(this@LoginActivity, DashboardActivity::class.java)
                                    startActivity(intent)
                                } else {
                                exists = false;

                                }

                            }
                        }
                        if(!exists){

                            Common.sharedInsance.showDialog(
                                this@LoginActivity,
                                "Invalid credentials"
                            )
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
            }
        }

        btnSignUp.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }else{

            var intent =
                Intent(this@LoginActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
