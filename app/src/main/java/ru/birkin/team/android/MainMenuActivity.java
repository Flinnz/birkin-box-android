package ru.birkin.team.android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainMenuActivity extends AppCompatActivity {
    private final ArrayList<ClothesWithLaundryRules> clothesArrayList = new ArrayList<>();
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
        BirkinBoxApplication
                .getInstance()
                .getDatabase()
                .clothesWithLaundryRulesDao()
                .getClothesWithRules()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<ClothesWithLaundryRules>>() {
                    @Override
                    public void accept(List<ClothesWithLaundryRules> clothesWithLaundryRules) throws Throwable {
                        clothesArrayList.clear();
                        clothesArrayList.addAll(clothesWithLaundryRules);
                        clothesAdapter.notifyDataSetChanged();
                    }
                });
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
            String photoName = UUID.randomUUID().toString();
            String path = getFilesDir().getAbsolutePath() + '/' + photoName + ".png";
            Toast.makeText(this, path, Toast.LENGTH_LONG).show();
            try (FileOutputStream out = new FileOutputStream(path)) {
                clothesBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                Clothes newClothes = newClothesBuilder.setPhoto(new BitmapWithPath(clothesBitmap, path)).setName("Вещь").build();
                ClothesWithLaundryRules clw = new ClothesWithLaundryRules();
                clw.clothes = newClothes;
                clw.laundryRules = new ArrayList<>();
                clothesArrayList.add(clw);
                LaundryRule lr = new LaundryRule("wash_30", "Стирка при 30 градусов", R.drawable.wash_30);
                BirkinBoxApplication
                        .getInstance()
                        .getDatabase()
                        .laundryRuleDao()
                        .insert(lr)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Action() {
                            @Override
                            public void run() throws Throwable {
                                BirkinBoxApplication
                                        .getInstance()
                                        .getDatabase()
                                        .clothesDao()
                                        .insert(clw.clothes)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new Consumer<Long>() {
                                            @Override
                                            public void accept(Long aLong) throws Throwable {
                                                clw.laundryRules.add(lr);
                                                for (LaundryRule l : clw.laundryRules) {
                                                    ClothesLaundryRuleRef ref = new ClothesLaundryRuleRef();
                                                    ref.alias = l.alias;
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
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {

                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Ошибка добавления вещи", Toast.LENGTH_LONG).show();
            }
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