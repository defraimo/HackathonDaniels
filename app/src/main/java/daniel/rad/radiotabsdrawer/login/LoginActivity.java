package daniel.rad.radiotabsdrawer.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import daniel.rad.radiotabsdrawer.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.login_frame,new RegularLoginFragment()).
                commit();

    }
}