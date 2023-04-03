package networkx.firstAppKotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import java.io.File

class MainActivity : AppCompatActivity() {
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
            val intent = Intent(this, scan::class.java)
            startActivity(intent)
        }


            createFolderInDocuments()


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
}