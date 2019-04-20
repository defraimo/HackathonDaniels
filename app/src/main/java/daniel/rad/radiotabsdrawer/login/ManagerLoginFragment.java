package daniel.rad.radiotabsdrawer.login;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import daniel.rad.radiotabsdrawer.MainActivity;
import daniel.rad.radiotabsdrawer.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManagerLoginFragment extends Fragment {

    EditText etAdminUserName;
    EditText etAdminPassword;
    ImageView ivEnterApp;

    private static final String ADMIN_USER_NAME = "koletzion@gmail.com";

    private FirebaseAuth mAuth;
    private static final String TAG = "Admin Log In";

    public ManagerLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        etAdminUserName = view.findViewById(R.id.etAdminUserName);
        etAdminPassword = view.findViewById(R.id.etAdminPassword);
        ivEnterApp = view.findViewById(R.id.ivEnterApp);

        ivEnterApp.setOnClickListener(v -> {
            if (etAdminUserName.getText() == null){
                etAdminUserName.setError("חובה להזין שם משתמש");
            }
            else if (etAdminPassword.getText() == null){
                etAdminPassword.setError("חובה להזין סיסמא");
            }
            else if (etAdminUserName.getText().toString().equalsIgnoreCase(ADMIN_USER_NAME)){
                mAuth.signInWithEmailAndPassword(etAdminUserName.getText().toString(),etAdminPassword.getText().toString()).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    SharedPreferences SPrefUser = view.getContext().getSharedPreferences("userName",MODE_PRIVATE);
                                    SPrefUser.edit().putString("name","מנהל").apply();

                                    SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("whichUser",MODE_PRIVATE);
                                    sharedPreferences.edit().putString("managerLogged","true").apply();
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.
                                            setTitle("שם משתמש או סיסמא שגויים").
                                            setPositiveButton("הבנתי", (dialog, which) -> {

                                            }).show();
                                }
                            }
                        });
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.
                        setTitle("שם משתמש או סיסמא שגויים").
                        setPositiveButton("הבנתי", (dialog, which) -> {

                        }).show();
            }
        });
    }
}
