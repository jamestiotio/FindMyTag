package com.example.findmytag;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;

import java.net.URI;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MappingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MappingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MappingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MappingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MappingFragment newInstance(String param1, String param2) {
        MappingFragment fragment = new MappingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mapping, container, false);
    }

    //outside
    Button upload_btn;
    Marker mapping_floorplan_imgView;
    private final int activity_code = 2000;
    private Uri imgUri1 = null, imgUri2 = null;
    private TextView lvl1,lvl2;


    private boolean ready = false; // boolean to check if user uploaded image
    //---

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        upload_btn = view.findViewById(R.id.btn_upload);
        mapping_floorplan_imgView = view.findViewById(R.id.imgView_mapping_floorplan);
        lvl1 = view.findViewById(R.id.txtView_map_L1);
        lvl2 = view.findViewById(R.id.txtView_map_L2);

        //----------- Change level floorplan-----------------
        lvl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgUri1 != null){
                    mapping_floorplan_imgView.setImage(ImageSource.uri(imgUri1));
                }

            }
        });

        lvl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgUri2 != null){
                    mapping_floorplan_imgView.setImage(ImageSource.uri(imgUri2));
                }

            }
        });

        //--------Marker touch event------------
        GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if(mapping_floorplan_imgView.isReady() && ready){
                    PointF markerCoord = mapping_floorplan_imgView.viewToSourceCoord(e.getX(),e.getY());
                    mapping_floorplan_imgView.setPin(markerCoord);
                    Toast.makeText(getContext(),"x: " + markerCoord.x + " y: " + markerCoord.y,Toast.LENGTH_SHORT).show();
                }
                return true;
            }


//            @Override
//            public boolean onDown(MotionEvent e) {
//                if(marker.isReady()){
//                    marker.removePin();
//                    PointF markerCoord = marker.viewToSourceCoord(e.getX(),e.getY());
//                    marker.setPin(markerCoord);
//                }
//                return true;
//            }
        });

        mapping_floorplan_imgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
        //--------Marker touch event------------

        //------upload btn---------
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(),imgPopupWindow.class),activity_code);
            }
        });
        //------upload btn---------


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == activity_code){
            if (resultCode == Activity.RESULT_OK){
                String location_name = data.getStringExtra("locationName1");
                imgUri1 = Uri.parse(data.getExtras().getString("imgUri1"));
                mapping_floorplan_imgView.setImage(ImageSource.uri(imgUri1));
                imgUri2 = Uri.parse(data.getExtras().getString("imgUri2"));
                ready = true;
            }
        }
    }
}