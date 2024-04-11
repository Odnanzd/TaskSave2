package com.example.tasksave.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.example.tasksave.dao.UserDAO;
import com.example.tasksave.objetos.User;
import com.example.tasksave.servicesreceiver.AlarmReceiver;
import com.example.tasksave.R;

import java.util.Timer;
import java.util.TimerTask;

public class activity_splash_screen extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        UserDAO userDAO = new UserDAO(this);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        String username = "Odnan";
        String password = "1234";
        String email = "fernando.trindade.ti@gmail.com";
        User user = new User(username, password, email);
        long id = userDAO.inserir(user);

// Agendar o BroadcastReceiver para ser chamado a cada minuto (ajuste conforme necessário)

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pendingIntent);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                CharSequence name = getString(R.string.channel_name);
                String description = getString(R.string.channel_description);
                int importance = NotificationManager.IMPORTANCE_MAX;

                @SuppressLint("WrongConstant")
                NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
                channel.setDescription(description);

                long[] pattern = {0, 1000, 500, 1000};
                channel.setVibrationPattern(pattern);

                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {


                SharedPreferences sharedPrefs2 = getApplicationContext().getSharedPreferences("ArquivoPrimeiroAcesso", Context.MODE_PRIVATE);
                SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("arquivoSalvarSenha", Context.MODE_PRIVATE);
                SharedPreferences sharedPrefs3 = getApplicationContext().getSharedPreferences("ArquivoFingerPrint", Context.MODE_PRIVATE);

                if (!sharedPrefs2.getBoolean("PrimeiroAcesso", false)) {

                    Intent intent = new Intent(activity_splash_screen.this, activity_welcome.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else if (!sharedPrefs.getBoolean("SalvarSenha", false) ) {

                    Intent intent = new Intent(activity_splash_screen.this, activity_login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else if(sharedPrefs.getBoolean("SalvarSenha", false) && sharedPrefs3.getBoolean("AcessoFingerPrint", false)) {

                    Intent intent = new Intent(activity_splash_screen.this, activity_fingerprint.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else if(sharedPrefs.getBoolean("SalvarSenha", false) && !sharedPrefs3.getBoolean("AcessoFingerPrint", false)) {
                    Intent intent = new Intent(activity_splash_screen.this, activity_main.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        }, SPLASH_TIME_OUT);
    }
    }