package li_ke.notificationdemo

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_notify.setOnClickListener { MyNotificationManager.notifyClickCancel() }
        btn_notify1.setOnClickListener { MyNotificationManager.notifyClickActivities() }
    }

    override fun onStart() {
        super.onStart()
        if (!MyNotificationManager.isAppNotificationEnabled())
            AlertDialog.Builder(this)
                    .setTitle("警告")
                    .setMessage("请开启通知权限以正常的使用Demo")
                    .setPositiveButton("去开启") { dialog, _ ->
                        dialog.dismiss()
                        val intent = MyNotificationManager.getEnableNotificationIntent()
                        startActivity(intent)
                    }
                    .show()
    }
}