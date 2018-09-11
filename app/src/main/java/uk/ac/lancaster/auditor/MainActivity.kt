package uk.ac.lancaster.auditor

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import uk.ac.lancaster.auditor.barcode.BarcodeScanActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mResultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.scan_ballot_qr).setOnClickListener {
            val intent = Intent(applicationContext, BarcodeScanActivity::class.java)
            startActivity(intent)
        }
    }
}
