package com.example.findmytag;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.Bundle;

import com.example.findmytag.wifi.WiFiDataManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;




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
    public static float x;
    public static float y;
    String coord;
    List<ScanResult> rssi;
    ArrayList arrayList;
    private Context mcontext;
    private WiFiDataManager wifiDataManager;
    StringBuffer sb;

    //verifystoragePermissions verifystoragePermissions;
    String[] permissions = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };
    int requestCode = 200;



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
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1){
            requestPermissions(permissions, requestCode);

        }


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mcontext = getActivity();
        wifiDataManager = new WiFiDataManager(mcontext);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mapping, container, false);
    }

    //outside
    Button rssi_btn;
    Button upload_btn;
    Marker mapping_floorplan_imgView;
    private final int activity_code = 2000;
    private Uri imgUri1 = null, imgUri2 = null;
    private TextView lvl1,lvl2;
    String path, txt;



    private boolean ready = false; // boolean to check if user uploaded image
    public void onStart() {
        super.onStart();

//        if (isAdded()){
//            String rssi = getArguments().getString("rssi");
//
//        }
    }

    //---

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        upload_btn = view.findViewById(R.id.btn_upload);
        mapping_floorplan_imgView = view.findViewById(R.id.imgView_mapping_floorplan);
        lvl1 = view.findViewById(R.id.txtView_map_L1);
        lvl2 = view.findViewById(R.id.txtView_map_L2);
        rssi_btn= view.findViewById(R.id.btn_rssi);
        int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    getActivity(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }


        //------------upload data-------------
        rssi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String s=wifiDataManager.scanWifi();
                 ArrayList arrayList= new ArrayList(Arrays.asList(s.split("/n")));

                coord = ("("+x+","+y+")");//(x,y)
                HashMap txt=new HashMap();
                txt.put(coord,arrayList);
                System.out.println(txt);
                try {
//                    path = Environment.getExternalStorageDirectory().getAbsolutePath();
//                    File file = new File(path+"/text.txt");
//                    if(!file.exists()){
//                        try{
//                            //file.getParentFile().mkdirs();
////                            file.mkdirs();
//                            file.createNewFile();
//
//                        }catch (Exception e){
//                            //
//                        }
                    writeToFile(txt);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
//                    if(!file.exists()){
//                        Toast.makeText(mcontext, "fail", Toast.LENGTH_SHORT).show();
//                    }
//                    FileOutputStream outputStream=new FileOutputStream(path+"/text.txt");
//                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//                    objectOutputStream.writeObject(txt);
//                    new FileOutputStream(file).close();
                Toast.makeText(mcontext, "saved successfully", Toast.LENGTH_SHORT).show();


//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//
//                }
            }



        });

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
                    x=markerCoord.x;
                    y=markerCoord.y;
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

    private void writeToFile(HashMap map) {
        final String TAG = "MEDIA";

        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/download");

        dir.mkdirs();
        File file = new File(dir, "WiFiData.txt");

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println(map);
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "File not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //tv.append("\n\nFile written to "+file);


    }

}