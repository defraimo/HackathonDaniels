package daniel.rad.radiotabsdrawer.login;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

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

    private static final String CHECK_USER_NAME = "c";
    private static final String CHECK_PASSWORD = "c";

    private LoginButton loginButton;
    private CircleImageView civProfilePicture;
//    private TextView tvFbName;
//    private TextView tvFbEmail;
    private CallbackManager cbManager;
    TextView tvManagerLogin;
    ImageView ivEnterApp;
    EditText etUserName;
    EditText etPassword;

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

        loginButton = view.findViewById(R.id.login_button);
        civProfilePicture = view.findViewById(R.id.civProfilePicture);
        ivEnterApp = view.findViewById(R.id.ivEnterApp);
        etUserName = view.findViewById(R.id.etUserName);
        etPassword = view.findViewById(R.id.etPassword);
//        tvFbName = view.findViewById(R.id.tvFbName);
//        tvFbEmail = view.findViewById(R.id.tvFbEmail);
        tvManagerLogin = view.findViewById(R.id.tvManagerLogin);
        cbManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        checkLoginStatus();

        loginButton.registerCallback(cbManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "התחברת בהצלחה!", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("whoIsLogged",MODE_PRIVATE);
//                sharedPreferences.edit().putString("userName",tvFbName.getText().toString()).apply();
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

        tvManagerLogin.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.login_frame,new ManagerLoginFragment()).
                    addToBackStack("regularLogin").
                    commit();
        });

        ivEnterApp.setOnClickListener(v -> {
            if (etUserName.getText() == null){
                etUserName.setError("חובה להזין שם משתמש");
            }
            else if (etPassword.getText() == null){
                etPassword.setError("חובה להזין סיסמא");
            }
            else if (etUserName.getText().toString().equalsIgnoreCase(CHECK_USER_NAME)
                    && etPassword.getText().toString().equalsIgnoreCase(CHECK_PASSWORD)){

                SharedPreferences SPrefUser = view.getContext().getSharedPreferences("userName",MODE_PRIVATE);
                SPrefUser.edit().putString("name",etUserName.getText().toString()).apply();

                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("whichUser",MODE_PRIVATE);
                sharedPreferences.edit().putString("userLogged","true").apply();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
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
        cbManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null){
                //null = user is logged out
//                tvFbName.setText("");
//                tvFbEmail.setText("");
                civProfilePicture.setImageResource(0);
                Toast.makeText(getActivity(), "User Logged out!",Toast.LENGTH_LONG).show();
            }else {
                loadProfile(currentAccessToken);
            }
        }
    };

    private void loadProfile(AccessToken newAccessToken){
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
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
