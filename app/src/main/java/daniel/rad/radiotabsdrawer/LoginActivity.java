package daniel.rad.radiotabsdrawer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CircleImageView civProfilePicture;
    private TextView tvFbName;
    private TextView tvFbEmail;
    private CallbackManager cbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        loginButton = findViewById(R.id.login_button);
        civProfilePicture = findViewById(R.id.civProfilePicture);
        tvFbName = findViewById(R.id.tvFbName);
        tvFbEmail = findViewById(R.id.tvFbEmail);
        cbManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        checkLoginStatus();

        loginButton.registerCallback(cbManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "התחברת בהצלחה!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        cbManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null){
                //null = user is logged out
                tvFbName.setText("");
                tvFbEmail.setText("");
                civProfilePicture.setImageResource(0);
                Toast.makeText(LoginActivity.this, "User Logged out!",Toast.LENGTH_LONG).show();
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

                    tvFbEmail.setText(email);
                    tvFbName.setText(first_name +" "+last_name);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();
                    Glide.with(LoginActivity.this).load(image_url).into(civProfilePicture);
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