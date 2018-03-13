package app.movie.tutorial.com.model.movie.images;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImagesResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("backdrops")
    @Expose
    private List<Backdrop> backdrops = null;
    @SerializedName("posters")
    @Expose
    private List<Poster> posters = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Backdrop> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(List<Backdrop> backdrops) {
        this.backdrops = backdrops;
    }

    public List<Poster> getPosters() {
        return posters;
    }

    public void setPosters(List<Poster> posters) {
        this.posters = posters;
    }

    @Override
    public String toString() {
        return "ImagesResponse{" +
                "id=" + id +
                ", backdrops=" + backdrops +
                ", posters=" + posters +
                '}';
    }
}