package es.miw.fem.rafa.ufoodbefree;

public class Recipe {
    public String IMAGE_URL;
    public String name;
    public String description;

    public Recipe(String IMAGE_URL, String name, String description) {
        this.IMAGE_URL = IMAGE_URL;
        this.name = name;
        this.description = description;
    }
}
