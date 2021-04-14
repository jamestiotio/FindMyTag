package com.example.findmytag;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        test_btn = view.findViewById(R.id.btn_test);
        //spinner methods
        test_spinner = view.findViewById(R.id.test_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.algo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        test_spinner.setAdapter(adapter);
        test_spinner.setOnItemSelectedListener(this);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

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
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        L1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //F.setImageResource(R.drawable.floorplan1);
                //F.setImage(ImageSource.resource(R.drawable.floorplan1));
                if(fAuth.getCurrentUser() != null) {
                    StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/l1.jpg");
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //System.out.println(uri);
//                            Bitmap bitmap = BitmapFactory.decodeFile(uri.toString());
//                            F.setImage(ImageSource.bitmap(bitmap));
                               Glide.with(F.getContext()).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    F.setImage(ImageSource.bitmap(resource));
                                }

//                                @Override
//                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                                    view.setImage(ImageSource.bitmap(resource));
//                                }
//                            });
//                            Bitmap bitmap = Glide.with(getActivity()).asBitmap().load(uri).into(-1,-1);
//

                            });
                        }
                    });
                }
            }
        });
        L2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //F.setImageResource(R.drawable.floorplan2);
                if(fAuth.getCurrentUser() != null) {
                    StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/l2.jpg");
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //System.out.println(uri);
//                            Bitmap bitmap = BitmapFactory.decodeFile(uri.toString());
//                            F.setImage(ImageSource.bitmap(bitmap));
                            Glide.with(F.getContext()).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    F.setImage(ImageSource.bitmap(resource));
                                }

//                                @Override
//                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                                    view.setImage(ImageSource.bitmap(resource));
//                                }
//                            });
//                            Bitmap bitmap = Glide.with(getActivity()).asBitmap().load(uri).into(-1,-1);
//

                            });
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