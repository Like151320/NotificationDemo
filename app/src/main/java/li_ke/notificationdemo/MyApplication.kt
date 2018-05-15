package li_ke.notificationdemo

import android.app.Application

/**
 * 作者: Li_ke
 * 日期: 2018/5/11 11:08
 * 作用:
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MyNotificationManager.init(this)
    }
}