package daniel.rad.radiotabsdrawer.admin.programsManager;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.login.User;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramsManagerOptionsFragment extends Fragment {

    TextView tvManagerProgramName;
    ImageView ivAddStudent;
    ImageView ivAddPicFromFB;
    ImageView ivEditPic;
    EditText etSearchUser;
    ImageView ivUserSearch;
    TextView tvAddStudent;
    TextView tvAddPicFromFB;
    RecyclerView rvChosenStudent;
    RecyclerView rvStudentsList;
    public ImageView ivProgramImage;
    ProgressBar pbAllUsers;
    ProgressBar pbChosenStudents;
    ProgressBar pbLoadingPic;

    static String imgDecodableString;

    boolean isButtonOnePressed = true;
    boolean isButtonTwoPressed = false;

    static String cameraCheckedFilePath;

    private DatabaseReference users =
            FirebaseDatabase.getInstance()
                    .getReference("Users");

    private DatabaseReference broadcastingUsers =
            FirebaseDatabase.getInstance()
                    .getReference("BroadcastingUsers");

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    List<User> allUsers = new ArrayList<>();
    List<User> broadcastUsers = new ArrayList<>();

    boolean isLoaded = false;
    static boolean uploadNeeded = false;

    DialogFragment newFragment;

    ProgramsData program;

    public ProgramsManagerOptionsFragment() {
        // Required empty public constructor
    }

    public static ProgramsManagerOptionsFragment newInstance(ProgramsData programsData) {
        Bundle args = new Bundle();
        args.putParcelable("model", programsData);
        ProgramsManagerOptionsFragment fragment = new ProgramsManagerOptionsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_programs_manager_options, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        program =  getArguments().getParcelable("model");

        tvManagerProgramName = view.findViewById(R.id.tvManagerProgramName);
        tvManagerProgramName.setText(program.getProgramName());
        tvManagerProgramName.setSelected(true);

        ivAddStudent = view.findViewById(R.id.ivAddStudent);
        ivAddPicFromFB = view.findViewById(R.id.ivAddPicFromFB);
        ivEditPic = view.findViewById(R.id.ivEditPic);
        ivProgramImage = view.findViewById(R.id.ivProgramImage);
        rvChosenStudent = view.findViewById(R.id.rvChosenStudent);
        rvStudentsList = view.findViewById(R.id.rvStudentsList);
        etSearchUser = view.findViewById(R.id.etSearchUser);
        ivUserSearch = view.findViewById(R.id.ivUserSearch);
        tvAddStudent = view.findViewById(R.id.tvAddStudent);
        tvAddPicFromFB = view.findViewById(R.id.tvAddPicFromFB);
        pbAllUsers = view.findViewById(R.id.pbAllUsers);
        pbChosenStudents = view.findViewById(R.id.pbChosenStudents);
        pbLoadingPic = view.findViewById(R.id.pbLoadingPic);

        ivAddStudent.setOnClickListener(v -> {
            if (!isButtonOnePressed){
                ivAddStudent.setImageResource(R.drawable.ic_search_bar);
                tvAddStudent.setTextColor(Color.parseColor("#FFF"));
                isButtonOnePressed = true;
                ivAddPicFromFB.setImageResource(R.drawable.ic_search_bar_unpressed);
                tvAddPicFromFB.setTextColor(Color.argb(100,206,204,204));
                isButtonTwoPressed = false;
            }
        });

        ivAddPicFromFB.setOnClickListener(v -> {
            if (!isButtonTwoPressed){
                ivAddPicFromFB.setImageResource(R.drawable.ic_search_bar);
                tvAddPicFromFB.setTextColor(Color.parseColor("#FFF"));
                isButtonTwoPressed = true;
                ivAddStudent.setImageResource(R.drawable.ic_search_bar_unpressed);
                tvAddStudent.setTextColor(Color.argb(100,206,204,204));
                isButtonOnePressed = false;
            }
        });

        ivEditPic.setOnClickListener(v -> {
            showDialog();
        });

        ivProgramImage.setOnClickListener(v -> {
            showZoomedDialog();
        });

        pbAllUsers.setVisibility(View.VISIBLE);
        pbChosenStudents.setVisibility(View.VISIBLE);

        ProgramsManagerUsersListAdapter adapter = new ProgramsManagerUsersListAdapter(allUsers, program, getContext());

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    allUsers.add(child.getValue(User.class));
                }
                pbAllUsers.setVisibility(View.INVISIBLE);
                rvStudentsList.setLayoutManager(new LinearLayoutManager(getContext()));
                rvStudentsList.setAdapter(adapter);

                broadcastingUsers.child(program.getVodId()).
                        addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                broadcastUsers.clear();
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    broadcastUsers.add(child.getValue(User.class));
                                }
                                pbChosenStudents.setVisibility(View.INVISIBLE);
                                rvChosenStudent.setLayoutManager(new LinearLayoutManager(getContext()));
                                rvChosenStudent.setAdapter(new ProgramsManagerChosenUsersListAdapter(broadcastUsers,program,getContext()));
                                isLoaded = true;
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ivUserSearch.setOnClickListener((v)->{
            if (isLoaded) {
                ArrayList<User> newList = new ArrayList<>();
                String search = etSearchUser.getText().toString().trim().toLowerCase();
                for (User user : allUsers) {
                    if (user.getUsername().toLowerCase().contains(search)){
                        newList.add(user);
                    }
                }
                if (!search.isEmpty()) {
                    ProgramsManagerUsersListAdapter usersListAdapter = new ProgramsManagerUsersListAdapter(newList, program, getContext());
                    rvStudentsList.setAdapter(usersListAdapter);
                }
            }
        });

        etSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(etSearchUser.getText().toString().isEmpty()){
                    rvStudentsList.setAdapter(adapter);
                }
            }
        });
    }

    private void showDialog() {
        // Create the fragment and show it as a dialog.
        newFragment = PictureDialogFragment.newInstance();
        newFragment.show(getFragmentManager(), "dialog");
    }

    private void showZoomedDialog(){
        ZoomedPictureDialogFragment zoomedDialog = ZoomedPictureDialogFragment.newInstance();
        Bundle bundle = new Bundle();
        if (imgDecodableString != null){
            bundle.putString("uriPic",imgDecodableString);
        }
        else if (cameraCheckedFilePath != null){
            bundle.putString("uriPic",cameraCheckedFilePath);
        }
        bundle.putParcelable("program",program);
        zoomedDialog.setArguments(bundle);
        zoomedDialog.show(getFragmentManager(),"zoomedDialog");
    }

    @Override
    public void onResume() {
        super.onResume();
        pbLoadingPic.setVisibility(View.VISIBLE);
        storageRef.child("images/"+program.getVodId()).
                getDownloadUrl().addOnSuccessListener(uri -> {
            pbLoadingPic.setVisibility(View.INVISIBLE);
            Glide.with(getApplicationContext()).load(uri).into(ivProgramImage);
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("failed downloading pic");
                pbLoadingPic.setVisibility(View.INVISIBLE);
                ivProgramImage.setImageResource(R.drawable.ic_default_pic);
                if (imgDecodableString != null){
                    //using Glide to set up the pic in the right rotation
                    Glide.with(getContext()).
                            load(Uri.parse(imgDecodableString)).
                            into(ivProgramImage);
                    if (newFragment != null)
                        newFragment.dismiss();
                }
                else if (cameraCheckedFilePath != null){
                    //using Glide to set up the pic in the right rotation

                    Glide.with(getContext()).
                            load(Uri.parse(cameraCheckedFilePath)).
                            into(ivProgramImage);
                    if (newFragment != null)
                        newFragment.dismiss();
                }
            }
        });

        if (uploadNeeded){
            if (imgDecodableString != null){
                uploadImg(imgDecodableString);
            }
            else if (cameraCheckedFilePath != null){
                uploadImg(cameraCheckedFilePath);
            }
            uploadNeeded = false;
        }
    }

    private void uploadImg(String picUri){
        Uri uri = Uri.parse(picUri);
        StorageReference ref = storageRef.child("images/"+program.getVodId());
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("מעלה תמונה..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("העלאת התמונה נכשלה, אנא נסה תמונה אחרת").
                        setPositiveButton("הבנתי", (dialog, which) -> {
                        }).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                        .getTotalByteCount());
                progressDialog.setMessage("מעלה "+(int)progress+"%");
            }
        });
    }
}
