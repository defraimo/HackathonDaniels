package daniel.rad.radiotabsdrawer.login;


import android.app.AlertDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;

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
public class RegularLoginFragment extends Fragment {

    TextView tvManagerLogin;
    TextView tvCreateUser;
    ImageView ivEnterApp;
    EditText etUserName;
    EditText etPassword;
    TextView tvForgotPassword;
    ProgressBar pbForgotPassword;

    private FirebaseAuth mAuth;
    private static final String TAG = "Log In";

    public RegularLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_regular_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        ivEnterApp = view.findViewById(R.id.ivEnterApp);
        etUserName = view.findViewById(R.id.etUserName);
        etPassword = view.findViewById(R.id.etPassword);
        tvCreateUser = view.findViewById(R.id.tvCreateUser);
        tvForgotPassword = view.findViewById(R.id.tvForgotPassword);
        pbForgotPassword = view.findViewById(R.id.pbForgotPassword);
        tvManagerLogin = view.findViewById(R.id.tvManagerLogin);


        tvCreateUser.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.login_frame, new SignInFragment()).
                    addToBackStack("regularLogin").
                    commit();
        });

        tvManagerLogin.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.login_frame, new ManagerLoginFragment()).
                    addToBackStack("regularLogin").
                    commit();
        });

        ivEnterApp.setOnClickListener(v -> {
            if (etUserName.getText().toString().equals("")) {
                etUserName.setError("חובה להזין כתובת אימייל");
            } else if (etPassword.getText().toString().equals("")) {
                etPassword.setError("חובה להזין סיסמא");
            } else {
                mAuth.signInWithEmailAndPassword(etUserName.getText().toString(), etPassword.getText().toString())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    SharedPreferences SPrefUser = view.getContext().getSharedPreferences("userName", MODE_PRIVATE);
                                    SPrefUser.edit().putString("name", etUserName.getText().toString()).apply();

                                    SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("whichUser", MODE_PRIVATE);
                                    sharedPreferences.edit().putString("userLogged", "true").apply();
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.
                                            setTitle("כתובת האימייל או הסיסמא שגויים").
                                            setPositiveButton("הבנתי", (dialog, which) -> {

                                            }).show();
                                }
                            }
                        });
            }
        });

        tvForgotPassword.setOnClickListener(v -> {
            if (etUserName.getText().toString().equals("")) {
                etUserName.setError("חובה להזין כתובת אימייל");
            } else {
                pbForgotPassword.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().sendPasswordResetEmail(etUserName.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                pbForgotPassword.setVisibility(View.INVISIBLE);
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.
                                            setTitle("בוצע איפוס סיסמא").
                                            setMessage("מייל נשלח לכתובת האימייל שלך עם הסיסמא החדשה").
                                            setPositiveButton("הבנתי", (dialog, which) -> {
                                            }).show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.
                                            setTitle("כתובת האימייל לא נמצאת במערכת").
                                            setPositiveButton("הזן אימייל תקין", (dialog, which) -> {
                                            }).show();
                                }
                            }
                        });
            }
        });

    }
}