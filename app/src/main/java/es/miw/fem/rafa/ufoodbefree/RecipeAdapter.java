package es.miw.fem.rafa.ufoodbefree;

import android.content.Context;
import android.net.Uri;
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


public class RecipeAdapter extends ArrayAdapter {
    private Context context;
    private Recipe[] recipes;
    private int idResourceLayout;


    public RecipeAdapter(@NonNull Context context, int resource, @NonNull Recipe[] recipes) {
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
                .load(recipes[position].IMAGE_URL)
                .override(200, 200)
                .centerCrop()
                .into(ivRecipeImage);
        tvRecipeTitle.setText(recipes[position].name);
        tvRecipeDescription.setText(recipes[position].description);

        return view;
    }
}
