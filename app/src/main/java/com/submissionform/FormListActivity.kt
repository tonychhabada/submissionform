package com.submissionform

import androidx.room.Room
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import com.submissionform.Model.InputDataModel
import com.submissionform.Room.AppDatabase
import com.submissionform.adapter.FormsAdapter
import kotlinx.android.synthetic.main.activity_crmform.*
import kotlinx.android.synthetic.main.activity_form_list.*

import android.view.ContextMenu.ContextMenuInfo
import android.widget.Toast


class FormListActivity : AppCompatActivity() {
//    lateinit var List<InputDataModel> obj;
private lateinit var  forms: List<InputDataModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_list)
        setTitle("Leads List")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar()?.setM



        formListView.setOnItemClickListener { parent, view, position, id ->
            var intent = Intent(this,CRMFormActivity::class.java)
            intent.putExtra("update",true)
            CRMFormActivity.form = forms.get(position)
//            intent.putExtra("updateObject",forms.get(position))
            startActivity(intent)



        }

    }

    override fun onResume() {
        super.onResume()
        AsyncTask.execute {
            // Insert Data
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "submission"
            ).build()

            forms = db.inputDao().all
            runOnUiThread {

                formListView.adapter = FormsAdapter(this,forms)
            }

        }
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
