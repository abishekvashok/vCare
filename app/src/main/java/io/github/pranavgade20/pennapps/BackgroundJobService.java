package io.github.pranavgade20.pennapps;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import io.github.pranavgade20.pennapps.networkmanager.Request;

import static java.text.DateFormat.getDateInstance;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class BackgroundJobService extends JobService {


    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    NotificationManager mNotifyManager;
    @Override
    public boolean onStartJob(JobParameters params) {
        createNotificationChannel();
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (this, 0, new Intent(this, MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (this, PRIMARY_CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Checking your daily activity against our model")
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);
        mNotifyManager.notify(0, builder.build());

        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();

        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = getDateInstance();

        GoogleSignInOptionsExtension fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
                        .build();

        GoogleSignInAccount googleSignInAccount =
                GoogleSignIn.getAccountForExtension(this, fitnessOptions);

        Task<DataReadResponse> response = Fitness.getHistoryClient(this, googleSignInAccount)
                .readData(new DataReadRequest.Builder()
                        .read(DataType.TYPE_HEART_RATE_BPM)
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                        .bucketByTime(1, TimeUnit.DAYS)
                        .enableServerQueries()
                        .build());


        new Thread(new Runnable() {
            @Override
            public void run() {
                DataReadResponse readDataResult = null;
                try {
                    readDataResult = Tasks.await(response);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    return;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                DataSet dataSet = readDataResult.getDataSet(DataType.TYPE_HEART_RATE_BPM);
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.user_data_file), Context.MODE_PRIVATE);
                int i = sharedPref.getInt("i", 0);
                int a[] = new int[4];
                if(i != 0){
                    for(int j = 0; j < i; j++){
                        String key = "h".concat(Integer.toString(i));
                        a[j] = sharedPref.getInt(key, 0);
                        if(a[j] == 0){
                            i = j;
                            break;
                        }
                    }
                }
                ArrayList<Integer> results = new ArrayList<Integer>();
                for(DataPoint dp: dataSet.getDataPoints()){
                    int beats = Integer.parseInt(dp.getValue(Field.FIELD_BPM).toString());
                    a[i] = beats;
                    i++;
                    String key = "h".concat(Integer.toString(i));
                    sharedPref.edit().putInt(key, beats).apply();
                    if(i == 4){
                        results.add(Request.make(sharedPref.getInt("AGE", 65), sharedPref.getInt("GENDER", 0), a[0],
                                a[1], a[2], a[3]));
                        i = 0;
                    }
                }
                for(int result: results){
                    if(result == 1){
                        Intent intent = new Intent(getApplicationContext(), NotifyActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        }).start();
        mNotifyManager.cancelAll();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    /**
     * Creates a Notification channel, for OREO and higher.
     */
    public void createNotificationChannel() {

        // Create a notification manager object.
        mNotifyManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Sync updates");

            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }
}
