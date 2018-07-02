package app.movie.tutorial.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import app.movie.tutorial.com.R;
import app.movie.tutorial.com.model.movie.GenreItem;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.GenreViewHolder> {
    private static final String TAG = GenresAdapter.class.getSimpleName();

    private List<GenreItem> genres;
    private int rowLayout;
    private Context context;

    public GenresAdapter(List<GenreItem> genres, int rowLayout, Context context) {
        this.genres = genres;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    public static class GenreViewHolder extends RecyclerView.ViewHolder {
        TextView genreName;
        public GenreViewHolder(View v) {
            super(v);
            genreName = (TextView) v.findViewById(R.id.genreName);
        }
    }

    @Override
    public GenresAdapter.GenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GenreViewHolder holder, final int position) {
        holder.genreName.setText(this.genres.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }
}
