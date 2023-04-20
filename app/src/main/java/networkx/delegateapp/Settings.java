package networkx.firstAppKotlin;

import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    TextView sPath;
    //nfc_contents = (TextView) findViewById(R.id.infotag);

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        TextView sPath = findViewById(R.id.sPath);
        Utilities.storageLocation(sPath, this);

        TextView dPath = findViewById(R.id.dPath);
        Utilities.deletedMeetingsPath(dPath, this);

        TextView tPath = findViewById(R.id.tPath);
        Utilities.tagsLocation(tPath, this);


    }}
