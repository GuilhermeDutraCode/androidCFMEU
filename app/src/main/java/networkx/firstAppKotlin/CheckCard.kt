import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import networkx.firstAppKotlin.R


class CheckCard : AppCompatActivity() {
    private var nfcAdapter : NfcAdapter? = null
    private var nfcTagId : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_check_card)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, arrayOf(intentFilter), null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNFCIntent(intent)
    }

    private fun handleNFCIntent(intent: Intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action){
            val rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val message = rawMessages?.getOrNull(0) as? NdefMessage
            val idBytes = message?.records?.getOrNull(0)?.id
            val id = idBytes?.joinToString(separator = "") { byte -> String.format("%02X", byte) }
            Log.d("NFC", "ID: $id")
        }
    }
}
