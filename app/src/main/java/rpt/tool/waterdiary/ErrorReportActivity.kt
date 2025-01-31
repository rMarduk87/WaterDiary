package rpt.tool.waterdiary

import android.os.StrictMode
import android.widget.Toast
import rpt.tool.waterdiary.base.BaseActivity
import rpt.tool.waterdiary.utils.ExceptionHandler


class ErrorReportActivity : BaseActivity() {
    var error: android.widget.TextView? = null
    var btn_cancel_error: android.widget.Button? = null
    var btn_send_error: android.widget.Button? = null


    @android.annotation.SuppressLint("NewApi")
    protected override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        setContentView(R.layout.activity_error_report)

        mContext = this@ErrorReportActivity

        error = findViewById<android.widget.TextView>(R.id.error)
        btn_cancel_error = findViewById(R.id.btn_cancel_error)
        btn_send_error = findViewById(R.id.btn_send_error)

        error!!.text = getIntent().getStringExtra("error")

        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        android.os.StrictMode.setThreadPolicy(policy)

        btn_send_error!!.setOnClickListener {
            try {
                val intent = android.content.Intent(
                    android.content.Intent.ACTION_VIEW,
                    android.net.Uri.parse("mailto:" + "jigar.infibrain@gmail.com")
                )
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Android Bug Report")
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "" + error!!.text.toString())
                startActivity(intent)
            } catch (ex: java.lang.Exception) {
                Toast.makeText(
                    this@ErrorReportActivity,
                    ex.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        btn_cancel_error!!.setOnClickListener {
            AppClose.exitApplication(
                mContext!!
            )
        }
    }
}