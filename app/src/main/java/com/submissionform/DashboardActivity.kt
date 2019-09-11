package com.submissionform

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_dashboard.*
import android.provider.MediaStore
import android.content.DialogInterface
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.submissionform.utilities.Common
import java.io.File
import android.app.ProgressDialog
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.storage.UploadTask
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.StorageReference
import com.kaopiz.kprogresshud.KProgressHUD
import com.submissionform.Model.MyDocuments


class DashboardActivity : AppCompatActivity() {
    var maxid:Long = 0
    protected val CAMERA_REQUEST = 0
    protected val GALLERY_PICTURE = 1
    private val pictureActionIntent: Intent? = null
    var bitmap: Bitmap? = null
    private lateinit var database: DatabaseReference
    var selectedImagePath: String? = null

    var chooseImg: Button? = null
    var imgView: ImageView? = null
    var PICK_IMAGE_REQUEST = 111
    var filePath: Uri? = null
    lateinit var progressHUD: KProgressHUD
    var storage = FirebaseStorage.getInstance();
    var storageRef = storage.getReferenceFromUrl("gs://quickstart-1563772816825.appspot.com")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        progressHUD = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setDetailsLabel("Uploading..")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)


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
        crmLayout.setOnClickListener {
            var intent = Intent(this,FormListActivity::class.java)
            startActivity(intent)
        }

        notesLayout.setOnClickListener {
            var intent = Intent(this,NotesActivity::class.java)
            startActivity(intent)

        }


        myDocumentsLayout.setOnClickListener {
            var intent = Intent(this,MyDocumentsActivity::class.java)
            startActivity(intent)

        }

        btnProfileSettings.setOnClickListener {
            var intent = Intent(this,ProfileSettings::class.java)
            startActivity(intent)

        }
        myResourcesLayout.setOnClickListener {
//            var intent = Intent(this,MyResourcesCenter::class.java)
//            startActivity(intent)
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCZhm6Wo_GHOktZr2x5iCgWg/featured"))
            startActivity(browserIntent)
        }
        btnCamera.setOnClickListener {
            startDialog()
        }
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

    override fun onResume() {
        super.onResume()
        val reference = FirebaseDatabase.getInstance().reference
        val query = reference.child("users").orderByChild("userid").equalTo(Common.sharedInsance.getListPreference(this@DashboardActivity,"userid"))

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (data in dataSnapshot.children) {
                        Common.sharedInsance.saveBooleanPreference(data.child("userEnabled").value as Boolean,this@DashboardActivity,"userEnabled")

                    }


                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
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
                progressHUD.show()
                //uploading the image
                val uploadTask = childRef.putFile(filePath!!)

                uploadTask.addOnSuccessListener {
//                    pd.dismiss()
                     it.storage.downloadUrl.addOnCompleteListener {
//                        var re = it.result
                         var mydocuments = MyDocuments(maxid.toString(),it.result.toString(),Common.sharedInsance.getListPreference(this@DashboardActivity,"userid"))
                         database.child("documents").child(maxid.toString()).setValue(mydocuments)
                         Common.sharedInsance.showDialog(this@DashboardActivity,"Document uploaded")
                         progressHUD.dismiss()
                    }

                }.addOnFailureListener { e ->

                    var ed = "Upload Failed -> $e"
                    Toast.makeText(this@DashboardActivity, "Upload Failed -> $e", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch(e: Exception) {
                e.printStackTrace()
            }

        }
    }
}
