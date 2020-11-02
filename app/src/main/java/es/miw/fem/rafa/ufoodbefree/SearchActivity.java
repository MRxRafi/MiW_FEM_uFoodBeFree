package es.miw.fem.rafa.ufoodbefree;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import es.miw.fem.rafa.ufoodbefree.dtos.ResultDto;
import es.miw.fem.rafa.ufoodbefree.models.RecipeList;
import es.miw.fem.rafa.ufoodbefree.utils.GsonSingle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {
    private static final String API_BASE_URL = "https://api.spoonacular.com";
    private static final String API_KEY = "7b85f04565ac44609112d2e20c7fa24c";
    private static final String MAX_RESULTS_NUMBER = "5";

    private static final String LOG_TAG = "MiW";

    private static final String SAVED_INSTANCE = "results";

    private FirebaseAuth fAuth;

    private EditText etRecipeName;
    private ListView lvResultsList;

    ResultDto[] resultsDto;

    private IRecipeRESTAPIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etRecipeName = (EditText) findViewById(R.id.recipeNameSearch);
        lvResultsList = (ListView) findViewById(R.id.resultsList);

        fAuth = FirebaseAuth.getInstance();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(IRecipeRESTAPIService.class);

        lvResultsList.setClickable(true);
        lvResultsList.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent(this, RecipeActivity.class);
            Log.i(LOG_TAG, "click en el elemento " + resultsDto[position].getTitle() + " de mi ListView");
            intent.putExtra(SAVED_INSTANCE, GsonSingle.getGsonParser().toJson(resultsDto[position]));
            startActivity(intent);
        });

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

        Map<String, String> query = new HashMap<>();
        query.put("query", recipeSearch);
        query.put("addRecipeInformation", "true");
        query.put("number", MAX_RESULTS_NUMBER);
        query.put("apiKey", API_KEY);

        Call<RecipeList> call_async = apiService.getRecipesByName(query);

        call_async.enqueue(new Callback<RecipeList>() {
            @Override
            public void onResponse(Call<RecipeList> call, Response<RecipeList> response) {
                RecipeList recipeList = response.body();

                if(null != recipeList) {
                    resultsDto = new ResultDto[recipeList.getResults().size()];
                    for(int i = 0; i < recipeList.getResults().size(); i++) {
                        resultsDto[i] = ResultDto.fromResult(recipeList.getResults().get(i));
                    }

                    RecipeAdapter adapter = new RecipeAdapter(
                            SearchActivity.this,
                            R.layout.result_item,
                            resultsDto
                    );

                    lvResultsList.setAdapter(adapter);
                } else {
                    Log.i(LOG_TAG, getString(R.string.strError));
                }
            }

            @Override
            public void onFailure(Call<RecipeList> call, Throwable t) {
                Toast.makeText(
                        getApplicationContext(),
                        "ERROR: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if(null != resultsDto) {
            String[] savedRecipes = new String[resultsDto.length];
            for(int i = 0; i < resultsDto.length; i++) {
                savedRecipes[i] = GsonSingle.getGsonParser().toJson(resultsDto[i]);
            }
            outState.putStringArray(SAVED_INSTANCE, savedRecipes);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String[] savedRecipes = savedInstanceState.getStringArray(SAVED_INSTANCE);
        if(savedRecipes != null) {
            resultsDto = new ResultDto[savedRecipes.length];
            for(int i = 0; i < savedRecipes.length; i++) {
                resultsDto[i] = GsonSingle.getGsonParser().fromJson(savedRecipes[i], ResultDto.class);
            }

            RecipeAdapter adapter = new RecipeAdapter(
                    SearchActivity.this,
                    R.layout.result_item,
                    resultsDto
            );

            lvResultsList.setAdapter(adapter);
        }
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
