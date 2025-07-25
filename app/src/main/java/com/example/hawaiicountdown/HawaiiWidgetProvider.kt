package com.example.hawaiicountdown

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class HawaiiWidgetProvider : AppWidgetProvider() {

    private val targetDate: LocalDate = LocalDate.of(2025, 10, 8) // trip start

    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        for (widgetId in ids) updateWidget(context, manager, widgetId)
        scheduleNextUpdate(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == "com.example.hawaiicountdown.WIDGET_TAP") {
            Toast.makeText(context, "Hawaii!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateWidget(ctx: Context, mgr: AppWidgetManager, id: Int) {
        val today = LocalDate.now(ZoneId.systemDefault())
        val daysLeft = ChronoUnit.DAYS.between(today, targetDate).coerceAtLeast(0)

        val views = RemoteViews(ctx.packageName, R.layout.widget_hawaii)
        views.setTextViewText(R.id.daysNumber, daysLeft.toString())

        // Create the tap PendingIntent
        val tapIntent = Intent(ctx, HawaiiWidgetProvider::class.java).apply {
            action = "com.example.hawaiicountdown.WIDGET_TAP"
        }
        val pendingTapIntent = PendingIntent.getBroadcast(
            ctx,
            1,
            tapIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        views.setOnClickPendingIntent(R.id.widgetRoot, pendingTapIntent)

        mgr.updateAppWidget(id, views)
    }

    private fun scheduleNextUpdate(ctx: Context) {
        val alarmMgr = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(ctx, HawaiiWidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        }
        val pending = PendingIntent.getBroadcast(
            ctx,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val zone = ZoneId.systemDefault()
        val nextMidnightMillis = LocalDate.now(zone)
            .plusDays(1)
            .atStartOfDay(zone)
            .toInstant()
            .toEpochMilli()

        alarmMgr.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            nextMidnightMillis,
            pending
        )
    }
}