package networkx.firstAppKotlin;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Nfc extends AppCompatActivity {

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writingTagFilters[];
    boolean writeMode;
    Tag myTag;
    Context context;
    TextView nfc_contents;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_check_card);
        nfc_contents = (TextView) findViewById(R.id.infotag);
        context = this;

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter == null) {
            Toast.makeText(this, "this deveice doesnt supoort nfc", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "this device doesn't support NFC");
            finish();
        }
        readFromIntent(getIntent());
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writingTagFilters = new IntentFilter[] { tagDetected };
        }

        private void readFromIntent(Intent intent){
            String action = intent.getAction();
            if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                ||NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                ||NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
                byte[] tag = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                StringBuilder stb = new StringBuilder();

                List<String> list = new ArrayList<>();
                for(int i =0; i < tag.length; i++ ){
                    list.add(Integer.toHexString(((int) tag[i] & 0xff)));
                }
                Collections.reverse(list);
                list.forEach(va ->{
                    stb.append(va);
                });

//                Toast.makeText(this, "skja---->>>>>" +  stb.toString() , Toast.LENGTH_SHORT).show();
                nfc_contents = (TextView) findViewById(R.id.infotag);
                nfc_contents.setText( stb.toString() );
            }

//            NfcTest test = new NfcTest();
//            test.readFromIntent(intent);
         }
        private void buildTagViews(NdefMessage[] msgs){
            if (msgs == null || msgs.length == 0) return;

            String text = "";
            // String tagId = new String(msgs[0].getRecords()[0].getType());
            byte [] payLoad = msgs[0].getRecords()[0].getPayload();
            String textEnconding = ((payLoad[0] & 128) == 0 ) ? "UTF-8" : "UTF-16";
            int languageCodeLength = payLoad[0] & 0063; //Get The Language code, e.g 'En'
            //String languageCode = new String(payLoad, 1, languageCodeLength, "US-ASCII");

            try {
                //get the text
                text = new String(payLoad, languageCodeLength + 1, payLoad.length - languageCodeLength -1, textEnconding);
            } catch (UnsupportedEncodingException e){
                Log.e("Unsuported Enconding of message", e.toString());
            }
            nfc_contents.setText("NFC content" + text);
        }

        @Override
        protected void onNewIntent(Intent intent){
            super.onNewIntent(intent);
            setIntent(intent);
            readFromIntent(getIntent());
            if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent().getAction())){
//                    Toast.makeText(this, "aaaaaa", Toast.LENGTH_SHORT).show();
//                myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            }
        }

        @Override
        public void onPause() {
            super.onPause();
            WriteModeOff();
    }
        @Override public void onResume() {
            super.onResume();
            writeModeOn();
        }

        private void writeModeOn(){
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writingTagFilters, null);
        }

        private void WriteModeOff(){
            writeMode = false;
            nfcAdapter.disableForegroundDispatch(this);
        }


}


