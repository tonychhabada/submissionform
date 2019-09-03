package com.submissionform

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_dashboard.*
import android.provider.MediaStore
import android.content.DialogInterface
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.File


class DashboardActivity : AppCompatActivity() {
    protected val CAMERA_REQUEST = 0
    protected val GALLERY_PICTURE = 1
    private val pictureActionIntent: Intent? = null
    var bitmap: Bitmap? = null
    private lateinit var database: DatabaseReference
    var selectedImagePath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        FirebaseApp.initializeApp(this)
// ...
        database = FirebaseDatabase.getInstance().reference
//
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
            var intent = Intent(this,MyResourcesCenter::class.java)
            startActivity(intent)

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
}
