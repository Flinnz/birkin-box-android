package ru.birkin.team.android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
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

public class AddItemActivity extends AppCompatActivity {
    private ClothesAdapter clothesAdapter;
    private static final int REQUEST_LABEL_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CLOTHES_IMAGE_CAPTURE = 2;
    private ImageView labelImageView;
    private ImageView clothesImageView;
    private EditText nameEditText;
    private ImageButton nextButton;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.get("image/png");
    private final ClothesBuilder newClothesBuilder = new ClothesBuilder();
    private final OkHttpClient client = new OkHttpClient();
    private final HashMap<String, LaundryRule> laundryRules = new HashMap<>();
    private final ArrayList<LaundryRule> currentLaundryRules = new ArrayList<>();
    private BitmapWithPath labelPhotoWithPath;
    private BitmapWithPath clothesPhotoWithPath;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        labelImageView = (ImageView)findViewById(R.id.birk_pic);
        labelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClothesButtonOnClick(v, REQUEST_LABEL_IMAGE_CAPTURE);
            }
        });
        clothesImageView = (ImageView) findViewById(R.id.item_pic);
        clothesImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClothesButtonOnClick(v, REQUEST_CLOTHES_IMAGE_CAPTURE);
            }
        });
        nameEditText = (EditText)findViewById(R.id.item_name);
        nextButton = (ImageButton) findViewById(R.id.add_check);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.isEmpty() || labelPhotoWithPath == null || clothesPhotoWithPath == null) {
                    showButton();
                    return;
                }

                Intent intent = new Intent(v.getContext(), manual_decryption.class);
                getFinalBundle(intent);
                v.getContext().startActivity(intent);
            }
        });
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0) {
                    name = s.toString();
                } else {
                    name = "";
                }
                showButton();
            }
        });
        showButton();
    }

    public void showButton() {
        if (name != null && !name.isEmpty() && labelPhotoWithPath != null && clothesPhotoWithPath != null) {
            nextButton.setVisibility(View.VISIBLE);
        } else {
            nextButton.setVisibility(View.GONE);
        }
    }

    public void addClothesButtonOnClick(View view, int result) {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePhotoIntent, result);
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
            String photoName = UUID.randomUUID().toString();
            String path = getFilesDir().getAbsolutePath() + '/' + photoName + ".png";
            labelPhotoWithPath = new BitmapWithPath(imageBitmap, path);
            labelImageView.setImageBitmap(imageBitmap);
            labelImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            showButton();
        } else if (requestCode == REQUEST_CLOTHES_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap clothesBitmap = (Bitmap) extras.get("data");
            String photoName = UUID.randomUUID().toString();
            String path = getFilesDir().getAbsolutePath() + '/' + photoName + ".png";
            clothesPhotoWithPath = new BitmapWithPath(clothesBitmap, path);
            clothesImageView.setImageBitmap(clothesBitmap);
            clothesImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            showButton();
        }
    }

    private void getFinalBundle(Intent intent) {
        String labelPhotoPath = savePhoto(this.labelPhotoWithPath);
        String clothesPhotoPath = savePhoto(this.clothesPhotoWithPath);
        String name = this.name;
        intent.putExtra("labelPhotoPath", labelPhotoPath);
        intent.putExtra("clothesPhotoPath", clothesPhotoPath);
        intent.putExtra("name", name);
    }

    private String savePhoto(BitmapWithPath photo) {
        try (FileOutputStream out = new FileOutputStream(photo.path)) {
            photo.bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка добавления вещи", Toast.LENGTH_LONG).show();
            return "";
        }
        return photo.path;
    }

    private void sendImage(byte[] imageBytes) {
        //final TextView textView = findViewById(R.id.responseTextView);
        String url = "http://192.168.0.27:5000/api/decodeLaundryTag";
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
                runOnUiThread(e::printStackTrace);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String desc = response.body().string();
                runOnUiThread(() -> {
                    try {
                        JSONObject jobject = new JSONObject(desc);
                        newClothesBuilder.setDescription(desc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}