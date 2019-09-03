package com.submissionform

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.*
import com.submissionform.Model.User
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    var maxid:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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
            var user = User(editUsername.text.toString(),editPassword.text.toString(),editName.text.toString())
            database.child("users").child(maxid.toString()).setValue(user)

        }
    }
}
