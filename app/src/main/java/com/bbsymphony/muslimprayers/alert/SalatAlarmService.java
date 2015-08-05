package com.bbsymphony.muslimprayers.alert;

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

import com.bbsymphony.muslimprayers.ConfigurationClass;
import com.bbsymphony.muslimprayers.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SalatAlarmService extends IntentService {

    private static String LOG = "SalatAlarmService";
    private DateFormat df = new SimpleDateFormat("HH:mm");
    private static MediaPlayer mpSalat;
    private static MediaPlayer mpWudu;

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
        SharedPreferences prefs =  this.getSharedPreferences(ConfigurationClass.SHARED_PREF,
                Context.MODE_PRIVATE);

        if (isTimeWudu(intent) && !prefs.getString("notifications_abulition_id","999").equals("0")) {
            playWuduSound(this.getApplicationContext());
        }

        if (isTimeSalat(intent) && prefs.getBoolean("notifications_salat_id",true)) {
            playSalatSound(this.getApplicationContext());
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG, "[ONSTARTCOMMAND] Service kicked off...");
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    private void playSalatSound(Context context) {
        Log.d(LOG, "[PLAYSOUND] Playing Salat sound...");
        mpSalat = MediaPlayer.create(context, R.raw.adhan_makkah);
        Log.d(LOG, "Click on Media Player Salat Test");
        try {
            if (mpSalat != null) {
                Log.d(LOG, "Killing actaul media player...");
                mpSalat.stop();
                mpSalat.release();
                mpSalat = null;
            }
            Log.d(LOG,"Playing media player wudu_djouher072015...");
            mpSalat = MediaPlayer.create(context, R.raw.adhan_makkah);
            mpSalat.prepare();
            mpSalat.start();
        } catch (Exception e) {
            Log.d(LOG, e.toString());
        }
    }

    private void playWuduSound(Context context) {
        Log.d(LOG, "[PLAYSOUND] Playing Wudu sound...");
        mpWudu = MediaPlayer.create(context, R.raw.wudu_djouher072015);
        try {
            if (mpWudu != null) {
                Log.d(LOG, "Killing actaul media player...");
                mpWudu.stop();
                mpWudu.release();
                mpWudu = null;
            }
            Log.d(LOG,"Playing media player wudu_djouher072015...");
            mpWudu = MediaPlayer.create(context, R.raw.wudu_djouher072015);
            mpWudu.prepare();
            mpWudu.start();
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

        if (prayerTimes.getString(ConfigurationClass.EXTRA_FAJR,"99:99").equals(time)) {
            return true;
        }
        if (prayerTimes.getString(ConfigurationClass.EXTRA_DUHR,"99:99").equals(time)) {
            return true;
        }
        if (prayerTimes.getString(ConfigurationClass.EXTRA_ASR,"99:99").equals(time)) {
            return true;
        }
        if (prayerTimes.getString(ConfigurationClass.EXTRA_MAGHRIB,"99:99").equals(time)) {
            return true;
        }
        if (prayerTimes.getString(ConfigurationClass.EXTRA_ISHAA,"99:99").equals(time)) {
            return true;
        }
        return false;
    }


    private boolean isTimeWudu (Intent intent) {
        String time = df.format(new Date());
        Bundle prayerTimesWudu = intent.getExtras();

        Log.d(LOG,"Fajr_wudu_time:" + prayerTimesWudu.getString(ConfigurationClass.EXTRA_WUDU_FAJR)  + "\n" +
                "Duhr_wudu_time:" + prayerTimesWudu.getString(ConfigurationClass.EXTRA_WUDU_DUHR)  + "\n" +
                "Asr_wudu_time:" + prayerTimesWudu.getString(ConfigurationClass.EXTRA_WUDU_ASR)  + "\n" +
                "Maghrib_wudu_time:" + prayerTimesWudu.getString(ConfigurationClass.EXTRA_WUDU_MAGHRIB)  + "\n" +
                "Isha_wudu_time:" + prayerTimesWudu.getString(ConfigurationClass.EXTRA_WUDU_ISHAA)  + "\n" +
                " time: " + time);
        if (prayerTimesWudu.getString(ConfigurationClass.EXTRA_WUDU_FAJR,"99:99").equals(time)) {
            return true;
        }
        if (prayerTimesWudu.getString(ConfigurationClass.EXTRA_WUDU_DUHR,"99:99").equals(time)) {
            return true;
        }
        if (prayerTimesWudu.getString(ConfigurationClass.EXTRA_WUDU_ASR,"99:99").equals(time)) {
            return true;
        }
        if (prayerTimesWudu.getString(ConfigurationClass.EXTRA_WUDU_MAGHRIB,"99:99").equals(time)) {
            return true;
        }
        if (prayerTimesWudu.getString(ConfigurationClass.EXTRA_WUDU_ISHAA,"99:99").equals(time)) {
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy ()  {
        super.onDestroy();
    }

}