package uk.ac.lancaster.auditor

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.R.array
import android.widget.ArrayAdapter



class ChooseRecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_record)

        var fileList = getFilesDir().listFiles()

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileList)
        (R.id.list).setAdapter(aa)
    }
}