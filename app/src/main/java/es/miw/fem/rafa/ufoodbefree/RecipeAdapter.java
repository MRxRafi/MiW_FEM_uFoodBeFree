package es.miw.fem.rafa.ufoodbefree;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import es.miw.fem.rafa.ufoodbefree.models.RecipeList;
import es.miw.fem.rafa.ufoodbefree.models.Result;


public class RecipeAdapter extends ArrayAdapter {
    private Context context;
    private Result[] recipes;
    private int idResourceLayout;


    public RecipeAdapter(@NonNull Context context, int resource, @NonNull Result[] recipes) {
        super(context, resource, recipes);
        this.context = context;
        this.idResourceLayout = resource;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RelativeLayout view;

        if(null != convertView) {
            view = (RelativeLayout) convertView;
        } else {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (RelativeLayout) inflater.inflate(idResourceLayout, parent, false);
        }

        ImageView ivRecipeImage = view.findViewById(R.id.recipeImage);
        TextView tvRecipeTitle = view.findViewById(R.id.recipeName);
        TextView tvRecipeDescription = view.findViewById(R.id.recipeDescription);

        Glide.with(context)
                .load(recipes[position].getImage())
                .override(300, 300)
                .centerCrop()
                .into(ivRecipeImage);
        tvRecipeTitle.setText(recipes[position].getTitle());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvRecipeDescription.setText(Html.fromHtml(recipes[position].getSummary(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvRecipeDescription.setText(Html.fromHtml(recipes[position].getSummary()));
        }


        return view;
    }
}
