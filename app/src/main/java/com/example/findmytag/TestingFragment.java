package com.example.findmytag;

import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.findmytag.algorithms.knn.KNN;
import com.example.findmytag.algorithms.neuralnetwork.CNNLocUtils;
import com.example.findmytag.algorithms.neuralnetwork.NeuralNetwork;
import com.example.findmytag.algorithms.neuralnetwork.WiFiAPBSSIDAndSSIDList;
import com.example.findmytag.algorithms.randomforest.ResultGenerator;
import com.example.findmytag.utils.DataParser;
import com.example.findmytag.wifi.WiFiDataManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.primitives.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestingFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static float x;
    public static float y;
    private static int floorLvl = 1;
    private static String select_algo = null;
    private static float imgWidth = -1;
    private static float imgHeight = -1;
    public ArrayList<Integer> dataRssi = new ArrayList<>();
    public ArrayList<String> dataBssid = new ArrayList<>();
    public HashMap hashMap = new HashMap() {
    };
    FirebaseUser user;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    WifiManager wifiManager;
    KNN knn = new KNN();
    String coord;
    String s;
    private WiFiDataManager wiFiDataManager = new WiFiDataManager(wifiManager, dataBssid, dataRssi);
    private Context mcontext;
    private WiFiDataManager wifiDataManager;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView L1, L2;
    private Marker F;
    private Spinner test_spinner;
    private Button test_btn;
    private Context context;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mcontext = getActivity();
        wifiDataManager = new WiFiDataManager(mcontext);

        test_btn = view.findViewById(R.id.btn_test);
        //spinner methods
        test_spinner = view.findViewById(R.id.test_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.algo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        test_spinner.setAdapter(adapter);
        test_spinner.setOnItemSelectedListener(this);
        //spinner methods

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //instantly put location image if exist
        if (fAuth.getCurrentUser() != null) {
            StorageReference fileRef =
                    storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "/l1.jpg");
            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .download(uri)
                            .into(new SimpleTarget<File>() {
//                                        @Override
//                                        public void onLoadFailed(@Nullable Drawable
//                                        errorDrawable) {
//                                            super.onLoadFailed(errorDrawable);
//                                            Log.d("Failed to load");
//                                        }

                                @Override
                                public void onResourceReady(File resource, Transition<?
                                        super File> transition) {
                                    //mPlaceHolder.setVisibility(GONE);
                                    // ImageViewState three parameters are: scale, center,
                                    // orientation
                                    // subsamplingScaleImageView.setImage(ImageSource.uri(Uri
                                    // .fromFile(file)),new ImageViewState(1.0f, new PointF(0, 0)
                                    // , 0));
                                    //
                                    F.setImage(ImageSource.uri(resource.getAbsolutePath()));
                                    // display the largest proportion
                                    //F.setMaxScale(10f);

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
                PointF imgSourceCoords = F.viewToSourceCoord(F.getWidth(), F.getHeight());
                imgWidth = imgSourceCoords.x;
                imgHeight = imgSourceCoords.y;
                if (select_algo.equals("Neural Network")) {
                    Toast.makeText(getContext(), "Neural Network selected", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(),
                            "Image Width: " + imgWidth + " Image Height: " + imgHeight,
                            Toast.LENGTH_SHORT).show();
                    String TAG = "MEDIA";

                    // Scan Wi-Fi and put in file
                    String s = wifiDataManager.scanWifi();
                    ArrayList arrayList = new ArrayList(Arrays.asList(s.split("/n")));

                    coord = ("(" + x + "," + y + "," + floorLvl + ")");    // (x,y) (this is unused)
                    hashMap.put(arrayList, coord);

                    String pathName =
                            android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/nn";
                    File dir = new File(pathName + "/TestData");
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File file = new File(dir, "/TestLocation.txt");

                    try {
                        FileOutputStream f = new FileOutputStream(file);
                        PrintWriter pw = new PrintWriter(f);
                        pw.println(hashMap);
                        pw.flush();
                        pw.close();
                        f.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Log.i(TAG, "File not found.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //parse the file and save to csv
                    DataParser o = new DataParser();
                    try {
                        o.readFile(file);
                        ResultGenerator.addDataToCSVWithoutCoord(o.getBSSID(), o.getLevels(),
                                pathName + "/TestResult.csv");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Load the saved correlation vectors and assign them to the class
                    try {
                        // Initialize the pre-trained models for prediction
                        NeuralNetwork nn = new NeuralNetwork("trained_x_model.zip",
                                "trained_y_model.zip");
                        INDArray xCorrelationVector = Nd4j.readBinary(new File(
                                "xCorrelationVector.bin"));
                        INDArray yCorrelationVector = Nd4j.readBinary(new File(
                                "yCorrelationVector.bin"));
                        nn.xCorrelationVector = xCorrelationVector;
                        nn.yCorrelationVector = yCorrelationVector;
                        // Process input and convert it to image representation
                        INDArray rawInputFingerprint = CNNLocUtils.parseTestingCSV(pathName +
                                "/TestResult.csv");
                        int upperbound =
                                (int) Math.ceil(Math.sqrt(WiFiAPBSSIDAndSSIDList.KNOWN_WIFI_BSSID_LIST.size()));
                        float[] r = rawInputFingerprint.getRow(0).toFloatVector();
                        INDArray floatR = Nd4j.create(r, new int[]{1,
                                WiFiAPBSSIDAndSSIDList.KNOWN_WIFI_BSSID_LIST.size()});
                        INDArray xHP = CNNLocUtils.getHP(floatR, nn.xCorrelationVector);
                        INDArray yHP = CNNLocUtils.getHP(floatR, nn.yCorrelationVector);
                        INDArray xImage = CNNLocUtils.imageFromHPINDArray(xHP).reshape(1, 1,
                                upperbound, upperbound);
                        INDArray yImage = CNNLocUtils.imageFromHPINDArray(yHP).reshape(1, 1,
                                upperbound, upperbound);

                        // Get prediction
                        Pair<Integer, Integer> predictedCoordPercentages = nn.predict(xImage,
                                yImage);
                        float actualXCoordinate =
                                predictedCoordPercentages.getFirst().floatValue() * imgWidth;
                        float actualYCoordinate =
                                predictedCoordPercentages.getSecond().floatValue() * imgWidth;
                        PointF markerPoint = new PointF(actualXCoordinate, actualYCoordinate);
                        F.setPin(markerPoint);
                        Toast.makeText(getContext(), "(Prediction) x: " + actualXCoordinate + " " +
                                "y: " + actualYCoordinate, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(getContext(), "Failed to predict your current location! " +
                                "Did you remember to map and train the model first?",
                                Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else if (select_algo.equals("Random Forest")) {
                    Toast.makeText(getContext(), "Random Forest selected", Toast.LENGTH_SHORT).show();
                } else if (select_algo.equals("KNN")) {
                    Toast.makeText(getContext(), "KNN selected", Toast.LENGTH_SHORT).show();
                    // dataBssid=wiFiDataManager.dataBssid;

                    // System.out.println(wifiDataManager.dataBssid);
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
                //F.setImageResource(R.drawable.floorplan1);
                //F.setImage(ImageSource.resource(R.drawable.floorplan1));
                if (fAuth.getCurrentUser() != null) {

                    StorageReference fileRef =
                            storageReference.child("users/" + fAuth.getCurrentUser().getUid() +
                                    "/l1.jpg");
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(getContext())
                                    .download(uri)
                                    .into(new SimpleTarget<File>() {
//                                        @Override
//                                        public void onLoadFailed(@Nullable Drawable
//                                        errorDrawable) {
//                                            super.onLoadFailed(errorDrawable);
//                                            Log.d("Failed to load");
//                                        }

                                        @Override
                                        public void onResourceReady(File resource, Transition<?
                                                super File> transition) {
                                            //mPlaceHolder.setVisibility(GONE);
                                            // ImageViewState three parameters are: scale,
                                            // center, orientation
                                            // subsamplingScaleImageView.setImage(ImageSource.uri
                                            // (Uri.fromFile(file)),new ImageViewState(1.0f, new
                                            // PointF(0, 0), 0));
                                            //
                                            F.setImage(ImageSource.uri(resource.getAbsolutePath()));
                                            // display the largest proportion
                                            //F.setMaxScale(10f);
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "No image found", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });
        L2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //F.setImageResource(R.drawable.floorplan2);
                if (fAuth.getCurrentUser() != null) {
                    StorageReference fileRef =
                            storageReference.child("users/" + fAuth.getCurrentUser().getUid() +
                                    "/l2.jpg");
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(getContext())
                                    .download(uri)
                                    .into(new SimpleTarget<File>() {
//                                        @Override
//                                        public void onLoadFailed(@Nullable Drawable
//                                        errorDrawable) {
//                                            super.onLoadFailed(errorDrawable);
//                                            Log.d("Failed to load");
//                                        }

                                        @Override
                                        public void onResourceReady(File resource, Transition<?
                                                super File> transition) {
                                            //mPlaceHolder.setVisibility(GONE);
                                            // ImageViewState three parameters are: scale,
                                            // center, orientation
                                            // subsamplingScaleImageView.setImage(ImageSource.uri
                                            // (Uri.fromFile(file)),new ImageViewState(1.0f, new
                                            // PointF(0, 0), 0));
                                            //
                                            F.setImage(ImageSource.uri(resource.getAbsolutePath()));
                                            // display the largest proportion
                                            //F.setMaxScale(10f);
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "No image found", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        // Inflate the layout for this fragment//return inflater.inflate(R.layout
        // .fragment_testing, container, false);
        return view;


    }


    //btm 2 methods are for spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.test_spinner) {
            select_algo = adapterView.getItemAtPosition(i).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}