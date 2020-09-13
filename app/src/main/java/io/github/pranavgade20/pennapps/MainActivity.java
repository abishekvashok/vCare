package io.github.pranavgade20.pennapps;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    FrameLayout gradientView = null;
    TextView mainText = null;
    ImageView imageView = null;
    SharedPreferences sharedPref;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = this.getSharedPreferences(getString(R.string.user_data_file), Context.MODE_PRIVATE);
        tryAndGetPermission();
        if (sharedPref.contains(getString(R.string.is_user_onboarded))) {
            try {
                ((JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE)).cancelAll();
                scheduleJob();
            } catch (Exception e) {
                e.printStackTrace();

                Toast error = new Toast(this);
                error.setText("An error occurred while syncing.");
                error.show();
            }
        } else {
            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);
        }
        gradientView = findViewById(R.id.gradient_view);
        boolean monitoring = sharedPref.getBoolean("monitoring", false);
        mainText = findViewById(R.id.main_text);
        imageView = findViewById(R.id.main_imageview);
        if(!monitoring){
            setMonitoringStateIndicators(false);
        }
        Switch s1 = findViewById(R.id.switch1);
        s1.setChecked(monitoring);
        s1.setOnClickListener(v -> {
            setMonitoringStateIndicators(s1.isChecked());
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setMonitoringStateIndicators(boolean monitoring){
        sharedPref.edit().putBoolean("monitoring", monitoring).apply();
        if(monitoring){
            imageView.setImageResource(R.drawable.ic_baseline_check_24);
            mainText.setText(R.string.main_ok_text);
            gradientView.setBackground(getDrawable(R.drawable.gradient_on));
        } else {
            imageView.setImageResource(R.drawable.ic_baseline_warning_24);
            mainText.setText(R.string.main_warning_text);
            gradientView.setBackground(getDrawable(R.drawable.gradient_off));
            ((JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE)).cancelAll();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void scheduleJob() {
        ComponentName serviceName = new ComponentName(getPackageName(),
                BackgroundJobService.class.getName());
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(24*60*60*1000); // every 24 hours, but this does not guarantee a specific time - its cool
        ((JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE)).schedule(builder.build());
    }
    void tryAndGetPermission(){
        String[] perms = {Manifest.permission.BODY_SENSORS};
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,perms ,10911);
        }
    }

}
