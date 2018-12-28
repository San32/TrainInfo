package com.example.kyj.traininfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TrainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);
        setTitle("hello");

        TextView textView = (TextView)findViewById(R.id.textView);

        Intent intent = getIntent();
        String text = intent.getExtras().getString("key");
        textView.setText(text);
        setTitle(text);

    }
}
