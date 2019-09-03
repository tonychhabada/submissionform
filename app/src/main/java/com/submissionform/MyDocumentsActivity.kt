package com.submissionform

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.submissionform.Model.Documents
import com.submissionform.adapter.MyDocumentsAdapter
import kotlinx.android.synthetic.main.activity_my_documents.*

class MyDocumentsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_documents)
        setTitle("")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

        val document: ArrayList<Documents> = ArrayList()

        document.add(Documents("","Notes","testt"))
        document.add(Documents("","Notes","testt"))
        document.add(Documents("","Notes","testt"))
        listViewDocuments.adapter = MyDocumentsAdapter(this,document);
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
