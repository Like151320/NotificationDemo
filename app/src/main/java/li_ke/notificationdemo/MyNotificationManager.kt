package li_ke.notificationdemo

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat

/**
 * 同一管理通知
 * 悬浮通知：notify后，通知栏会悬浮在屏幕顶部一段时间
 *
 * 如何处理点击通知后的页面跳转？
 * 参考其它App：
 * 知乎会 -> 目标页 -> 返回到欢迎页 -> 首页
 * 京东会 -> 欢迎页 -> 目标页，返回后直接退出程序。
 * UC会  -> 目标页 -> 返回到首页
 * 移动会 -> 目标页 -> 返回到欢迎页 -> 首页
 * 使用PendingIntent.getActivities()来打开多个Activity。
 */
object MyNotificationManager {
    private const val CHANNEL_ID = "1234567"
    private lateinit var application: Application
    private val notificationManager: NotificationManager by lazy { application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    /**初始化*/
    fun init(application: Application) {
        this.application = application
        //适配Android8.0以上的通知（用户可操作的通知）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "主功能通知", NotificationManager.IMPORTANCE_HIGH)
            channel.description = "司机抵达、支付完成等重要通知"

            channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC//是否在锁定的屏幕上显示
            channel.setShowBadge(true)//是否显示角标
            channel.setBypassDnd(true)//绕过免打扰

            channel.enableLights(true)//呼吸灯
            channel.enableVibration(true)//开启震动
            //channel.setSound(Uri.fromFile(), new AudioAttributes.Builder());//声音
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**程序是否允许通知/是否开启了通知权限。配合[getEnableNotificationIntent]*///有些手机(oppo)默认没有通知权限
    fun isAppNotificationEnabled(): Boolean {
        return NotificationManagerCompat.from(application).areNotificationsEnabled()
    }

    /**获取去开启通知的intent，用startActivity启动它。配合[isAppNotificationEnabled]*/
    fun getEnableNotificationIntent(): Intent {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", application.packageName, null)
        return intent
    }

    fun notifyClickCancel() {
        val builder = NotificationCompat.Builder(application, CHANNEL_ID)
        builder.setLargeIcon(BitmapFactory.decodeResource(application.resources, R.mipmap.ic_launcher))//设置大图标 不写会无提示的失败
        builder.setContentTitle("标题")
        builder.setContentText("内容")
        builder.setSmallIcon(R.drawable.ic_warning_black_24dp)
        builder.setTicker("上飘")//通知时状态栏向上飘的文字
        builder.setAutoCancel(true)//可滑动取消
        builder.setDefaults(Notification.DEFAULT_SOUND.or(Notification.DEFAULT_VIBRATE))//声音+震动 在Android8.0以上，声音/震动只能在 Channel 中设置

        //点击关闭 - 点击后会自动关闭，那么做一个无用的点击处理即可，例如发一个不处理的广播
        builder.setContentIntent(PendingIntent.getBroadcast(application, 101, Intent("userTouchNotification"), PendingIntent.FLAG_ONE_SHOT))

        //点击打开页面
//        builder.setContentIntent(PendingIntent.getActivity(application, 100, Intent(application, TargetActivity::class.java), PendingIntent.FLAG_ONE_SHOT))
        //由 Intent(application, MainActivity::class.java) 得知，启动后此MainActivity应该是在新的Activity栈中的。

        val notification = builder.build()
        notificationManager.notify(100, notification)
    }

    fun notifyClickActivities() {
        val builder = NotificationCompat.Builder(application, CHANNEL_ID)
        builder.setLargeIcon(BitmapFactory.decodeResource(application.resources, R.mipmap.ic_launcher))//设置大图标 不写会无提示的失败
        builder.setContentTitle("标题")
        builder.setContentText("内容")
        builder.setSmallIcon(R.drawable.ic_warning_black_24dp)
        builder.setTicker("上飘")//通知时状态栏向上飘的文字
        builder.setAutoCancel(true)//可滑动取消
        builder.setDefaults(Notification.DEFAULT_SOUND.or(Notification.DEFAULT_VIBRATE))//声音+震动 在Android8.0以上，声音/震动只能在 Channel 中设置

        //点击打开多个页面
        val intent = arrayOf(Intent(application, MainActivity::class.java), Intent(application, TargetActivity::class.java))//数组后面是页面栈上层，前面是下层
        builder.setContentIntent(PendingIntent.getActivities(application, 100, intent, PendingIntent.FLAG_ONE_SHOT))

        //由 Intent(application, MainActivity::class.java) 得知，启动后此MainActivity应该是在新的Activity栈中的。

        val notification = builder.build()
        notificationManager.notify(100, notification)
    }
}