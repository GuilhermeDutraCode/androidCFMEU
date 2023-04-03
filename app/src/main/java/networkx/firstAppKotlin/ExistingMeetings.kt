package networkx.firstAppKotlin


import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity



class ExistingMeetings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_existing_meetings)//main
        val documentsDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val arrayAdapter: ArrayAdapter<*>
        val listView = findViewById<ListView>(R.id.listViewFile)



        arrayAdapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1,
            Utilities.getAllFiles(documentsDirectory.toString())
        )

        listView.adapter = arrayAdapter;



    }


}
