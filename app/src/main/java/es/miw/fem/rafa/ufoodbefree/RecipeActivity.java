package es.miw.fem.rafa.ufoodbefree;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import es.miw.fem.rafa.ufoodbefree.dtos.ResultDto;
import es.miw.fem.rafa.ufoodbefree.utils.GsonSingle;
import es.miw.fem.rafa.ufoodbefree.utils.SendDeviceDetails;

public class RecipeActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MiW";
    private static final String SAVED_INSTANCE = "results";
    private static String IP_PRISMA;

    ResultDto result;

    private TextView recipeName;
    private TextView recipeDescription;
    private ImageView recipeImage;
    private TextView healthScore;
    private TextView recipeTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        result = GsonSingle.getGsonParser().fromJson(getIntent().getStringExtra(SAVED_INSTANCE), ResultDto.class);

        recipeName = findViewById(R.id.ra_recipeName);
        recipeDescription = findViewById(R.id.ra_description);
        recipeImage = findViewById(R.id.ra_recipeImage);
        healthScore = findViewById(R.id.ra_healthScore);
        recipeTime = findViewById(R.id.ra_clockText);

        // Mostrar el icono back en la ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Icono Actionbar
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_miw_launcher_round);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        IP_PRISMA = sharedPreferences.getString(
                "ipPrisma",
                getString(R.string.prisma_defaultIP)
        );

        if(result != null) {
            recipeName.setText(result.getTitle());
            recipeDescription.setText(result.getSummary());
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                recipeDescription.setText(Html.fromHtml(result.getSummary(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                recipeDescription.setText(Html.fromHtml(result.getSummary()));
            }

            Glide.with(this)
                    .load(result.getImage())
                    .override(600, 600)
                    .centerCrop()
                    .into(recipeImage);

            int[] rgb = new int[3];
            healthScore.setText("Health Score: " + result.getHealthScore());
            if(result.getHealthScore() < 33) {
                healthScore.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                rgb[0] = 255;
            } else if(result.getHealthScore() >= 33 && result.getHealthScore() < 80) {
                healthScore.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
                rgb[0] = 255;
                rgb[1] = 128;
            } else {
                healthScore.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                rgb[2] = 255;
            }

            JSONObject json = new JSONObject();
            try {
                json.put("r", rgb[0]);
                json.put("g", rgb[1]);
                json.put("b", rgb[2]);
                new SendDeviceDetails().execute("http://" + IP_PRISMA + "/ring/color/", json.toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            recipeTime.setText(String.valueOf(result.getReadyInMinutes()));
        }
    }
}
