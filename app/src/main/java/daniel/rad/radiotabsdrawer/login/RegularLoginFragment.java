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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import daniel.rad.radiotabsdrawer.MainActivity;
import daniel.rad.radiotabsdrawer.R;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegularLoginFragment extends Fragment {

    private LoginButton loginButton;
    private CircleImageView civProfilePicture;
//    private TextView tvFbName;
//    private TextView tvFbEmail;
    private CallbackManager cbManager;
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

        loginButton = view.findViewById(R.id.login_button);
        civProfilePicture = view.findViewById(R.id.civProfilePicture);
        ivEnterApp = view.findViewById(R.id.ivEnterApp);
        etUserName = view.findViewById(R.id.etUserName);
        etPassword = view.findViewById(R.id.etPassword);
        tvCreateUser = view.findViewById(R.id.tvCreateUser);
        tvForgotPassword = view.findViewById(R.id.tvForgotPassword);
        pbForgotPassword = view.findViewById(R.id.pbForgotPassword);
//        tvFbName = view.findViewById(R.id.tvFbName);
//        tvFbEmail = view.findViewById(R.id.tvFbEmail);
        tvManagerLogin = view.findViewById(R.id.tvManagerLogin);
        cbManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));

//        checkLoginStatus();

        cbManager = CallbackManager.Factory.create();

        loginButton.registerCallback(cbManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                Toast.makeText(getApplicationContext(), "התחברת בהצלחה!", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("whichUser",MODE_PRIVATE);
                sharedPreferences.edit().putString("userLogged","true").apply();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        tvCreateUser.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.login_frame,new SignInFragment()).
                    addToBackStack("regularLogin").
                    commit();
        });

        tvManagerLogin.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.login_frame,new ManagerLoginFragment()).
                    addToBackStack("regularLogin").
                    commit();
        });

        ivEnterApp.setOnClickListener(v -> {
            if (etUserName.getText().toString().equals("")){
                etUserName.setError("חובה להזין כתובת אימייל");
            }
            else if (etPassword.getText().toString().equals("")){
                etPassword.setError("חובה להזין סיסמא");
            }
            else {
                mAuth.signInWithEmailAndPassword(etUserName.getText().toString(), etPassword.getText().toString())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    SharedPreferences SPrefUser = view.getContext().getSharedPreferences("userName",MODE_PRIVATE);
                                    SPrefUser.edit().putString("name",etUserName.getText().toString()).apply();

                                    SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("whichUser",MODE_PRIVATE);
                                    sharedPreferences.edit().putString("userLogged","true").apply();
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
            if (etUserName.getText().toString().equals("")){
                etUserName.setError("חובה להזין כתובת אימייל");
            }
            else {
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

    private void afterLoginSuccesses(){

        //SharedPreferences sharedPreferences = getSharedPreferences("userName", MODE_PRIVATE);
        //sharedPreferences.edit().putString("userName", "fbUser").apply();
        //Intent intent = new Intent(this, DrawerActivity.class);
        //startActivity(intent);
        //if we don't wat to get back to this activity again
        //finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cbManager.onActivityResult(requestCode,resultCode,data);
    }

//    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
//        @Override
//        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//            if (currentAccessToken == null){
//                //null = user is logged out
////                tvFbName.setText("");
////                tvFbEmail.setText("");
//                civProfilePicture.setImageResource(0);
//                Toast.makeText(getActivity(), "User Logged out!",Toast.LENGTH_LONG).show();
//            }else {
//                loadProfile(currentAccessToken);
//            }
//        }
//    };

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getContext(), "ההתחברות נכשלה",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loadProfile(AccessToken newAccessToken){
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email;
                    if (object.has("email")) {
                        email = object.getString("email");
                    }
                    String id = object.getString("id");
                    String image_url = "https://graph.facebook.com/"+id+"/picture?type=normal";

//                    tvFbEmail.setText(email);
//                    tvFbName.setText(first_name +" "+last_name);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();
                    Glide.with(getActivity()).load(image_url).into(civProfilePicture);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name, last_name, email, id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void checkLoginStatus(){
        if (AccessToken.getCurrentAccessToken() != null){
            //!=null means user already logged in!
            loadProfile(AccessToken.getCurrentAccessToken());
        }
    }
}
