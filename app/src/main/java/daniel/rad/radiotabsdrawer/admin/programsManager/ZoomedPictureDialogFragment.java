package daniel.rad.radiotabsdrawer.admin.programsManager;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import daniel.rad.radiotabsdrawer.R;
import daniel.rad.radiotabsdrawer.programs.ProgramsData;


public class ZoomedPictureDialogFragment extends DialogFragment {

    ImageView ivZoomedPic;
    ProgressBar pbLoadingZoomedPic;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    static ZoomedPictureDialogFragment newInstance() {
        return new ZoomedPictureDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.zoomed_admin_program_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments()!= null) {
            ProgramsData program = getArguments().getParcelable("program");
            ivZoomedPic = view.findViewById(R.id.ivZoomedPic);
            pbLoadingZoomedPic = view.findViewById(R.id.pbLoadingZoomedPic);

            pbLoadingZoomedPic.setVisibility(View.VISIBLE);
            storageRef.child("images/" + program.getVodId()).
                    getDownloadUrl().addOnSuccessListener(uri -> {
                pbLoadingZoomedPic.setVisibility(View.INVISIBLE);
                Glide.with(view.getContext()).load(uri).into(ivZoomedPic);
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pbLoadingZoomedPic.setVisibility(View.INVISIBLE);

                    String uriPic = getArguments().getString("uriPic");
                    if (uriPic != null) {
                        Glide.with(getContext()).load(Uri.parse(uriPic)).into(ivZoomedPic);
                    } else {
                        ivZoomedPic.setImageResource(R.drawable.ic_default_pic);
                    }
                }
            });
        }
    }
}