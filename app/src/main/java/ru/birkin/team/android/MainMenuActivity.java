package ru.birkin.team.android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainMenuActivity extends AppCompatActivity {
    private final ArrayList<Clothes> clothesArrayList = new ArrayList<>();
    private ClothesAdapter clothesAdapter;
    private static final int REQUEST_LABEL_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CLOTHES_IMAGE_CAPTURE = 2;

    private static final MediaType MEDIA_TYPE_PNG = MediaType.get("image/png");
    private final ClothesBuilder newClothesBuilder = new ClothesBuilder();
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        RecyclerView recyclerView = findViewById(R.id.clothes_list);
        clothesAdapter = new ClothesAdapter(LayoutInflater.from(this), clothesArrayList);
        recyclerView.setAdapter(clothesAdapter);
    }

    public void addClothesButtonOnClick(View view) {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePhotoIntent, REQUEST_LABEL_IMAGE_CAPTURE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LABEL_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            sendImage(outputStream.toByteArray());
            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CLOTHES_IMAGE_CAPTURE);
        } else if (requestCode == REQUEST_CLOTHES_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap clothesBitmap = (Bitmap) extras.get("data");
            Clothes newClothes = newClothesBuilder.setPhoto(clothesBitmap).setName("Вещь").build();
            clothesArrayList.add(newClothes);
            clothesAdapter.notifyItemInserted(clothesArrayList.size()-1);
        }
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
                        newClothesBuilder.setDescription(response.body().string());
                    } catch (Exception e) {
                        textView.setText(e.getMessage());
                    }
                });
            }
        });
    }
}