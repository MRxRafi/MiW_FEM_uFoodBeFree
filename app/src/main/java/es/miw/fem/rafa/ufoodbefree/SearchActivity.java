package es.miw.fem.rafa.ufoodbefree;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MiW";

    private EditText etRecipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etRecipeName = (EditText) findViewById(R.id.recipeNameSearch);
    }

    public void obtainRecipes(View v) {
        String recipeSearch = etRecipeName.getText().toString();
        Toast.makeText(getApplicationContext(), recipeSearch, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuPreviousSearches:
                /*if(mAuth.getCurrentUser() != null) {
                    startActivity(new Intent(this, CountriesListActivity.class));
                } else {
                    Toast.makeText(this, R.string.txtNoLogado, Toast.LENGTH_SHORT)
                            .show();
                }
                */
                break;
            case R.id.menuSignOut:
                break;
        }
        return true;
    }
}
