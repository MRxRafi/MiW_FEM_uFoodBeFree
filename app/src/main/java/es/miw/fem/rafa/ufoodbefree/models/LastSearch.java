package es.miw.fem.rafa.ufoodbefree.models;

public class LastSearch {
    private String lastSearch;
    private String username;

    public LastSearch() {}

    public LastSearch(String lastSearch, String username) {
        this.lastSearch = lastSearch;
        this.username = username;
    }

    public String getLastSearch() {
        return lastSearch;
    }

    public void setLastSearch(String lastSearch) {
        this.lastSearch = lastSearch;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
