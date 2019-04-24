package daniel.rad.radiotabsdrawer.login;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import daniel.rad.radiotabsdrawer.BuildConfig;
import daniel.rad.radiotabsdrawer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    EditText etEmail;
    EditText etSignInPassword;
    EditText etUserFullName;
    ImageView ivSignUp;
    ImageView ivUserTakePhoto;
    ImageView ivUserFromGallery;
    ProgressBar pbSignIn;

    public static final int PICK_IMAGE = 1;
    private static final int CAMERA_REQUEST_CODE = 4;

    private String cameraUncheckedFilePath;
    static String imgDecodableString;
    static String cameraCheckedFilePath;

    private FirebaseAuth mAuth;
    private static final String TAG = "Sign Up";

    private DatabaseReference users =
            FirebaseDatabase.getInstance()
                    .getReference("Users");

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        etEmail = view.findViewById(R.id.etEmail);
        etSignInPassword = view.findViewById(R.id.etSignInPassword);
        etUserFullName = view.findViewById(R.id.etUserFullName);
        ivSignUp = view.findViewById(R.id.ivSignUp);
        ivUserTakePhoto = view.findViewById(R.id.ivUserTakePhoto);
        ivUserFromGallery = view.findViewById(R.id.ivUserFromGallery);
        pbSignIn = view.findViewById(R.id.pbSignIn);
        ivSignUp.setEnabled(true);

        ivSignUp.setOnClickListener(v -> {
            if (!etUserFullName.getText().toString().equals("")) {
                pbSignIn.setVisibility(View.VISIBLE);
                if (imgDecodableString == null && cameraUncheckedFilePath == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("מומלץ להוסיף תמונה").
                            setPositiveButton("הוסף", (dialog, which) -> {
                                pbSignIn.setVisibility(View.INVISIBLE);
                            }).
                            setNegativeButton("המשך ללא תמונה", (dialog, which) -> {
                                ivSignUp.setEnabled(false);
                                mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etSignInPassword.getText().toString())
                                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    Log.d(TAG, "createUserWithEmail:success");
                                                    pbSignIn.setVisibility(View.INVISIBLE);
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    createNewUser(user);
                                                    getActivity().getSupportFragmentManager().
                                                            beginTransaction().
                                                            replace(R.id.login_frame, new RegularLoginFragment()).
                                                            commit();
                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                                    pbSignIn.setVisibility(View.INVISIBLE);
                                                    ivSignUp.setEnabled(true);
                                                    if (etSignInPassword.getText().toString().length() < 6) {
                                                        etSignInPassword.setError("הסיסמא צריכה להיות באורך 6 תווים לפחות");
                                                    } else {
                                                        etEmail.setError("אימייל לא תקין");
                                                    }
                                                }
                                            }
                                        });
                            }).
                            show();
                }
                else {
                    mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etSignInPassword.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        pbSignIn.setVisibility(View.INVISIBLE);
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        createNewUser(user);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        pbSignIn.setVisibility(View.INVISIBLE);
                                        ivSignUp.setEnabled(true);
                                        if (etSignInPassword.getText().toString().length() < 6) {
                                            etSignInPassword.setError("הסיסמא צריכה להיות באורך 6 תווים לפחות");
                                        } else {
                                            etEmail.setError("אימייל לא תקין");
                                        }
                                    }
                                }
                            });
                }
            }
            else {
                pbSignIn.setVisibility(View.INVISIBLE);
                etUserFullName.setError("שדה חובה");
            }
        });

        ivUserTakePhoto.setOnClickListener(v -> {
            captureFromCamera();
        });

        ivUserFromGallery.setOnClickListener(v -> {
            goToGallery();
        });
    }
    private void createNewUser(FirebaseUser fbUser) {
        if (fbUser != null) {
            String username = etUserFullName.getText().toString();
            String email = fbUser.getEmail();
            String userId = fbUser.getUid();

            User user;

            if (imgDecodableString != null){
                user = new User(username, email, userId, imgDecodableString);
                uploadImg(imgDecodableString,fbUser);
//                uploadImage(imgDecodableString);
            }
            else if (cameraCheckedFilePath != null){
                user = new User(username, email, userId, cameraCheckedFilePath);
                uploadImg(cameraCheckedFilePath,fbUser);
//                uploadImage(cameraCheckedFilePath);
            }
            else {
                user = new User(username, email, userId);
            }

            users.child(userId).setValue(user);
        }
    }

    private void uploadImg(String picUri,FirebaseUser user){
        Uri uri = Uri.parse(picUri);
        StorageReference ref = storageRef.child("images/"+user.getUid());
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("יוצר משתמש..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                getActivity().getSupportFragmentManager().
                        beginTransaction().
                        replace(R.id.login_frame, new RegularLoginFragment()).
                        commit();
                Toast.makeText(getContext(), "משתמש נוצר בהצלחה", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                ivSignUp.setEnabled(true);
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
                progressDialog.setMessage("טוען "+(int)progress+"%");
            }
        });
    }

    private void goToGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                if (data.getData() != null) {
                    Uri selectedImage = data.getData();
                    imgDecodableString = String.valueOf(selectedImage);
                    cameraCheckedFilePath = null;
                }
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                cameraCheckedFilePath = cameraUncheckedFilePath;
                imgDecodableString = null;
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //This is the directory in which the file will be created. This is the default location of Camera photos
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for using again
        cameraUncheckedFilePath = "file://" + image.getAbsolutePath();
        return image;
    }

    private void captureFromCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
