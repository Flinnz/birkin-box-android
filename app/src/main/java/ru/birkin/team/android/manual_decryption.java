package ru.birkin.team.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class manual_decryption extends AppCompatActivity {

    List<ImageView> washList = new ArrayList<ImageView>();
    List<ImageView> bleachList = new ArrayList<ImageView>();
    List<ImageView> dryList = new ArrayList<ImageView>();
    List<ImageView> ironingList = new ArrayList<ImageView>();
    List<ImageView> cleaningList = new ArrayList<ImageView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        washList.add(findViewById(R.id.dont_wash_id));
        washList.add(findViewById(R.id.hand_wash_id));
        washList.add(findViewById(R.id.wash_30_id));
        washList.add(findViewById(R.id.wash_40_id));
        washList.add(findViewById(R.id.wash_50_id));
        washList.add(findViewById(R.id.wash_60_id));
        washList.add(findViewById(R.id.wash_70_id));
        washList.add(findViewById(R.id.wash_95_id));

        bleachList.add(findViewById(R.id.dont_bleach_id));
        bleachList.add(findViewById(R.id.any_bleach_id));
        bleachList.add(findViewById(R.id.only_oxygen_bleach_id));

        dryList.add(findViewById(R.id.dont_tumble_id));
        dryList.add(findViewById(R.id.lower_tumble_id));
        dryList.add(findViewById(R.id.normal_tumble_id));
        dryList.add(findViewById(R.id.line_tumble_id));
        dryList.add(findViewById(R.id.flat_tumble_id));
        dryList.add(findViewById(R.id.drip_tumble_id));

        ironingList.add(findViewById(R.id.do_not_iron_id));
        ironingList.add(findViewById(R.id.low_temperature_ironing_id));
        ironingList.add(findViewById(R.id.medium_temperature_ironing_id));
        ironingList.add(findViewById(R.id.max_temperature_ironing_id));

        cleaningList.add(findViewById(R.id.dont_drycleaning_id));
        cleaningList.add(findViewById(R.id.w_drycleaning_id));
        cleaningList.add(findViewById(R.id.p_drycleaning_id));
        cleaningList.add(findViewById(R.id.f_drycleaning_id));

        setContentView(R.layout.activity_manual_decryption);
    }
}