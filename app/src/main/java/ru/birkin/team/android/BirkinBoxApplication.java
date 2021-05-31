package ru.birkin.team.android;

import android.app.Application;

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
        laundryRules.put("wash_30", new LaundryRule("wash_30", "Стирка при 30 градусов", R.drawable.wash_30));
        laundryRules.put("wash_40", new LaundryRule("wash_40", "Стирка при 40 градусов", R.drawable.wash_40));
        laundryRules.put("wash_50", new LaundryRule("wash_50", "Стирка при 50 градусов", R.drawable.wash_50));
        laundryRules.put("wash_60", new LaundryRule("wash_60", "Стирка при 60 градусов", R.drawable.wash_60));
        laundryRules.put("wash_70", new LaundryRule("wash_70", "Стирка при 70 градусов", R.drawable.wash_70));
        laundryRules.put("wash_95", new LaundryRule("wash_95", "Стирка при 95 градусов", R.drawable.wash_95));
        laundryRules.put("hand_wash", new LaundryRule("hand_wash", "Не стирать", R.drawable.hand_wash));
        laundryRules.put("dont_wash", new LaundryRule("dont_wash", "Ручная стирка", R.drawable.dont_wash));
        laundryRules.put("any_bleach", new LaundryRule("any_bleach", "Любое отбеливание", R.drawable.any_bleach));
        laundryRules.put("dont_bleach", new LaundryRule("dont_bleach", "Не отбеливать", R.drawable.dont_bleach));
        laundryRules.put("only_oxygen_bleach", new LaundryRule("only_oxygen_bleach", "Только кислородное отбеливание", R.drawable.only_oxygen_bleach));
        laundryRules.put("lower_tumble", new LaundryRule("lower_tumble", "Сушка до 60 градусов", R.drawable.lower_tumble));
        laundryRules.put("normal_tumble", new LaundryRule("normal_tumble", "Сушка до 80 градусов", R.drawable.normal_tumble));
        laundryRules.put("dont_tumble", new LaundryRule("dont_tumble", "Сушка запрещена", R.drawable.dont_tumble));
        laundryRules.put("low_temperature_ironing", new LaundryRule("low_temperature_ironing", "Гладить при низкой температуре", R.drawable.low_temperature_ironing));
        laundryRules.put("medium_temperature_ironing", new LaundryRule("medium_temperature_ironing", "Гладить при средней температуре", R.drawable.medium_temperature_ironing));
        laundryRules.put("max_temperature_ironing", new LaundryRule("max_temperature_ironing", "Гладить при максимальной температуре", R.drawable.max_temperature_ironing));
        laundryRules.put("do_not_iron", new LaundryRule("do_not_iron", "Не гладить", R.drawable.do_not_iron));
        laundryRules.put("drip_flat_drying", new LaundryRule("drip_flat_drying", "Горизонтальная сушка с отжимом", R.drawable.drip_flat_drying));
        laundryRules.put("drip_line_drying", new LaundryRule("drip_line_drying", "Вертикальная сушка с отжимом", R.drawable.drip_line_drying));
        laundryRules.put("flat_drying", new LaundryRule("flat_drying", "Горизонтальная сушка без отжима", R.drawable.flat_drying));
        laundryRules.put("line_drying", new LaundryRule("line_drying", "Вертикальная сушка без отжима", R.drawable.line_drying));
        laundryRules.put("professional_wet_clean", new LaundryRule("professional_wet_clean", "Аквачистка", R.drawable.professional_wet_clean));
        laundryRules.put("do_not_wet_clean", new LaundryRule("do_not_wet_clean", "Аквачистка запрещена", R.drawable.do_not_wet_clean));
        laundryRules.put("professional_tetrachloroethene_dry_clean", new LaundryRule("professional_tetrachloroethene_dry_clean", "Химчистка с применением перхлорэтилена", R.drawable.professional_tetrachloroethene_dry_clean));
        laundryRules.put("hydrocarbons_dry_clean", new LaundryRule("hydrocarbons_dry_clean", "Химчистка с применением специальных растворителей", R.drawable.hydrocarbons_dry_clean));
        laundryRules.put("do_not_dry_clean", new LaundryRule("do_not_dry_clean", "Химчистка запрещена", R.drawable.do_not_dry_clean));
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
