package networkx.firstAppKotlin;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcelable;

public class NfcTest implements NfcAdapter.ReaderCallback {

//    public void getNFCMessage(NfcAdapter nfcAdapter, Activity activity) {
//        if (nfcAdapter != null) {
//            Bundle options = new Bundle();
//            options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250);
//            nfcAdapter.enableReaderMode(activity,
//                    this,
//                    NfcAdapter.FLAG_READER_NFC_A |
//                            NfcAdapter.FLAG_READER_NFC_B |
//                            NfcAdapter.FLAG_READER_NFC_F |
//                            NfcAdapter.FLAG_READER_NFC_V |
//                            NfcAdapter.FLAG_READER_NFC_BARCODE |
//                            NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS,
//                    options);
//        }
//
//    }

    public void readFromIntent(Intent intent){
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                ||NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                ||NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_TAG);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++){
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }

        }
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        System.out.println("Tag Discovered");
    }
}