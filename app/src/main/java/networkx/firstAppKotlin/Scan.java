package networkx.firstAppKotlin;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    List<String> tags;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        nfc_contents = (TextView) findViewById(R.id.infotag);
        status = (TextView) findViewById(R.id.status);

        context = this;

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
                    Toast.makeText(this, "tamo no tags", Toast.LENGTH_SHORT).show();
                    Log.d("Tags", line);
                }
                br.close();
            } catch (IOException e) {
                Log.e("Scan", "Failed to read tags from file", e);
                Toast.makeText(this, "Failed to read tags from file", Toast.LENGTH_SHORT).show();
                finish();
            }
        }


        extracted();


        // Request permission to read and write to external storage
        ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
        );



    }

    private void extracted() {
        count = count ++;
        Log.i("COUNT", "-------->" + count);
        //Code to check if we are getting the selected file and are able to edit it.
        // Retrieve the selected file from the intent
        Intent intent = getIntent();
        String selectedFileName = intent.getStringExtra("selected_file");
        File documentsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File selectedFile = new File(documentsDirectory, "CFMEU_Meetings/" + selectedFileName);
        //File file = new File(getExternalFilesDir(null), "Documents/");

        try {
            OPCPackage opcPackage = OPCPackage.open( selectedFile );
            XSSFWorkbook wb = new XSSFWorkbook( opcPackage );

            Sheet sheet = wb.createSheet();

            Row row = sheet.createRow(2);
            row.setHeightInPoints(30);

            opcPackage.close();
        } catch ( Exception e) {
          e.printStackTrace();
        }
        //Utilities.getFileXlsx(file);

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
                stb.append(va);
            });

            String tagId = stb.toString(); // Define tagId variable with the tag ID


            nfc_contents = (TextView) findViewById(R.id.infotag);
            nfc_contents.setText(tagId);

            if (tags.contains(tagId)) {
                status.setText("Active");
            } else {
                status.setText("Inactive");
            }
        }
    }

    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;

        String text = "";

        byte[] payLoad = msgs[0].getRecords()[0].getPayload();
        String textEnconding = ((payLoad[0] & 128) == 0) ? "UTF-8" : "UTF-16";
        int languageCodeLength = payLoad[0] & 0063; //Get The Language code, e.g 'En'

        try {
            //get the text
            text = new String(payLoad, languageCodeLength + 1, payLoad.length - languageCodeLength - 1, textEnconding);
        } catch (UnsupportedEncodingException e) {
            Log.e("Unsuported Enconding of message", e.toString());
        }
        nfc_contents.setText("NFC content" + text);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        readFromIntent(getIntent());
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent().getAction())) {
//
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


    private void addHelloWorldToExcel(File selectedFile) throws IOException {
        // Open the selected Excel file
        FileInputStream fis = new FileInputStream(selectedFile);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);

        // Get the first sheet of the workbook
        XSSFSheet sheet = workbook.getSheetAt(0);

        // Create a new row at the end of the sheet
        XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);

        // Create a new cell in the row and set its value to "Hello World"
        XSSFCell cell = row.createCell(0);
        cell.setCellValue("Hello World");

        // Save the changes to the Excel file
        FileOutputStream fos = new FileOutputStream(selectedFile);
        workbook.write(fos);

        // Close the input and output streams
        fis.close();
        fos.close();
    }

}





