package daniel.rad.radiotabsdrawer.admin.programsManager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import daniel.rad.radiotabsdrawer.BuildConfig;
import daniel.rad.radiotabsdrawer.R;

import static daniel.rad.radiotabsdrawer.admin.programsManager.ProgramsManagerOptionsFragment.imgDecodableString;

public class PictureDialogFragment extends DialogFragment {

    Button bnTakePic;
    Button bnFromGalary;

    public static final int PICK_IMAGE = 1;
    private static final int CAMERA_REQUEST_CODE = 4;

    private String cameraUncheckedFilePath;

    static PictureDialogFragment newInstance() {
        return new PictureDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_program_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bnTakePic = view.findViewById(R.id.bnTakePic);
        bnFromGalary = view.findViewById(R.id.bnFromGalary);

        bnTakePic.setOnClickListener(v -> {
            captureFromCamera();
        });

        bnFromGalary.setOnClickListener(v -> {
            goToGallery();
        });
    }

    private void goToGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ProgramsManagerOptionsFragment.uploadNeeded = true;
            if (requestCode == PICK_IMAGE) {
                if (data.getData() != null) {
                    Uri selectedImage = data.getData();
//                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                    // Get the cursor
//                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                    // Move to first row
//                    cursor.moveToFirst();
//                    //Get the column index of MediaStore.Images.Media.DATA
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    //Gets the String value in the column
//                    ProgramsManagerOptionsFragment.imgDecodableString = cursor.getString(columnIndex);
//                    cursor.close();
//                    // Set the Image in ImageView after decoding the String
////                    ivProgramImage.setImageBitmap(BitmapFactory.decodeFile(ProgramsManagerOptionsFragment.imgDecodableString));
                    imgDecodableString = String.valueOf(selectedImage);
                    ProgramsManagerOptionsFragment.cameraCheckedFilePath = null;
                }
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                ProgramsManagerOptionsFragment.cameraCheckedFilePath = cameraUncheckedFilePath;
//                ivProgramImage.setImageURI(Uri.parse(ProgramsManagerOptionsFragment.cameraCheckedFilePath));
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
        String writingToDisk = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{writingToDisk}, 1);
        }
        if (ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}