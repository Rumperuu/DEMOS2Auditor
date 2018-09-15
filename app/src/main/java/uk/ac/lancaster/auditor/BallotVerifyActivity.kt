package uk.ac.lancaster.auditor

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView

import kotlinx.android.synthetic.main.activity_ballot_verify.*

class BallotVerifyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ballot_verify)
        setSupportActionBar(toolbar)

        val extras = intent.extras
        if (extras != null) {
            val ballotHandle = extras.getString("ballotHandle")
            val myWebView: WebView = findViewById<WebView>(R.id.webview)
            myWebView.loadUrl("http://elections.lancaster.ac.uk:8000/event/audit?handle=$ballotHandle")
            myWebView.settings.javaScriptEnabled = true
        }

    }

}
