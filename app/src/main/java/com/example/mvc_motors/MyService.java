package com.example.mvc_motors;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    private Timer timer = new Timer();
    private static int count;
    BookYourRideActivitySecond activity = new BookYourRideActivitySecond();

    private String userID;
    FirebaseUser user;

    String _NAME, _SURNAME;

    DatabaseReference reference;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int onStartCommand(Intent in, int flag, int startID){
        count = 0;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                count++;

                if (count == 10){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            userID = user.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("users");
                            reference.child(userID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Users userProfile = snapshot.getValue(Users.class);
                                    _NAME = userProfile.Name;
                                    _SURNAME = userProfile.Surname;

                                    NotificationChannel channel = null;
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                                        Intent rateIntent = new Intent(getApplicationContext(), RateUsActivity.class);
                                        rateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        PendingIntent ratePIntent = PendingIntent.getActivity(getApplicationContext(), 0, rateIntent, PendingIntent.FLAG_ONE_SHOT);

                                        channel = new NotificationChannel(
                                                "1",
                                                "channel1",
                                                NotificationManager.IMPORTANCE_DEFAULT);

                                        //create the notification manager
                                        NotificationManager manager = getSystemService(NotificationManager.class);
                                        manager.createNotificationChannel(channel);

                                        String message = "Dear " + _NAME + " " + _SURNAME + ",\nYour order has been accepted.\nThank you very much and don't forget to rate us!\nEnjoy the ride!";

                                        //create the notification
                                        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "1")
                                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.mvc_motors))
                                                .setSmallIcon(R.drawable.mvc_motors)
                                                .setContentTitle("Order Accepted")
                                                .setColor(Color.RED)
                                                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                .addAction(R.drawable.ic_bike, "Rate Us", ratePIntent)
                                                .setAutoCancel(true);

                                        NotificationManagerCompat notifyAdmin = NotificationManagerCompat.from(getApplicationContext());
                                        notifyAdmin.notify(1, notification.build());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                }
            }
        }, 0, 1000);

        return super.onStartCommand(in, flag, startID);
    }

    public void onDestroy(){
        if (timer != null && count > 11){
            timer.cancel();
        }

        super.onDestroy();
    }
}