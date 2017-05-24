package com.example.jg.cmps121;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);
    }
}
