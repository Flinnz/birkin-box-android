package ru.birkin.team.android;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddItemActivity extends AppCompatActivity {
    private ClothesAdapter clothesAdapter;
    private static final int REQUEST_LABEL_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CLOTHES_IMAGE_CAPTURE = 2;

    private static final MediaType MEDIA_TYPE_PNG = MediaType.get("image/png");
    private final ClothesBuilder newClothesBuilder = new ClothesBuilder();
    private final OkHttpClient client = new OkHttpClient();
    private final HashMap<String, LaundryRule> laundryRules = new HashMap<>();
    private final ArrayList<LaundryRule> currentLaundryRules = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
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
    }

    private void sendImage(byte[] imageBytes) {
        final TextView textView = findViewById(R.id.responseTextView);
        String url = "http://185.246.66.34:8081/api/decodeLaundryTag";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", UUID.randomUUID().toString() + ".png",
                        RequestBody.create(
                                imageBytes,
                                MEDIA_TYPE_PNG))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> textView.setText(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(() -> {
                    try {
                        String desc = response.body().string();
                        JSONObject jobject = new JSONObject(desc);
                        newClothesBuilder.setDescription(desc);
                    } catch (Exception e) {
                        textView.setText(e.getMessage());
                    }
                });
            }
        });
    }
}