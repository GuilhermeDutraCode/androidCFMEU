package networkx.firstAppKotlin;
//package networx.deligationapp;
import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scan extends AppCompatActivity {
    int count = 0;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1001; // or any other positive integer value
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private File selectedFile;


    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writingTagFilters[];
    boolean writeMode;
    Tag myTag;
    Context context;
    TextView nfc_contents;
    TextView status;

    TextView date;

    TextView name;
    List<String> tags;

    private TextView countTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        nfc_contents = (TextView) findViewById(R.id.infotag);
        status = (TextView) findViewById(R.id.status);

        context = this;
        countTextView = findViewById(R.id.countTextView);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "this deveice doesnt supoort nfc", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "this device doesn't support NFC");
            finish();
        }
        readFromIntent(getIntent());
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writingTagFilters = new IntentFilter[]{tagDetected};

        tags = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission if it is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {

            try {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        102);

               // File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "tags.txt");
                File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "tags.txt");
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String line;
                while ((line = br.readLine()) != null) {
                    tags.add(line);
                    //Toast.makeText(this, "tamo no tags", Toast.LENGTH_SHORT).show();
                    Log.d("Tags", line);
                }
                br.close();
            } catch (IOException e) {
                Log.e("Scan", "Failed to read tags from file", e);
                Toast.makeText(this, "Failed to read tags from file", Toast.LENGTH_SHORT).show();
                finish();
            }
        }


        displayMeetingInfo();


        ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
        );

    }

    private void displayMeetingInfo(){
        Intent intent = getIntent();
        String selectedFileName = intent.getStringExtra("selected_file");
        String[] parts = selectedFileName.split(" ");
        if (parts.length == 2) {
            String date = parts[1];
            String nameWithExtension = parts[0];
            String[] nameParts = nameWithExtension.split("\\.");
            String name = nameParts[0];

           //return date + "__" + name + ".xlsx";
            TextView dateTextView = (TextView) findViewById(R.id.date);
            TextView nameTextView = (TextView) findViewById(R.id.name);
            dateTextView.setText(date);
            nameTextView.setText(name);

        }
        selectedFileName =  Utilities.putFormattedName(selectedFileName);
        File documentsDirectory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File selectedFile = new File(documentsDirectory, "CFMEU_Meetings/" + selectedFileName);
        try ( InputStream inp = new FileInputStream(selectedFile)){
            Workbook wb = WorkbookFactory.create(inp);
            CreationHelper creationHelper = wb.getCreationHelper();
            Sheet sheet = wb.getSheet("Active");
            //int lastRow = -1; // get last row with content

            int lastRow = sheet.getLastRowNum(); // get last row with content
//            int tagCount = countTags(sheet);
            //createContent(sheet, creationHelper, lastRow);
            //createContent2(sheet, creationHelper, lastRow, tag);
//            try (OutputStream fileOut = new FileOutputStream( selectedFileName )) {
//                wb.write(fileOut);
//            }

            TextView countTextView = (TextView) findViewById(R.id.countTextView);
            countTextView.setText(String.valueOf(lastRow));

           // TextView countTextView = view.findViewById(R.id.countTextView);
            //countTextView.setText(String.valueOf(lastRow));

        } catch (Exception e){
            e.printStackTrace();
        }


        if( selectedFileName == null ){
            SharedPreferences sp=getSharedPreferences("key", Context.MODE_PRIVATE);
            selectedFileName = sp.getString("selected_file","");
        }


    }


    private void extracted(String tag, String wSheet) {

        Intent intent = getIntent();
        String selectedFileName = intent.getStringExtra("selected_file");

        if( selectedFileName == null ){
            SharedPreferences sp=getSharedPreferences("key", Context.MODE_PRIVATE);
            selectedFileName = sp.getString("selected_file","");
            selectedFileName = Utilities.putFormattedName(selectedFileName);
        }

        File documentsDirectory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File selectedFile = new File(documentsDirectory, "CFMEU_Meetings/" + selectedFileName);

        TextView countTextView = findViewById(R.id.countTextView);
        TextView alreadyInTextView = findViewById(R.id.alreadyIn);
        alreadyInTextView.setText("");


        Utilities.activeUserIntoExcel(selectedFile, tag, wSheet, countTextView, alreadyInTextView);
    }

       private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            byte[] tag = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            StringBuilder stb = new StringBuilder();

            List<String> list = new ArrayList<>();
            for (int i = 0; i < tag.length; i++) {
                list.add(Integer.toHexString(((int) tag[i] & 0xff)));
            }
            Collections.reverse(list);
            list.forEach(va -> {
                stb.append(va.toUpperCase());
            });

            String tagId = stb.toString(); // Define tagId variable with the tag ID

            nfc_contents = (TextView) findViewById(R.id.infotag);
            nfc_contents.setText(tagId);

            if (tags.contains(tagId)) {
                status.setText("Active");
                extracted(tagId, "Active");
            } else {
                String inactiveText = "Inactive";
                SpannableString spannableString = new SpannableString(inactiveText);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.RED);
                spannableString.setSpan(colorSpan, 0, inactiveText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                status.setText(spannableString);
                extracted(tagId, "Inactive");
            }
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        readFromIntent(getIntent());
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent().getAction())) {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        WriteModeOff();
    }

    @Override
    public void onResume() {
        super.onResume();
        writeModeOn();
    }

    private void writeModeOn() {
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writingTagFilters, null);
    }

    private void WriteModeOff() {
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }



}





