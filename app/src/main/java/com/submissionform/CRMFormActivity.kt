package com.submissionform

import androidx.room.Room
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_crmform.*
import android.content.Intent

import android.view.MenuItem
import com.submissionform.Model.InputDataModel

import android.os.AsyncTask
import com.submissionform.Room.AppDatabase
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import android.util.Patterns
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.R.attr.country
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import java.text.SimpleDateFormat
import java.util.*
import android.widget.AdapterView.OnItemSelectedListener
import com.google.firebase.database.*
import com.submissionform.Model.Lead
import com.submissionform.utilities.Common


class CRMFormActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            if (position == 0) {
                applicationStatus = "Yes"
            } else if (position == 1) {
                applicationStatus = "No"
            } else {
                applicationStatus = "In Progress"
            }
    }
    var maxid:Long = 0
    private lateinit var database: DatabaseReference
    var applicationStatus = "Yes"
    var referralSourceValue = "Realtor Referral"
    var status = arrayOf("Yes","No","In Progress")
    var referralSource = arrayOf("Realtor Referral","Website","Open house","Zillow","Other")
    companion object{
       lateinit var form:Lead;
    }
    var update = false;
    var selectedSave = "Document"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crmform)
        setTitle("New Lead")
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
        spinner.onItemSelectedListener = this;
        spinnerReferral.onItemSelectedListener = object : OnItemSelectedListener {

            override fun onItemSelected(
                adapterView: AdapterView<*>, view: View,
                position: Int, id: Long
            ) {
                if(position == 0){
                    referralSourceValue = "Realtor Referral"
                    edtReferralSource.isEnabled = false;
                    edtReferralSource.visibility = View.INVISIBLE
                }else if(position == 1){
                    referralSourceValue = "Website"
                    edtReferralSource.isEnabled = false;
                    edtReferralSource.visibility = View.INVISIBLE
                }else if(position == 1){
                    referralSourceValue = "Open house"
                    edtReferralSource.isEnabled = false;
                    edtReferralSource.visibility = View.INVISIBLE
                }else if(position == 1){
                    referralSourceValue = "Zillow"
                    edtReferralSource.isEnabled = false;
                    edtReferralSource.visibility = View.INVISIBLE
                }else{
                    edtReferralSource.visibility = View.VISIBLE
                    referralSourceValue = "Other"
                    edtReferralSource.isEnabled = true;
                }

            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {


            }
        }

        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, status)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

        val referral = ArrayAdapter(this, android.R.layout.simple_spinner_item, referralSource)
        referral.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReferral.adapter = referral

        if(intent.hasExtra("update")){
    update = true;
    btnSubmit.setText("Update")
    editEmail.setText(form.email)
            referralSourceValue = form.referralSource!!
            if(referralSourceValue == "Realtor Referral"){
                spinnerReferral.setSelection(0)

            }else if(referralSourceValue == "Website"){
                spinnerReferral.setSelection(1)
            }else if(referralSourceValue == "Open house"){
                spinnerReferral.setSelection(2)
            }else if(referralSourceValue == "Zillow"){
                spinnerReferral.setSelection(3)
            }else{
                spinnerReferral.setSelection(4)

            }
            applicationStatus = form.applicationComplete!!;

            if(applicationStatus.equals("Yes")){
                spinner.setSelection(0)

            }else if(applicationStatus.equals("No")){
                spinner.setSelection(1)

            }else{

                spinner.setSelection(2)
            }
            editTextName.setText(form.fullName)
            editPhone.setText(form.phone)
            editLoanNotes.setText(form.loanNotes)
            edtReferralSource.setText(form.referralSourceOther!!)
}

        radioSaving.setOnCheckedChangeListener { group, checkedId ->
            if(R.id.radioCRM ==  checkedId){
                selectedSave = "Document"
            }else if(R.id.radioEmail ==  checkedId){
                selectedSave = "Email"
            }else if(R.id.radioBoth ==  checkedId){
                selectedSave = "Both"
            }
        }
        btnSubmit.setOnClickListener {

            if (editEmail.text.toString() == "" || editTextName.text.toString() == "" || editPhone.text.toString() == "" || editLoanNotes.text.toString() == "" ) {
                showDialog("Enter all the values")
            } else if(!isValidEmail(editEmail.text.toString())) {
                showDialog("Enter valid email")

            }else if(referralSourceValue == "Other" && edtReferralSource.text.toString() == ""){

//                if(edtReferralSource.text.toString() == ""){
                    showDialog("Enter Referral Source")

//                }
            }else if(update){

//                AsyncTask.execute {
//                    // Insert Data
//                    val db = Room.databaseBuilder(
//                        applicationContext,
//                        AppDatabase::class.java, "submission"
//                    ).build()
                if (Common.sharedInsance.getListPreference(this, "userEnabled") == "" || Common.sharedInsance.getListPreference(this, "userEnabled") == "false") {
                    Common.sharedInsance.showDialog(
                        this@CRMFormActivity,
                        "You have been disabled, contact admin to enable."
                    )

                } else {
                    form.email = editEmail.text.toString()
                    form.fullName = editTextName.text.toString();
                    form.phone = editPhone.text.toString();
                    form.loanNotes = editLoanNotes.text.toString();
                    form.referralSource = referralSourceValue
                    var otherSource = false;

                    var otherSourceText = "";
                    if (referralSourceValue == "Other") {
                        otherSource = true
                        otherSourceText = edtReferralSource.text.toString()
//
                    } else {
                        otherSource = false
                        otherSourceText = "";
//
                    }
//                    form.whetherReferralSourceOther = otherSource
                    form.referralSourceOther = otherSourceText
                    database.child("leads").child(form.leadsId).setValue(form)

//                    if(referralSourceValue == "Other"){
//                        form.whetherReferralSourceOther = true
//                        form.referralSourceOther = edtReferralSource.text.toString()
//
//                    }else{
//                        form.whetherReferralSourceOther = false
//                        form.referralSourceOther = ""
//
//                    }
//                    form.applicationStatus = applicationStatus;
//                    db.inputDao().update(form)
                    runOnUiThread {
                        editEmail.setText("")
                        editTextName.setText("")
                        editPhone.setText("")
                        editLoanNotes.setText("")
                        edtReferralSource.setText("")
                        spinner.setSelection(0)
                        spinnerReferral.setSelection(0)
                    }


//                }

//                Toast.makeText(this,"Form Updated Locally",Toast.LENGTH_LONG).show()
                    Common.sharedInsance.showDialog(this@CRMFormActivity, "Lead/Referral Updated")
                }
            }else {
//                var date = Date();

                if (Common.sharedInsance.getListPreference(this, "userEnabled") == "" || Common.sharedInsance.getListPreference(this, "userEnabled") == "false") {
                    Common.sharedInsance.showDialog(
                        this@CRMFormActivity,
                        "You have been disabled, contact admin to enable."
                    )

                } else{
                val pattern = "EEE dd, MMM yyyy hh:mm:ss "
                val simpleDateFormat = SimpleDateFormat(pattern)
                val date = simpleDateFormat.format(Date())
                var otherSource = false;
                var otherSourceText = "";
                if (referralSourceValue == "Other") {
                    otherSource = true
                    otherSourceText = edtReferralSource.text.toString()
//
                } else {
                    otherSource = false
                    otherSourceText = "";
//
                }
                val lead = Lead(
                    editTextName.text.toString(),
                    editEmail.text.toString(),
                    editPhone.text.toString(),
                    editLoanNotes.text.toString(),
                    applicationStatus,
                    referralSourceValue,
                    Common.sharedInsance.getListPreference(this@CRMFormActivity, "userid"),
                    date,
                    maxid.toString(),
                    otherSourceText

                )

                database.child("leads").child(maxid.toString()).setValue(lead)

//                AsyncTask.execute {
                    // Insert Data
//                    val db = Room.databaseBuilder(
//                        applicationContext,
//                        AppDatabase::class.java, "submission"
//                    ).build()
//                    var input = InputDataModel()
//                    input.email = editEmail.text.toString()
//                    input.fullName = editTextName.text.toString();
//                    input.phone = editPhone.text.toString();
//                    input.loanNotes = editLoanNotes.text.toString();
//                    input.referralSource = referralSourceValue
//                    if(referralSourceValue == "Other"){
//                        input.whetherReferralSourceOther = true
//                        input.referralSourceOther = edtReferralSource.text.toString()
//
//                    }else{
//                        input.whetherReferralSourceOther = false
//                        input.referralSourceOther = ""
//
//                    }
//                    input.applicationStatus = applicationStatus
//                    input.leadCreated = date
//                    db.inputDao().insertAll(input)
                editEmail.hideKeyboard()
                editTextName.hideKeyboard()
                editPhone.hideKeyboard()
                editLoanNotes.hideKeyboard()
                edtReferralSource.setText("")
                    runOnUiThread {
                        editEmail.setText("")
                        editTextName.setText("")
                        editPhone.setText("")
                        editLoanNotes.setText("")
                        edtReferralSource.setText("")
                        spinner.setSelection(0)
                        spinnerReferral.setSelection(0)
                    }

//                }
                Common.sharedInsance.showDialog(this@CRMFormActivity,"Lead/Referral Saved")
                if (selectedSave == "Email" || selectedSave == "Both") {
                    var body = editTextName.text.toString() + "\n"
                    body += editEmail.text.toString() + "\n"
                    body += editPhone.text.toString() + "\n"
                    body += editLoanNotes.text.toString() + "\n"
                    body += edtReferralSource.text.toString()
                    val emailIntent = Intent(android.content.Intent.ACTION_SEND)
                    emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    emailIntent.type = "vnd.android.cursor.item/email" //            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, arrayOf("abc@xyz.com"))
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Mortage data")
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body)
                    startActivity(Intent.createChooser(emailIntent, "Send mail using..."))
                }
            }
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


    fun showDialog(message:String){
        AlertDialog.Builder(this)
//            .setTitle("Delete entry")
            .setMessage(message)

            // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, which ->
                // Continue with delete operation
            })

            // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()

    }
    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}
