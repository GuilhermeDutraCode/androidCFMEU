package networkx.firstAppKotlin

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.OutputStream
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private val REQUEST_READ_EXTERNAL_STORAGE = 1
    private val REQUEST_WRITE_EXTERNAL_STORAGE = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val goToNewMeeting = findViewById<Button>(R.id.new_meeting)
        val goToExistingMeetings = findViewById<Button>(R.id.button2)
        val goToCheckCard = findViewById<Button>(R.id.button4)
        val goToScan = findViewById<Button>(R.id.button5)

        goToNewMeeting.setOnClickListener{
            val intent = Intent(this, NewMeeting::class.java)
            startActivity(intent)
        }

        goToExistingMeetings.setOnClickListener{
            val intent = Intent(this, ExistingMeetings::class.java)
            startActivity(intent)
        }

        goToCheckCard.setOnClickListener{
            try{
                val intent = Intent(this, Nfc::class.java )
                startActivity(intent)
            }catch (e:Exception){
                e.printStackTrace();
            }

        }
        goToScan.setOnClickListener{
            val intent = Intent(this, Scan::class.java)
            startActivity(intent)
        }

            //Check if App has READ_EXTERNAL Storage permission:
           if(ContextCompat.checkSelfPermission(this,  android.Manifest.permission.READ_EXTERNAL_STORAGE)
           != PackageManager.PERMISSION_GRANTED){
               ActivityCompat.requestPermissions(this,
               arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                   REQUEST_READ_EXTERNAL_STORAGE)
           }



            createFolderInDocuments()
            createMembersList(this)

    }

    private fun createFolderInDocuments(){
        val documentsDirectory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString())
        val folderName = "CFMEU_Meetings"
        val folderPath = "$documentsDirectory/$folderName"

        val folder = File(folderPath)
        if (!folder.exists()){
            val created = folder.mkdir()
            if(created) {
                println("Folder created successfully.")
            } else {
                println("Failed to create folder")
            }
        } else {
            println("Folder already exists")
        }
    }

    private fun createMembersList(context: Context){
        val fileName = "tags.txt"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

        val manager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse("https://vic.cfmeu.org/sites/vic.cfmeu.org/files/tags.txt")
        val request = DownloadManager.Request(uri)
        request.setVisibleInDownloadsUi(true)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)

        if (file.exists()) {
            file.delete()
        }

        try {
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, fileName)
            val reference = manager.enqueue(request)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE || requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


}

