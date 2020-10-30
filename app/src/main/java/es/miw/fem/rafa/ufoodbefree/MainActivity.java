package es.miw.fem.rafa.ufoodbefree;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final String LOG_TAG = "MiW";

    private EditText etEmailField;
    private EditText etPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmailField = findViewById(R.id.fieldEmail);
        etPasswordField = findViewById(R.id.fieldPassword);

        findViewById(R.id.buttonSignIn).setOnClickListener(this);

        // Mostrar el icono back en la ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Icono Actionbar
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_miw_launcher_round);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if(viewId == R.id.buttonSignIn) {
            signInWithCredentials();
        }

        if(viewId == R.id.buttonSignOut) {
            // TODO Sign out implementation
        }
    }

    private boolean validateLinkForm() {
        boolean valid = true;

        String email = etEmailField.getText().toString();
        if(TextUtils.isEmpty(email)) {
            etEmailField.setError(getString(R.string.field_required));
            valid = false;
        } else {
            etEmailField.setError(null);
        }

        String password = etPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPasswordField.setError(getString(R.string.field_required));
            valid = false;
        } else {
            etPasswordField.setError(null);
        }

        return valid;
    }

    private void signInWithCredentials() {
        if(!validateLinkForm()) return;

        // Get email and password from form
        String email = etEmailField.getText().toString();
        String password = etPasswordField.getText().toString();

        // TODO Sign in implementation with Firebase

        startActivity(new Intent(this, SearchActivity.class));
    }
}