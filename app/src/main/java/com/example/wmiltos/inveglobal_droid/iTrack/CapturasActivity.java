package com.example.wmiltos.inveglobal_droid.iTrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.wmiltos.inveglobal_droid.R;

public class CapturasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capturas);


        Intent intent = getIntent();
        String texto1 = intent.getStringExtra("texto1");
        String texto2 = intent.getStringExtra("texto2");
        String texto3 = intent.getStringExtra("texto3");

        TextView textView = (TextView)findViewById(R.id.text);
        textView.setText("Hello i´m " + texto2 + "\n" + texto3);

    }
}
