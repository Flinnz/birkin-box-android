package ru.birkin.team.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class GarmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garment);
        add_listener();
    }

    public void add_listener () {
        LinearLayout changeWashingSymbols = (LinearLayout) findViewById(R.id.change_washing_symbols);
        changeWashingSymbols.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, manual_decryption.class);
                context.startActivity(intent);
            }
        });
    }
}