package ru.birkin.team.android;

import android.app.Application;
import android.widget.ImageView;

import androidx.room.Room;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BirkinBoxApplication extends Application {
    public static BirkinBoxApplication instance;
    private ClothesDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, ClothesDatabase.class, "birkinbox")
                .build();
        Map<String, LaundryRule> laundryRules = new HashMap<>();
        laundryRules.put("dont_wash_id", new LaundryRule("dont_wash_id", "Ручная стирка", R.drawable.dont_wash));
        laundryRules.put("hand_wash_id", new LaundryRule("hand_wash_id", "Не стирать", R.drawable.hand_wash));
        laundryRules.put("wash_30_id", new LaundryRule("wash_30_id", "Стирка при 30 градусов", R.drawable.wash_30));
        laundryRules.put("wash_50_id", new LaundryRule("wash_50_id", "Стирка при 50 градусов", R.drawable.wash_50));
        laundryRules.put("wash_60_id", new LaundryRule("wash_60_id", "Стирка при 60 градусов", R.drawable.wash_60));
        laundryRules.put("wash_40_id", new LaundryRule("wash_40_id", "Стирка при 40 градусов", R.drawable.wash_40));
        laundryRules.put("wash_70_id", new LaundryRule("wash_70_id", "Стирка при 70 градусов", R.drawable.wash_70));
        laundryRules.put("wash_95_id", new LaundryRule("wash_95_id", "Стирка при 95 градусов", R.drawable.wash_95));

        laundryRules.put("dont_bleach_id", new LaundryRule("dont_bleach_id", "Не отбеливать", R.drawable.dont_bleach));
        laundryRules.put("any_bleach_id", new LaundryRule("any_bleach_id", "Любое отбеливание", R.drawable.any_bleach));
        laundryRules.put("only_oxygen_bleach_id", new LaundryRule("only_oxygen_bleach_id", "Только кислородное отбеливание", R.drawable.only_oxygen_bleach));

        laundryRules.put("dont_tumble_id", new LaundryRule("dont_tumble_id", "Сушка запрещена", R.drawable.dont_tumble));
        laundryRules.put("lower_tumble_id", new LaundryRule("lower_tumble_id", "Сушка до 60 градусов", R.drawable.lower_tumble));
        laundryRules.put("normal_tumble_id", new LaundryRule("normal_tumble_id", "Сушка до 80 градусов", R.drawable.normal_tumble));
        laundryRules.put("line_tumble_id", new LaundryRule("line_tumble_id", "Вертикальная сушка без отжима", R.drawable.line_dry));
        laundryRules.put("flat_tumble_id", new LaundryRule("flat_tumble_id", "Горизонтальная сушка без отжима", R.drawable.flat_dry));
        laundryRules.put("drip_tumble_id", new LaundryRule("drip_tumble_id", "Горизонтальная сушка с отжимом", R.drawable.drip_dry));

        laundryRules.put("low_temperature_ironing_id", new LaundryRule("low_temperature_ironing_id", "Гладить при низкой температуре", R.drawable.low_temperature_ironing));
        laundryRules.put("medium_temperature_ironing_id", new LaundryRule("medium_temperature_ironing_id", "Гладить при средней температуре", R.drawable.medium_temperature_ironing));
        laundryRules.put("max_temperature_ironing_id", new LaundryRule("max_temperature_ironing_id", "Гладить при максимальной температуре", R.drawable.max_temperature_ironing));
        laundryRules.put("do_not_iron_id", new LaundryRule("do_not_iron_id", "Не гладить", R.drawable.do_not_iron));

        laundryRules.put("dont_drycleaning_id", new LaundryRule("dont_drycleaning_id", "Химчистка запрещена", R.drawable.no_drycleaning));
        laundryRules.put("w_drycleaning_id", new LaundryRule("w_drycleaning_id", "Аквачистка", R.drawable.w_drycleaning));
        laundryRules.put("p_drycleaning_id", new LaundryRule("p_drycleaning_id", "Химчистка с применением перхлорэтилена", R.drawable.p_drycleaning));
        laundryRules.put("f_drycleaning_id", new LaundryRule("f_drycleaning_id", "Химчистка с применением специальных растворителей", R.drawable.f_drycleaning));
        for(Map.Entry<String, LaundryRule> entry: laundryRules.entrySet()){
            database.laundryRuleDao().insert(entry.getValue()).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe();
        }
    }

    public static BirkinBoxApplication getInstance() {
        return instance;
    }

    public ClothesDatabase getDatabase() {
        return database;
    }
}
