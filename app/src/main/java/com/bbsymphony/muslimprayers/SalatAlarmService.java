package com.bbsymphony.muslimprayers;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SalatAlarmService extends IntentService {

    private static String LOG = "SalatAlarmService";
    private DateFormat df = new SimpleDateFormat("HH:mm");
    private MediaPlayer mp;

    public SalatAlarmService() {
        super(LOG);
    }

    public SalatAlarmService(String service) {
        super(service);
    }


    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(LOG, "[ONSTART] Service kicked off...");
        super.onStart(intent, startId);
        SharedPreferences prefs =  this.getSharedPreferences(MuslimPrayersBroadcastReceiver.SHARED_PREF,
                Context.MODE_PRIVATE);

        if (isTimeWudu(intent) && !prefs.getString("notifications_abulition_id","999").equals("0")) {
            playSound(this.getApplicationContext());
        }

        if (isTimeSalat(intent) && prefs.getBoolean("notifications_salat_id",true)) {
            playSound(this.getApplicationContext());
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG, "[ONSTARTCOMMAND] Service kicked off...");
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    private void playSound(Context context) {
        Log.d(LOG, "[PLAYSOUND] Playing sound...");
        try {
            mp = MediaPlayer.create(context, R.raw.adhan_makkah);
            mp.prepare();
            mp.start();
            mp.release();
        } catch (Exception e) {
            Log.d(LOG, e.toString());
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    private boolean isTimeSalat (Intent intent) {
        String time = df.format(new Date());
        Bundle prayerTimes = intent.getExtras();

        Log.d(LOG,"Fajr_time:" + prayerTimes.getString("fajr_time") +
                " time: " + time);
        if (prayerTimes.getString("fajr_time","99:99").equals(time)) {
            return true;
        }
        if (prayerTimes.getString("duhr_time","99:99").equals(time)) {
            return true;
        }
        if (prayerTimes.getString("asr_time","99:99").equals(time)) {
            return true;
        }
        if (prayerTimes.getString("maghrib_time","99:99").equals(time)) {
            return true;
        }
        if (prayerTimes.getString("isha_time","99:99").equals(time)) {
            return true;
        }
        return false;
    }


    private boolean isTimeWudu (Intent intent) {
        String time = df.format(new Date());
        Bundle prayerTimesWudu = intent.getExtras();

        Log.d(LOG,"Fajr_wudu_time:" + prayerTimesWudu.getString("fajr_wudu_time")  + "\n" +
                "Duhr_wudu_time:" + prayerTimesWudu.getString("duhr_wudu_time")  + "\n" +
                "Asr_wudu_time:" + prayerTimesWudu.getString("asr_wudu_time")  + "\n" +
                "Maghrib_wudu_time:" + prayerTimesWudu.getString("maghrib_wudu_time")  + "\n" +
                "Isha_wudu_time:" + prayerTimesWudu.getString("isha_wudu_time")  + "\n" +
                " time: " + time);
        if (prayerTimesWudu.getString("fajr_wudu_time","99:99").equals(time)) {
            return true;
        }
        if (prayerTimesWudu.getString("duhr_wudu_time","99:99").equals(time)) {
            return true;
        }
        if (prayerTimesWudu.getString("asr_wudu_time","99:99").equals(time)) {
            return true;
        }
        if (prayerTimesWudu.getString("maghrib_wudu_time","99:99").equals(time)) {
            return true;
        }
        if (prayerTimesWudu.getString("isha_wudu_time","99:99").equals(time)) {
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy ()  {
        super.onDestroy();
    }

}