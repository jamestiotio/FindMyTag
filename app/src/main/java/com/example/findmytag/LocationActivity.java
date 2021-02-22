package com.example.findmytag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LocationActivity extends AppCompatActivity {

    Button btn_mappingfrag, btn_testingfrag;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        btn_mappingfrag = findViewById(R.id.btn_mappingfrag);
        btn_testingfrag = findViewById(R.id.btn_testingfrag);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        MappingFragment mappingFragment = new MappingFragment();
        fragmentTransaction.replace(R.id.frag_container, mappingFragment);
        fragmentTransaction.commit();

        btn_mappingfrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                MappingFragment mappingFragment = new MappingFragment();
                fragmentTransaction.replace(R.id.frag_container, mappingFragment);
                fragmentTransaction.commit();

            }
        });

        btn_testingfrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                TestingFragment testingFragment = new TestingFragment();
                fragmentTransaction.replace(R.id.frag_container, testingFragment);
                fragmentTransaction.commit();

            }
        });


    }
}