package es.miw.fem.rafa.ufoodbefree.dtos;

import es.miw.fem.rafa.ufoodbefree.models.Result;

public class ResultDto {
    private String title;
    private String summary;
    private String image;
    private Double healthScore;
    private Integer readyInMinutes;
    private Integer servings;

    public ResultDto(String title, String summary, String image, Double healthScore, Integer readyInMinutes, Integer servings) {
        this.title = title;
        this.summary = summary;
        this.image = image;
        this.healthScore = healthScore;
        this.readyInMinutes = readyInMinutes;
        this.servings = servings;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(Double healthScore) {
        this.healthScore = healthScore;
    }

    public Integer getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(Integer readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public static ResultDto fromResult(Result result) {
        return new ResultDto(result.getTitle(),
                result.getSummary(),
                result.getImage(),
                result.getHealthScore(),
                result.getReadyInMinutes(),
                result.getServings());
    }
}
