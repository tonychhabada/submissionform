package com.submissionform

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import com.submissionform.Model.User


class LoginActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        database = FirebaseDatabase.getInstance().reference
        val reference = FirebaseDatabase.getInstance().reference
        val ti = object : GenericTypeIndicator<ArrayList<HashMap<String?, String?>?>>() {}

        val query = reference.child("users")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (data in dataSnapshot.children) {
                        val users: ArrayList<HashMap<String?, String?>?> = dataSnapshot.getValue(ti)
//                        val password = users!!.get("password")
                        if (data.key == "username") {
                        val orderNumber = data.value!!.toString()
                        Log.d("Specific Node Value", orderNumber)
                    }
                }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
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
//                var intent = Intent(this,DashboardActivity::class.java)
//            startActivity(intent)

        }

        btnSignUp.setOnClickListener {
            var intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
