package es.miw.fem.rafa.ufoodbefree;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SearchActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MiW";

    private FirebaseAuth fAuth;

    private EditText etRecipeName;
    private ListView lvResultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etRecipeName = (EditText) findViewById(R.id.recipeNameSearch);
        lvResultsList = (ListView) findViewById(R.id.resultsList);

        fAuth = FirebaseAuth.getInstance();

        Recipe recipe1 = new Recipe("https://image.shutterstock.com/image-photo/carrot-isolated-on-white-background-260nw-795704785.jpg",
                "ZanahoriaRecipe", "Pues zanahorias solo");
        Recipe recipe2 = new Recipe("https://image.shutterstock.com/image-photo/carrot-isolated-on-white-background-260nw-795704785.jpg",
                "ZanahoriaRecipe2", "Pues zanahorias solo2");

        Recipe[] recipes = new Recipe[2];
        recipes[0] = recipe1;
        recipes[1] = recipe2;

        RecipeAdapter adapter = new RecipeAdapter(
                this,
                R.layout.result_item,
                recipes
        );

        lvResultsList.setAdapter(adapter);

        // Mostrar el icono back en la ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Icono Actionbar
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_miw_launcher_round);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void obtainRecipes(View v) {
        String recipeSearch = etRecipeName.getText().toString();
        Toast.makeText(getApplicationContext(), recipeSearch, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.add(Menu.NONE, 2, Menu.NONE, R.string.sign_out);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuPreviousSearches:
                // TODO Mostrar listado búsquedas anteriores del usuario

                break;
            case 2:
                this.signOut();
                break;
        }
        return true;
    }

    private void signOut() {
        fAuth.signOut();
        String strLoggedOut = "Usuario desconectado";
        Toast.makeText(this, strLoggedOut, Toast.LENGTH_SHORT)
                .show();
        startActivity(new Intent(this, MainActivity.class));
    }
}
