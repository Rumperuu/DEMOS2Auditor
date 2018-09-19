package uk.ac.lancaster.auditor

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import uk.ac.lancaster.auditor.barcode.BarcodeScanActivity

class AuditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audit)

        findViewById<Button>(R.id.button).setOnClickListener {
            val intent = Intent(applicationContext, ChooseRecordActivity::class.java)
            startActivityForResult(intent, 1)
        }
        findViewById<Button>(R.id.button2).setOnClickListener {
            val intent = Intent(applicationContext, ChooseRecordActivity::class.java)
            startActivityForResult(intent, 1)
        }
        findViewById<Button>(R.id.button3).setOnClickListener {
            val intent = Intent(applicationContext, ChooseRecordActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(data: Intent?) {

    }
}