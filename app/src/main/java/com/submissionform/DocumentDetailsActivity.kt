package com.submissionform

import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.submissionform.Model.MyDocuments
import com.submissionform.utilities.Common
import kotlinx.android.synthetic.main.activity_document_details.*
import java.io.File

class DocumentDetailsActivity : AppCompatActivity() {
    protected val CAMERA_REQUEST = 0
    protected val GALLERY_PICTURE = 1
    lateinit var document:MyDocuments
    private lateinit var database: DatabaseReference
    var id = "";
    var filePath: Uri? = null
    var storage = FirebaseStorage.getInstance();
    var storageRef = storage.getReferenceFromUrl("gs://quickstart-1563772816825.appspot.com")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_details)
        setTitle("Details")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        database = FirebaseDatabase.getInstance().reference
        var image = intent.getStringExtra("imageUrl")
        id = intent.getStringExtra("id");
        Picasso.get().load(image).into(imageView)

        document = MyDocuments(id,image,Common.sharedInsance.getListPreference(this,"userid"))
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item?.itemId == R.id.save){
            startDialog()
        }else {
            onBackPressed()
            return true

        }
        return super.onOptionsItemSelected(item)
    }
    private fun startDialog() {
        val myAlertDialog = AlertDialog.Builder(
            this
        )
        myAlertDialog.setTitle("Upload Pictures Option")
        myAlertDialog.setMessage("How do you want to select your picture?")

        myAlertDialog.setPositiveButton("Gallery",
            DialogInterface.OnClickListener { arg0, arg1 ->
                var pictureActionIntent: Intent? = null

                pictureActionIntent = Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(
                    pictureActionIntent,
                    GALLERY_PICTURE
                )
            })

        myAlertDialog.setNegativeButton("Camera",
            DialogInterface.OnClickListener { arg0, arg1 ->
                val intent = Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE
                )
                val f = File(
                    Environment
                        .getExternalStorageDirectory(), "temp.jpg"
                )
                intent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(f)
                )

                startActivityForResult(
                    intent,
                    CAMERA_REQUEST
                )
            })
        myAlertDialog.show()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.document_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_PICTURE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data

            try {
                //getting image from gallery
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                val tsLong = System.currentTimeMillis() / 1000

                val childRef = storageRef.child(tsLong.toString()+".jpg")

                //uploading the image
                val uploadTask = childRef.putFile(filePath!!)

                uploadTask.addOnSuccessListener {
                    //                    pd.dismiss()
                    it.storage.downloadUrl.addOnCompleteListener {
                        Picasso.get().load(it.result.toString()).into(imageView)
                        document.documentUrl = it.result.toString()
                        database.child("documents").child(document.id).setValue(
                            document
                        )
//                        var mydocuments = MyDocuments(maxid.toString(),it.result.toString(),
//                            Common.sharedInsance.getListPreference(this@DashboardActivity,"userid"))
//                        database.child("documents").child(maxid.toString()).setValue(mydocuments)
//                        Common.sharedInsance.showDialog(this@DashboardActivity,"Document uploaded")
                    }

                }.addOnFailureListener { e ->

                    var ed = "Upload Failed -> $e"
                    Toast.makeText(this@DocumentDetailsActivity, "Upload Failed -> $e", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch(e: Exception) {
                e.printStackTrace()
            }

        }
    }
}
