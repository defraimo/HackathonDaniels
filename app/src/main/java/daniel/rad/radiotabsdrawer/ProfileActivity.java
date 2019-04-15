package daniel.rad.radiotabsdrawer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import daniel.rad.radiotabsdrawer.login.LoginActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView tvDisconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvDisconnect = findViewById(R.id.tvDisconnect);

        tvDisconnect.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("האם אתה בטוח שברצונך להתנתק?").
                    setNegativeButton("לא", (dialog, which) -> {}).
                    setPositiveButton("כן",(dialog, which) -> {
                        SharedPreferences SPrefUser = getSharedPreferences("userName",MODE_PRIVATE);
                        SPrefUser.edit().putString("name",null).apply();
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        finish();
            }).show();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,DrawerActivity.class);
        startActivity(intent);
    }
}
