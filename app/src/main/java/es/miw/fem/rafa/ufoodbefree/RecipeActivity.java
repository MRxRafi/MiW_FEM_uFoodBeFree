package es.miw.fem.rafa.ufoodbefree;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import es.miw.fem.rafa.ufoodbefree.dtos.ResultDto;
import es.miw.fem.rafa.ufoodbefree.utils.GsonSingle;

public class RecipeActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MiW";
    private static final String SAVED_INSTANCE = "results";

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

        healthScore.setText("Health Score: " + String.valueOf(result.getHealthScore()));
        if(result.getHealthScore() < 33) {
            healthScore.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        } else if(result.getHealthScore() >= 33 && result.getHealthScore() < 80) {
            healthScore.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
        } else {
            healthScore.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        }
        recipeTime.setText(String.valueOf(result.getReadyInMinutes()));
    }
}
