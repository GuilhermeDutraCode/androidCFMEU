package networkx.firstAppKotlin

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class ExistingMeetings : AppCompatActivity() {

    private lateinit var meetingsRecyclerView: RecyclerView
    private lateinit var adapter: MeetingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_existing_meetings)

        val documentsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val folderName = "/CFMEU_Meetings/"
        val folder = File(documentsDirectory, folderName)
        Log.d("Path", "Directory path: ${folder.getAbsolutePath()}")

        meetingsRecyclerView = findViewById(R.id.meetingsRecyclerView)
        meetingsRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MeetingsAdapter(emptyArray())
        meetingsRecyclerView.adapter = adapter

        listAllMeetings(folder)
//        listAllMeetings(documentsDirectory)

        if (!folder.exists()) {
            Log.d("Log", "Directory does not exist")
            return
        } else (Log.d ("Log", "Directory exists"))

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Permission has already been granted
            listAllMeetings(folder)
        } else {
            // Request permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            Log.d("A", "App doesn't have enough permission")
        }


    }

    private class MeetingsAdapter(private var fileList: Array<File>?) :
        RecyclerView.Adapter<MeetingViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.meeting_item_layout, parent, false)
            return MeetingViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {
            val file = fileList!![position]
            holder.fileNameTextView.text = file.name
        }

        override fun getItemCount(): Int = fileList?.size ?: 0

        fun updateList(newList: Array<File>) {
            fileList = newList
            notifyDataSetChanged()
        }
    }

    private fun listAllMeetings(directory: File) {
        if (!directory.exists()) {
            Log.d("Log", "Directory does not exist: ${directory.path}")
            return
        }
        val files = directory.listFiles()
        Log.d("Log", "Files found: ${files?.size}")
        if (files != null) {
            adapter.updateList(files)
        }
    }

    private class MeetingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fileNameTextView: TextView = itemView.findViewById(R.id.fileNameTextView)
    }
}
