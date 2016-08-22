package de.android.restfullapiexample;

public class Value {
    private int id;
    private String joke;
    private String[] categories;

    public Value(int id, String joke, String[] categories) {
        this.id = id;
        this.joke = joke;
        this.categories = categories;
    }

    public int getId() {
        return id;
    }

    public String getJoke() {
        return joke;
    }

    public String[] getCategories() {
        return categories;
    }
}
