package com.example.findmytag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.findmytag.wifi.WiFiActivity;

public class LocationActivity extends AppCompatActivity {

    Button btn_mappingfrag, btn_testingfrag, btn_listofwifi;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Bundle bundle=getIntent().getExtras();
//        String rssi=bundle.getString("rssi");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        btn_mappingfrag = findViewById(R.id.btn_mappingfrag);
        btn_testingfrag = findViewById(R.id.btn_testingfrag);
        btn_listofwifi = findViewById(R.id.listofwifi);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        MappingFragment mappingFragment = new MappingFragment();
//        bundle.putString("rssi", rssi);
//        mappingFragment.setArguments(bundle);
        fragmentTransaction.show(mappingFragment);
        fragmentTransaction.replace(R.id.frag_container, mappingFragment);
        fragmentTransaction.commit();

        btn_listofwifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(LocationActivity.this, WiFiActivity.class);
                startActivity(x);
            }
        });

        //MappingFragment mappingFragment = new MappingFragment();

        btn_mappingfrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.performClick();
                view.setPressed(true);
                btn_testingfrag.setPressed(false);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frag_container, mappingFragment);
                fragmentTransaction.commit();
                return true;
            }
        });

//        btn_mappingfrag.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                MappingFragment mappingFragment = new MappingFragment();
//                fragmentTransaction.replace(R.id.frag_container, mappingFragment);
//                fragmentTransaction.commit();
//
//            }
//        });
        TestingFragment testingFragment = new TestingFragment();
        btn_testingfrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.performClick();
                view.setPressed(true);
                btn_mappingfrag.setPressed(false);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                //TestingFragment testingFragment = new TestingFragment();
                fragmentTransaction.replace(R.id.frag_container, testingFragment);
                fragmentTransaction.commit();
                return true;
            }
        });

//        btn_testingfrag.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                TestingFragment testingFragment = new TestingFragment();
//                fragmentTransaction.replace(R.id.frag_container, testingFragment);
//                fragmentTransaction.commit();
//
//            }
//        });


    }
}