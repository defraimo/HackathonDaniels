package daniel.rad.radiotabsdrawer.admin.notificationManager;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import daniel.rad.radiotabsdrawer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsManagerFragment extends Fragment {

    private DatabaseReference notifications =
            FirebaseDatabase.getInstance()
                    .getReference("Notifications");

    EditText etTitle;
    EditText etText;
    ImageView ivSend;

    public NotificationsManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications_manager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etTitle = view.findViewById(R.id.etTitle);
        etText = view.findViewById(R.id.etText);
        ivSend = view.findViewById(R.id.ivSend);

        ivSend.setOnClickListener(v -> {
            if (etTitle.getText().toString().equals("")){
                etTitle.setError("שדה חובה");
            }
            else if (etText.getText().toString().equals("")){
                etText.setError("שדה חובה");
            }
            else {
                notifications.setValue(new CostumeNotification(etTitle.getText().toString(), etText.getText().toString()));
                Toast.makeText(getContext(), "ההתראה נשלחה", Toast.LENGTH_SHORT).show();
                etTitle.setText("");
                etText.setText("");
            }
        });
    }
}
