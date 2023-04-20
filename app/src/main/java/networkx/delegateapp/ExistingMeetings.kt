package networkx.firstAppKotlin


import android.app.AlertDialog
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File


class ExistingMeetings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_existing_meetings)
        val documentsDirectory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val arrayAdapter: ArrayAdapter<*>
        val listView = findViewById<ListView>(R.id.listViewFile)

        arrayAdapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1,
            Utilities.getAllFiles(documentsDirectory.toString())
        )

        listView.adapter = arrayAdapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val fileToDelete = File(
                (documentsDirectory?.absolutePath ?: null) + "/CFMEU_Meetings",
                Utilities.putFormattedName(arrayAdapter.getItem(position)!!)
            )
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete Meeting")
            builder.setMessage("Are you sure you want to delete this meeting?")
            builder.setPositiveButton("Yes") { _, _ ->
                val deletedMeetingsDir = File(documentsDirectory, "Deleted_Meetings")
                if (!deletedMeetingsDir.exists()) {
                    deletedMeetingsDir.mkdir()
                }
                val newFile = File(deletedMeetingsDir, fileToDelete.name)
                if (fileToDelete.renameTo(newFile)) {
                    Toast.makeText(
                        applicationContext,
                        "Meeting moved to Deleted Meetings folder",
                        Toast.LENGTH_SHORT
                    ).show()
                    arrayAdapter.remove(arrayAdapter.getItem(position))
                    arrayAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Unable to delete meeting",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            builder.setNegativeButton("No") { _, _ -> }
            builder.show()
        }
    }
}