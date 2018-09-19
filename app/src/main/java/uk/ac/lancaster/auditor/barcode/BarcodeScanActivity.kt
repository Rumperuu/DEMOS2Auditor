package uk.ac.lancaster.auditor.barcode

import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.io.File
import java.io.FileOutputStream
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import uk.ac.lancaster.auditor.BallotVerifyActivity
import uk.ac.lancaster.auditor.MainActivity
import uk.ac.lancaster.auditor.R

class BarcodeScanActivity : AppCompatActivity() {

    private lateinit var mResultTextView: TextView
    private var stage = 0

    data class Record(val recordID: Int) {
        var voterID: String = ""
        var eventID: String = ""
        var pollID: String = ""
        var hashes: String = ""
        var handle: String = ""
        var hmac: String = ""
    }

    private val record = Record(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_scan)

        mResultTextView = findViewById(R.id.result_textview)

        findViewById<Button>(R.id.scan_barcode_button).setOnClickListener {
            val intent = Intent(applicationContext, BarcodeCaptureActivity::class.java)
            startActivityForResult(intent, BARCODE_READER_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    val barcode = data.getParcelableExtra<Barcode>(BarcodeCaptureActivity.BarcodeObject)
                    mResultTextView.text = barcode.displayValue

                    // Store the relevant info from whatever this QR code is for
                    when (stage) {
                        // Stage 0 is getting the voter ID, poll ID and event ID
                        0 -> {
                            val barcodeVal = barcode.displayValue.split(";")
                            record.voterID = barcodeVal[0]
                            record.eventID = barcodeVal[1]
                            record.pollID = barcodeVal[2]
                        }
                        // Stage 1 is getting the hashes of both ballots
                        1 -> {
                            record.hashes = barcode.displayValue
                        }
                        // Stage 2 is getting the handle of the unselected ballot on the server
                        2 -> {
                            record.handle = barcode.displayValue.split(";")[0]
                            record.hmac = String(Base64.decode(barcode.displayValue.split(";")[1], Base64.DEFAULT))
                            saveRecord()
                            finish()
                        }
                        else -> {
                            //something's gone wrong if this gets called
                        }
                    }
                    stage++
                } else
                    mResultTextView.setText(R.string.no_barcode_captured)
            } else
                Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
                        CommonStatusCodes.getStatusCodeString(resultCode)))
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }

    private fun saveRecord() {
        // save the ballot
        val filename = record.handle
        val fileContents = record.toString()
        val outputStream: FileOutputStream

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE)
            outputStream.write(fileContents.toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        private val LOG_TAG = MainActivity::class.java.simpleName
        private val BARCODE_READER_REQUEST_CODE = 1
    }
}
