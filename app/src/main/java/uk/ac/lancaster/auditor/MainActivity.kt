package uk.ac.lancaster.auditor

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import uk.ac.lancaster.auditor.barcode.BarcodeScanActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.cast_vote).setOnClickListener {
            val intent = Intent(applicationContext, BarcodeScanActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.audit_election).setOnClickListener {
            val intent = Intent(applicationContext, AuditActivity::class.java)
            startActivity(intent)
        }
    }
}
