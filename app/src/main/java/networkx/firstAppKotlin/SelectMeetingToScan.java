package networkx.firstAppKotlin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class SelectMeetingToScan extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_existing_meetings);

        File documentsDirectory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                Utilities.getAllFiles(documentsDirectory.toString())
        );

        ListView listView = findViewById(R.id.listViewFile);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFile = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(SelectMeetingToScan.this, Scan.class);
                intent.putExtra("selected_file", selectedFile);
                startActivity(intent);
            }
        });    }
}

