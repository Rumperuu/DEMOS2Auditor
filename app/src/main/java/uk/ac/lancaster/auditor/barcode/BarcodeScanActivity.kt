package uk.ac.lancaster.auditor.barcode

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.io.File
import java.io.FileWriter
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import uk.ac.lancaster.auditor.BallotVerifyActivity
import uk.ac.lancaster.auditor.MainActivity
import uk.ac.lancaster.auditor.R

class BarcodeScanActivity : AppCompatActivity() {

    private lateinit var mResultTextView: TextView

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
                    val handle = barcode.displayValue.split(";")[0]
                    val hmac = String(Base64.decode(barcode.displayValue.split(";")[1], Base64.DEFAULT))

                    // save the ballot
                    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                        val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "ballots")
                        if (!dir?.mkdirs()) {
                            Log.e(LOG_TAG, "Directory not created")
                        }

                        val ballotFile = File(dir, "$handle.ballot")
                        if (ballotFile.exists()) {
                            ballotFile.delete()
                        }

                        try {
                            val out = FileWriter(ballotFile)
                            out.write(handle)
                            out.flush()
                            out.close()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        val hmacFile = File(dir, "$handle.hmac")
                        if (hmacFile.exists()) {
                            hmacFile.delete()
                        }

                        try {
                            val out = FileWriter(hmacFile)
                            out.write(hmac)
                            out.flush()
                            out.close()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }

                    //if some test of the QR = ballot handle
                    val intent = Intent(baseContext, BallotVerifyActivity::class.java)
                    intent.putExtra("ballotHandle", handle)
                    startActivity(intent)
                } else
                    mResultTextView.setText(R.string.no_barcode_captured)
            } else
                Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
                        CommonStatusCodes.getStatusCodeString(resultCode)))
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private val LOG_TAG = MainActivity::class.java.simpleName
        private val BARCODE_READER_REQUEST_CODE = 1
    }
}
