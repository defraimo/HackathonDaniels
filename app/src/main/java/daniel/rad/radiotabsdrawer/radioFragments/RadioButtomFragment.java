package daniel.rad.radiotabsdrawer.radioFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import daniel.rad.radiotabsdrawer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RadioButtomFragment extends Fragment {

    ImageView ivLike;
    ImageView ivComment;
    ImageView ivShare;
    static boolean isLiked = false;

    public RadioButtomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_radio_buttom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivLike = view.findViewById(R.id.ivLike);
        ivComment = view.findViewById(R.id.ivComment);
        ivShare = view.findViewById(R.id.ivShare);

        ivLike.setOnClickListener(v -> {
            if (!isLiked) {
                ivLike.setImageResource(R.drawable.ic_like_red);
                isLiked = true;
            }
            else {
                ivLike.setImageResource(R.drawable.ic_like_grey);
                isLiked = false;
            }
        });

        ivComment.setOnClickListener(v -> {
            getFragmentManager().
                    beginTransaction().
                    addToBackStack("radioHomePage").
                    replace(R.id.buttom_frame,new ButtomCommentsRadioFragment()).
                    commit();
        });

        ivShare.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, "מוזמנ/ת להקשיב לתוכנית *שם התוכנית* של *שם התלמיד*! *קישור כניסה לאפליקצייה*");

            startActivity(Intent.createChooser(share, "היכן תרצה לשתף את התוכנית?"));
        });
    }
}
