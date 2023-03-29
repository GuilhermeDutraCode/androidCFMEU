package networkx.firstAppKotlin

import CheckCard
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

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
            val intent = Intent(this, New_Meeting::class.java)
            startActivity(intent)
        }

        goToExistingMeetings.setOnClickListener{
            val intent = Intent(this, Existing_Meetings::class.java)
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
    }
}