package ru.birkin.team.android;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class manual_decryption extends AppCompatActivity {

    Map<String, ImageView> washList = new HashMap<>();
    private Hack chosenWash = new Hack(null);
    Map<String, ImageView> bleachList = new HashMap<>();
    private Hack chosenBleach = new Hack(null);
    Map<String, ImageView> dryList = new HashMap<>();
    private Hack chosenDry = new Hack(null);
    Map<String, ImageView> ironingList = new HashMap<>();
    private Hack chosenIroning = new Hack(null);
    Map<String, ImageView> cleaningList = new HashMap<>();
    private Hack chosenCleaning = new Hack(null);


    private static class SpecialOnClick implements View.OnClickListener {
        private Hack prevChosen;
        private String nextChosen;
        Map<String, ImageView> list;

        public SpecialOnClick(Hack prevChosen, String nextChosen, Map<String, ImageView> list) {
            this.prevChosen = prevChosen;
            this.nextChosen = nextChosen;
            this.list = list;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            if (prevChosen != null) {
                ImageView imgV = list.get(prevChosen.chosen);
                if (imgV != null) {
                    imgV.setColorFilter(Color.TRANSPARENT);
                }
            }
            ImageView imgV = list.get(nextChosen);
            if (imgV != null) {
                imgV.setColorFilter(Color.parseColor("#66000000"));
            }
            prevChosen.chosen = nextChosen;
        }
    }

    private String pathToLabel;
    private String pathToClothes;
    private String name;
    private Button finalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_decryption);
        finalButton = (Button) findViewById(R.id.final_button);
        manual_decryption instance = this;
        finalButton.setVisibility(View.VISIBLE);
        finalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenWash.chosen != null && chosenBleach.chosen != null && chosenCleaning.chosen != null && chosenDry.chosen != null && chosenIroning.chosen != null) {
                    Bitmap bmpClothes = BitmapFactory.decodeFile(pathToClothes);
                    Bitmap bmpLabel = BitmapFactory.decodeFile(pathToLabel);
                    Clothes clothes = new Clothes(new BitmapWithPath(bmpClothes, pathToClothes), new BitmapWithPath(bmpLabel, pathToLabel),name,"");
                    BirkinBoxApplication
                            .getInstance()
                            .getDatabase()
                            .clothesDao()
                            .insert(clothes)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Consumer<Long>() {
                                @Override
                                public void accept(Long aLong) throws Throwable {
                                    String[] ar = new String[]{chosenBleach.chosen, chosenCleaning.chosen, chosenDry.chosen, chosenIroning.chosen, chosenWash.chosen};
                                    for (String l : ar) {
                                        ClothesLaundryRuleRef ref = new ClothesLaundryRuleRef();
                                        ref.alias = l;
                                        ref.clothesId = aLong;
                                        BirkinBoxApplication
                                                .getInstance()
                                                .getDatabase()
                                                .clothesWithLaundryRulesDao()
                                                .insert(ref)
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribeOn(Schedulers.io())
                                                .subscribe(() -> {
                                                });
                                    }
                                }
                            });
                    instance.finish();
                }
            }
        });
        if (getIntent() != null) {
            if (getIntent().hasExtra("labelPhotoPath")) {
                pathToLabel = getIntent().getStringExtra("labelPhotoPath");
            }
            if (getIntent().hasExtra("clothesPhotoPath")) {
                pathToClothes = getIntent().getStringExtra("clothesPhotoPath");
            }
            if (getIntent().hasExtra("name")) {
                name = getIntent().getStringExtra("name");
            }
        }
        washList.put("dont_wash_id", (ImageView) findViewById(R.id.dont_wash_id));
        washList.put("hand_wash_id", (ImageView) findViewById(R.id.hand_wash_id));
        washList.put("wash_30_id", (ImageView) findViewById(R.id.wash_30_id));
        washList.put("wash_50_id", (ImageView) findViewById(R.id.wash_50_id));
        washList.put("wash_60_id", (ImageView) findViewById(R.id.wash_60_id));
        washList.put("wash_40_id", (ImageView) findViewById(R.id.wash_40_id));
        washList.put("wash_70_id", (ImageView) findViewById(R.id.wash_70_id));
        washList.put("wash_95_id", (ImageView) findViewById(R.id.wash_95_id));
        for (Map.Entry<String, ImageView> entry : washList.entrySet()) {
            entry.getValue().setOnClickListener(new SpecialOnClick(chosenWash, entry.getKey(), washList));
        }
        bleachList.put("dont_bleach_id", (ImageView) findViewById(R.id.dont_bleach_id));
        bleachList.put("any_bleach_id", (ImageView) findViewById(R.id.any_bleach_id));
        bleachList.put("only_oxygen_bleach_id", (ImageView) findViewById(R.id.only_oxygen_bleach_id));
        for (Map.Entry<String, ImageView> entry : bleachList.entrySet()) {
            entry.getValue().setOnClickListener(new SpecialOnClick(chosenBleach, entry.getKey(), bleachList));
        }
        dryList.put("dont_tumble_id", (ImageView) findViewById(R.id.dont_tumble_id));
        dryList.put("lower_tumble_id", (ImageView) findViewById(R.id.lower_tumble_id));
        dryList.put("normal_tumble_id", (ImageView) findViewById(R.id.normal_tumble_id));
        dryList.put("line_tumble_id", (ImageView) findViewById(R.id.line_tumble_id));
        dryList.put("flat_tumble_id", (ImageView) findViewById(R.id.flat_tumble_id));
        dryList.put("drip_tumble_id", (ImageView) findViewById(R.id.drip_tumble_id));
        for (Map.Entry<String, ImageView> entry : dryList.entrySet()) {
            entry.getValue().setOnClickListener(new SpecialOnClick(chosenDry, entry.getKey(), dryList));
        }
        ironingList.put("do_not_iron_id", (ImageView) findViewById(R.id.do_not_iron_id));
        ironingList.put("low_temperature_ironing_id", (ImageView) findViewById(R.id.low_temperature_ironing_id));
        ironingList.put("medium_temperature_ironing_id", (ImageView) findViewById(R.id.medium_temperature_ironing_id));
        ironingList.put("max_temperature_ironing_id", (ImageView) findViewById(R.id.max_temperature_ironing_id));
        for (Map.Entry<String, ImageView> entry : ironingList.entrySet()) {
            entry.getValue().setOnClickListener(new SpecialOnClick(chosenIroning, entry.getKey(), ironingList));
        }
        cleaningList.put("dont_drycleaning_id", (ImageView) findViewById(R.id.dont_drycleaning_id));
        cleaningList.put("w_drycleaning_id", (ImageView) findViewById(R.id.w_drycleaning_id));
        cleaningList.put("p_drycleaning_id", (ImageView) findViewById(R.id.p_drycleaning_id));
        cleaningList.put("f_drycleaning_id", (ImageView) findViewById(R.id.f_drycleaning_id));
        for (Map.Entry<String, ImageView> entry : cleaningList.entrySet()) {
            entry.getValue().setOnClickListener(new SpecialOnClick(chosenCleaning, entry.getKey(), cleaningList));
        }
    }
}