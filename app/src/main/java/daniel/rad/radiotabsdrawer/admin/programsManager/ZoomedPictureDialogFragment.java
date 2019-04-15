package daniel.rad.radiotabsdrawer.admin.programsManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import daniel.rad.radiotabsdrawer.R;

public class ZoomedPictureDialogFragment extends DialogFragment {

    ImageView ivZoomedPic;

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

        ivZoomedPic = view.findViewById(R.id.ivZoomedPic);

        if (getArguments() != null) {
            String bitmapPic = getArguments().getString("bitmapPic");
            String uriPic = getArguments().getString("uriPic");

            if (bitmapPic != null)
                Glide.with(this).load(BitmapFactory.decodeFile(bitmapPic)).into(ivZoomedPic);

            if (uriPic != null)
                Glide.with(this).load(Uri.parse(uriPic)).into(ivZoomedPic);
        }
        else {
            ivZoomedPic.setImageResource(R.drawable.ic_default_pic);
        }

    }
}