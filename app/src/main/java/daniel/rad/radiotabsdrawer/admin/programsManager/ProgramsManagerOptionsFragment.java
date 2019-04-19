package daniel.rad.radiotabsdrawer.admin.programsManager;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import daniel.rad.radiotabsdrawer.BuildConfig;
import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramsManagerOptionsFragment extends Fragment {

    TextView tvManagerProgramName;
    ImageView ivAddStudent;
    ImageView ivAddPicFromFB;
    ImageView ivEditPic;
    public ImageView ivProgramImage;

    static String imgDecodableString;

    boolean isButtonOnePressed = false;
    boolean isButtonTwoPressed = false;

    static String cameraCheckedFilePath;

    DialogFragment newFragment;

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

        ProgramsData model =  getArguments().getParcelable("model");
        tvManagerProgramName = view.findViewById(R.id.tvManagerProgramName);

        tvManagerProgramName.setText(model.getProgramName());
        tvManagerProgramName.setSelected(true);

        ivAddStudent = view.findViewById(R.id.ivAddStudent);
        ivAddPicFromFB = view.findViewById(R.id.ivAddPicFromFB);
        ivEditPic = view.findViewById(R.id.ivEditPic);
        ivProgramImage = view.findViewById(R.id.ivProgramImage);

        ivAddPicFromFB.setOnClickListener(v -> {
            if (!isButtonOnePressed){
                ivAddPicFromFB.setImageResource(R.drawable.ic_search_bar_pressed);
                isButtonOnePressed = true;
                ivAddStudent.setImageResource(R.drawable.ic_search_bar);
                isButtonTwoPressed = false;
            }
        });

        ivAddStudent.setOnClickListener(v -> {
            if (!isButtonTwoPressed){
                ivAddStudent.setImageResource(R.drawable.ic_search_bar_pressed);
                isButtonTwoPressed = true;
                ivAddPicFromFB.setImageResource(R.drawable.ic_search_bar);
                isButtonOnePressed = false;
            }
        });

        ivEditPic.setOnClickListener(v -> {
            showDialog();
        });

        ivProgramImage.setOnClickListener(v -> {
            showZoomedDialog();
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
        zoomedDialog.setArguments(bundle);
        zoomedDialog.show(getFragmentManager(),"zoomedDialog");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (imgDecodableString != null){
            //using Glide to set up the pic in the right rotation
            Glide.with(this).
                    load(Uri.parse(imgDecodableString)).
                    into(ivProgramImage);
//            ivProgramImage.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
            if (newFragment != null)
                newFragment.dismiss();
        }
        else if (cameraCheckedFilePath != null){
            //using Glide to set up the pic in the right rotation

            Glide.with(this).
                    load(Uri.parse(cameraCheckedFilePath)).
                    into(ivProgramImage);
//            ivProgramImage.setImageURI(Uri.parse(cameraCheckedFilePath));
            if (newFragment != null)
                newFragment.dismiss();
        }
    }}
