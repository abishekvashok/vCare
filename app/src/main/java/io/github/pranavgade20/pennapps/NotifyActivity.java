package io.github.pranavgade20.pennapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

public class NotifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        Button close = findViewById(R.id.notify_close);
        Button learnmore = findViewById(R.id.notify_learnmore);

        close.setOnClickListener(v -> {
            finish();
        });
        learnmore.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cancercenter.com/cancer-types/lung-cancer/symptoms")));
        });
    }
}