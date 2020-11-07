package es.miw.fem.rafa.ufoodbefree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.miw.fem.rafa.ufoodbefree.utils.SendDeviceDetails;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final String LOG_TAG = "MiW";

    private FirebaseAuth fAuth;

    private EditText etEmailField;
    private EditText etPasswordField;
    private Button bSignIn;
    private Button bSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmailField = findViewById(R.id.fieldEmail);
        etPasswordField = findViewById(R.id.fieldPassword);
        bSignIn = findViewById(R.id.buttonSignIn);
        bSignOut = findViewById(R.id.buttonSignOut);

        bSignIn.setOnClickListener(this);
        bSignOut.setOnClickListener(this);
        findViewById(R.id.statusSwitch).setClickable(false);

        fAuth = FirebaseAuth.getInstance();

        this.initPrisma();

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

        FirebaseUser currentUser = fAuth.getCurrentUser();
        this.updateUI(currentUser);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if(viewId == R.id.buttonSignIn) {
            this.signInWithCredentials();
        }

        if(viewId == R.id.buttonSignOut) {
            this.signOut();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.add(Menu.NONE, 2, Menu.NONE, R.string.menuRecipes);
        menu.add(Menu.NONE, 3, Menu.NONE, R.string.menuSettings);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuPreviousSearches:
                if(fAuth.getCurrentUser() != null) {
                    startActivity(new Intent(this, LastSearchesActivity.class));
                } else {
                    Toast.makeText(this, getString(R.string.sign_in_required), Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case 2:
                if(fAuth.getCurrentUser() != null) {
                    startActivity(new Intent(this, SearchActivity.class));
                } else {
                    Toast.makeText(this, R.string.sign_in_required, Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case 3:
                if(fAuth.getCurrentUser() != null) {
                    startActivity(new Intent(this, SettingsActivity.class));
                } else {
                    Toast.makeText(this, R.string.sign_in_required, Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
        return true;
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

        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // Sign in success
                            Log.i(LOG_TAG, "signInWithCredentials:success");
                            FirebaseUser user = fAuth.getCurrentUser();

                            String strLogged = "Usuario logado: " + user.getEmail();
                            Toast.makeText(MainActivity.this, strLogged, Toast.LENGTH_SHORT)
                                    .show();
                            updateUI(user);

                            startActivity(new Intent(MainActivity.this, SearchActivity.class));
                        } else {
                            // Sign in failed
                            Log.w(LOG_TAG, "signInWithCredentials:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Auth failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

    }

    private void signOut() {
        fAuth.signOut();

        String strLoggedOut = "Usuario desconectado";
        Toast.makeText(this, strLoggedOut, Toast.LENGTH_SHORT).show();

        updateUI(null);
    }

    private void updateUI(FirebaseUser user) {
        TextView tvEmailView = findViewById(R.id.statusEmail);
        Switch sLogStatus = findViewById(R.id.statusSwitch);
        boolean signedIn = (user != null);

        if(signedIn) {
            tvEmailView.setText(getString(R.string.email_fmt, user.getEmail()));
            Log.i(LOG_TAG, "signedIn: " + getString(R.string.id_fmt, user.getDisplayName()));
        } else {
            tvEmailView.setText(R.string.email_none);
            Log.i(LOG_TAG, "signOut: " + getString(R.string.signed_out));
        }

        bSignIn.setEnabled(!signedIn);
        bSignOut.setEnabled(signedIn);
        sLogStatus.setChecked(signedIn);
    }

    private void initPrisma() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String IP_PRISMA = sharedPreferences.getString(
                "ipPrisma",
                getString(R.string.prisma_defaultIP)
        );
        new SendDeviceDetails().execute("http://" + IP_PRISMA + "/ring/on/","");
    }
}