package networkx.firstAppKotlin


import android.os.Bundle
import android.os.Environment
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import networkx.firstAppKotlin.R.id.listViewFile


class ExistingMeetings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_existing_meetings)//main
        val documentsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val arrayAdapter: ArrayAdapter<*>
        val listView = findViewById<ListView>(R.id.listViewFile)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,
                Utilities.getAllFiles( documentsDirectory.toString() ))
        listView.adapter = arrayAdapter;




    //        val documentsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
//        val folderName = "CFMEU_Meetings"
//        val folder = File(documentsDirectory, folderName)

//        meetingsRecyclerView = findViewById(R.id.meetingsRecyclerView)
//        meetingsRecyclerView.layoutManager = LinearLayoutManager(this)
//
//        adapter = MeetingsAdapter( Utilities.getAllFiles( documentsDirectory.toString() ))
//
//        meetingsRecyclerView.adapter = adapter



//        if (!folder.exists()) {
//            Log.d("ExistingMeetings", "Directory does not exist")
//            return
//        } else (Log.d ("ExistingMeetings", "Directopry exists"))
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            // Permission has already been granted
//            //listAllMeetings(folder)
//        } else {
//            // Request permission
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
//            Log.d("A", "App doesn't have enough permission")
//        }


    }

//    private class MeetingsAdapter(private var fileList: List<File>?) :
//        RecyclerView.Adapter<MeetingViewHolder>() {
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {
//            val itemView = LayoutInflater.from(parent.context)
//                .inflate(R.layout.meeting_item_layout, parent, false)
//            return MeetingViewHolder(itemView)
//        }
//
//        override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {
//            val file = fileList!![position]
//            holder.fileNameTextView.text = file.name
//        }
//
//        override fun getItemCount(): Int = fileList?.size ?: 0
//
//        fun updateList(newList: List<File>) {
//            fileList = newList
//            notifyDataSetChanged()
//        }
//    }

//    private fun listAllMeetings(directory: File) {
//        if (!directory.exists()) {
//            Log.d("ExistingMeetings", "Directory does not exist")
//            return
//        }
//        val files = directory.listFiles()
//        Log.d("ExistingMeetings", "Files found: ${files?.size}")
//        if (files != null) {
//            adapter.updateList(files)
//        }
//    }



//    private class MeetingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val fileNameTextView: TextView = itemView.findViewById(R.id.fileNameTextView)
//    }



}
