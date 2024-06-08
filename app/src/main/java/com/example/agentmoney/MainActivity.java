package com.example.agentmoney;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.credentials.PrepareGetCredentialResponse;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etAccount;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignup;
    private Button btnNotificationTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNotificationTest = findViewById(R.id.btn_notification_test);

        btnNotificationTest.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_notification_test) {
                notification("test");
            }
        }
    };

    /*
    因為android 13有通知權限的改動，需要在發通知前請求權限。此函式會在下方requestPermission執行後執行。
    requestCode對應requestPermission裡給的requestCode，有需要使用要注意是否一致。
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 這裡面可以寫權限允許後執行什麼動作，也可以寫不允許後執行什麼動作
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "同意通知", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /*
    這裡是通知的函式，暫時先放這裡，之後應該會抽出去。
     */
    private void notification(String message) {
        // Android 8之後都需要建立通道給通知使用
        String CHANNEL_ID = "Save Money";

        // 判斷當前版本是否大於8，若是，建立一個通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "DEMO", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        // 建立一個空Intent，用來讓通知欄裡按下通知時可以使通知消失
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(), PendingIntent.FLAG_IMMUTABLE);

        // 通知的本體
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("救救錢包!")
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // 因為Android 13通知權限的改動，需要發通知前先請求權限
        NotificationManagerCompat compat = NotificationManagerCompat.from(MainActivity.this);
        if (Build.VERSION.SDK_INT >= 33) {
            if (ActivityCompat.checkSelfPermission(
                    this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
            compat.notify(1, builder.build());
        }
    }
}