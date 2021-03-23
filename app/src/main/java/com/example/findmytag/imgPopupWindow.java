package com.example.findmytag;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.findmytag.R;

import java.net.URI;

public class imgPopupWindow extends AppCompatActivity {
    private static final int PERMISSION_CODE = 1001;
    private Button inner_upload_btn, inner_upload_confirm, inner_upload_cancel;
    private static final int IMAGE_PICK_CODE = 1000;
    private ImageView inner_imgView;
    private EditText inner_edtTxt;
    private Uri imgUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_popup_window);

        inner_upload_btn = findViewById(R.id.uploadPopup_upload_btn);
        inner_imgView = findViewById(R.id.uploadPopup_upload_imgView);
        inner_edtTxt = findViewById(R.id.uploadPopup_upload_locationName);
        inner_upload_confirm = findViewById(R.id.uploadPopup_upload_btn_confirm);
        inner_upload_cancel = findViewById(R.id.uploadPopup_upload_btn_cancel);


        //--------Btn confirm------------------
            inner_upload_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),LocationActivity.class);
                    if (inner_edtTxt.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please fill in Location Name", Toast.LENGTH_SHORT).show();
                    }
                    else if (imgUri == null){
                        Toast.makeText(getApplicationContext(), "Please upload Location Floor Plan", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        intent.putExtra("locationName", inner_edtTxt.getText().toString());
                        intent.putExtra("imgUri", imgUri.toString());
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }



                }
            });
        //--------Btn confirm------------------

        //--------Btn cancel-------------------
            inner_upload_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_CANCELED, intent);
                    finish();
                }
            });
        //--------Btn cancel-------------------

        // ---------Upload image--------------
        inner_upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check runtime permission
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        //permission not granted, request it.
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for permission
                        requestPermissions(permissions, PERMISSION_CODE);

                    }
                    else {
                        //permission already granted
                        pickImageFromGallery();
                    }
                }
                else{
                    pickImageFromGallery();
                }
            }
        });
        //---------Upload image--------------


        //--------pop up stuff-----------------

        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        }

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.83), (int) (height * 0.85));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.3f;

        getWindow().setAttributes(params);
        //--------pop up stuff-----------------
    }


    //---------upload img-----------
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            //set image to image view
            inner_imgView.setImageURI(data.getData());
            imgUri = data.getData();
            //inner_imgView.setImage(ImageSource.uri(data.getData()));
            //ready = true;
        }
    }

    //---------upload img-----------
}