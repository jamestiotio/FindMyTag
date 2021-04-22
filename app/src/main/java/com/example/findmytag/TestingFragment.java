package com.example.findmytag;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.findmytag.algorithms.knn.KNN;
import com.example.findmytag.wifi.WiFiDataManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestingFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    FirebaseUser user;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    WifiManager wifiManager;
    public ArrayList<Integer> dataRssi = new ArrayList<>();
    public ArrayList<String>  dataBssid =new ArrayList<>();

    private Context mcontext;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TestingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestingFragment newInstance(String param1, String param2) {
        TestingFragment fragment = new TestingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private TextView L1,L2;
    private Marker F;
    private Spinner test_spinner;
    private Button test_btn;
    private static String select_algo = null;
    private Context context;
    private  KNN knn=new KNN();
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mcontext = getActivity();
        WiFiDataManager wifiDataManager = new WiFiDataManager(mcontext);
        test_btn = view.findViewById(R.id.btn_test);
        //spinner methods
        test_spinner = view.findViewById(R.id.test_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.algo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        test_spinner.setAdapter(adapter);
        test_spinner.setOnItemSelectedListener(this);
        //spinner methods

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //instantly put location image if exist
        if(fAuth.getCurrentUser() != null) {
            StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/l1.jpg");
            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .download(uri)
                            .into(new SimpleTarget<File>() {

                                @Override
                                public void onResourceReady(File resource, Transition<? super File> transition) {

                                    F.setImage(ImageSource.uri(resource.getAbsolutePath()));

                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    F.setImage(ImageSource.resource(R.drawable.floorplan_src));
                }
            });
        }

        //test_btn onclick listener
        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(select_algo.equals("Neural Network")){
                    Toast.makeText(getContext(),"Neural Network selected",Toast.LENGTH_SHORT).show();
                }
                else if(select_algo.equals("Random Forest")){
                    Toast.makeText(getContext(),"Random Forest selected",Toast.LENGTH_SHORT).show();
                }
                else if(select_algo.equals("KNN")){
                    Toast.makeText(getContext(),"KNN selected",Toast.LENGTH_SHORT).show();

                    HashMap sortteddata=wifiDataManager.userScanWifi();
                    System.out.println(sortteddata);
                }
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                   Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_testing, container, false);

        L1 = view.findViewById(R.id.txtView_map_L1);
        L2 = view.findViewById(R.id.txtView_map_L2);
        F = view.findViewById(R.id.imgView_testing_floorplan);
        L1.setClickable(true);
        L2.setClickable(true);
        L1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fAuth.getCurrentUser() != null) {
                    StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/l1.jpg");
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(getContext())
                                    .download(uri)
                                    .into(new SimpleTarget<File>() {
//                                        @Override
//                                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                                            super.onLoadFailed(errorDrawable);
//                                            Log.d("Failed to load");
//                                        }

                                        @Override
                                        public void onResourceReady(File resource, Transition<? super File> transition) {

                                            F.setImage(ImageSource.uri(resource.getAbsolutePath()));

                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"No image found", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });
        L2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fAuth.getCurrentUser() != null) {
                    StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/l2.jpg");
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(getContext())
                                    .download(uri)
                                    .into(new SimpleTarget<File>() {
//                                        @Override
//                                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                                            super.onLoadFailed(errorDrawable);
//                                            Log.d("Failed to load");
//                                        }

                                        @Override
                                        public void onResourceReady(File resource, Transition<? super File> transition) {
                                            F.setImage(ImageSource.uri(resource.getAbsolutePath()));
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"No image found", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        // Inflate the layout for this fragment//return inflater.inflate(R.layout.fragment_testing, container, false);
        return view;



    }


    //btm 2 methods are for spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.test_spinner){
            select_algo = adapterView.getItemAtPosition(i).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}