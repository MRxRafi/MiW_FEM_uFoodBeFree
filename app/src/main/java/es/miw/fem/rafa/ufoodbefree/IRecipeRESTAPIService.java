package es.miw.fem.rafa.ufoodbefree;

import java.util.Map;

import es.miw.fem.rafa.ufoodbefree.models.RecipeList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

@SuppressWarnings("Unused")
public interface IRecipeRESTAPIService {

    // https://api.spoonacular.com/recipes/complexSearch?query=...
    @GET("/recipes/complexSearch")
    Call<RecipeList> getRecipesByName(@QueryMap Map<String, String> params);
}
