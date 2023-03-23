package networkx.firstAppKotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    val fragmentManager = supportFragmentManager
    val goToNewMeeting = findViewById<Button>(R.id.new_meeting)
        goToNewMeeting.setOnClickListener{
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_new_meeting, NewMeeting())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}
