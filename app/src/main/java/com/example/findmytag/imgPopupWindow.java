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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;

public class imgPopupWindow extends AppCompatActivity {
    private static final int PERMISSION_CODE = 1001;
    private Button inner_upload_btn, inner_upload_btn2, inner_upload_confirm, inner_upload_cancel;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int IMAGE_PICK_CODE2 = 1100;
    private ImageView inner_imgView, inner_imgView2;
    private EditText inner_edtTxt, inner_edtTxt2;
    private Uri imgUri = null;
    private Uri imgUri2 = null;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_popup_window);

        inner_upload_btn = findViewById(R.id.uploadPopup_upload_btn);
        inner_imgView = findViewById(R.id.uploadPopup_upload_imgView);
        inner_edtTxt = findViewById(R.id.uploadPopup_upload_locationName);
        inner_upload_confirm = findViewById(R.id.uploadPopup_upload_btn_confirm);
        inner_upload_cancel = findViewById(R.id.uploadPopup_upload_btn_cancel);

        inner_upload_btn2 = findViewById(R.id.uploadPopup_upload_btn2);
        inner_imgView2 = findViewById(R.id.uploadPopup_upload_imgView2);
        inner_edtTxt2 = findViewById(R.id.uploadPopup_upload_locationName2);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();


        //--------Btn confirm------------------
            inner_upload_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),LocationActivity.class);
                    if (inner_edtTxt.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please fill in Location 1 Name", Toast.LENGTH_SHORT).show();
                    }
                    else if (imgUri == null){
                        Toast.makeText(getApplicationContext(), "Please upload Location 1 Floor Plan", Toast.LENGTH_SHORT).show();
                    }
                    else if (inner_edtTxt2.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please fill in Location 2 Name", Toast.LENGTH_SHORT).show();
                    }
                    else if (imgUri2 == null){
                        Toast.makeText(getApplicationContext(), "Please upload Location 2 Floor Plan", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        intent.putExtra("locationName1", inner_edtTxt.getText().toString());
                        intent.putExtra("imgUri1", imgUri.toString());
                        intent.putExtra("locationName2", inner_edtTxt2.getText().toString());
                        intent.putExtra("imgUri2", imgUri2.toString());
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

        // ---------Upload image 2--------------
        inner_upload_btn2.setOnClickListener(new View.OnClickListener() {
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
                        pickImageFromGallery2();
                    }
                }
                else{
                    pickImageFromGallery2();
                }
            }
        });
        //---------Upload image 2--------------


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
    //----------upload img 2---------
    private void pickImageFromGallery2() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE2);
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
    private void uploadImageToFirebase(Uri imageUri) {
        // uplaod image to firebase storage
        final StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+inner_edtTxt+".jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(inner_imgView);
                        Picasso.get().load(uri).into(inner_imgView2);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            //set image to image view
            inner_imgView.setImageURI(data.getData());
            imgUri = data.getData();
            uploadImageToFirebase(imgUri);
            //inner_imgView.setImage(ImageSource.uri(data.getData()));
            //ready = true;
        }
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE2){
            //set image to image view
            inner_imgView2.setImageURI(data.getData());
            imgUri2 = data.getData();
            uploadImageToFirebase(imgUri2);
            //inner_imgView.setImage(ImageSource.uri(data.getData()));
            //ready = true;
        }
    }

    //---------upload img-----------
}