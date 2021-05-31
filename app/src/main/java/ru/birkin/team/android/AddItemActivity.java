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