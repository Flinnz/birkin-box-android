package ru.birkin.team.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GarmentActivity extends AppCompatActivity {
    private long clothesId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garment);
        clothesId = getIntent().getLongExtra("clothesId", 0);
        if (clothesId != 0) {
            BirkinBoxApplication.getInstance().getDatabase().clothesWithLaundryRulesDao()
                    .getClothesWithRulesById(clothesId).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<List<LaundryRule>>() {
                        @Override
                        public void accept(List<LaundryRule> laundryRule) throws Throwable {
                            StringBuilder sb = new StringBuilder();
                            for(LaundryRule lr : laundryRule) {
                                sb.append(lr.displayName);
                                sb.append('\n');
                            }
                            ((TextView)findViewById(R.id.washing_instructions)).setText(sb.toString());
                            ((ImageView)findViewById(R.id.dryclean_id)).setImageResource(laundryRule.get(0).drawableId);
                            ((ImageView)findViewById(R.id.wasing_id)).setImageResource(laundryRule.get(1).drawableId);
                            ((ImageView)findViewById(R.id.iron_id)).setImageResource(laundryRule.get(2).drawableId);
                            ((ImageView)findViewById(R.id.bleach_id)).setImageResource(laundryRule.get(3).drawableId);
                            ((ImageView)findViewById(R.id.tumble_id)).setImageResource(laundryRule.get(4).drawableId);
                        }
                    });
        }
        String labelPath = getIntent().getStringExtra("labelPhoto");
        if (labelPath != null) {
            ((ImageView)findViewById(R.id.label_photo)).setImageBitmap(BitmapFactory.decodeFile(labelPath));
        }
        String clothesPath = getIntent().getStringExtra("clothesPhoto");
        if (labelPath != null) {
            ((ImageView)findViewById(R.id.clothes_photo_preview)).setImageBitmap(BitmapFactory.decodeFile(clothesPath));
        }
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