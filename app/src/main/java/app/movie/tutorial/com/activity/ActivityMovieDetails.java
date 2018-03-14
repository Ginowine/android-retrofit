package app.movie.tutorial.com.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import app.movie.tutorial.com.Config;
import app.movie.tutorial.com.R;
import app.movie.tutorial.com.adapter.GenresAdapter;
import app.movie.tutorial.com.adapter.ProductionCompaniesAdapter;
import app.movie.tutorial.com.model.movie.MovieResponse;
import app.movie.tutorial.com.rest.API;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ActivityMovieDetails extends AppCompatActivity {
    private final static String TAG = ActivityMovieDetails.class.getSimpleName();
    private Context context;
    private static Retrofit retrofit = null;
    private Toolbar toolbar;
    private TextView tv_title, tv_original_title;
    private WebView webView;
    private ImageView img_movie;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ProgressBar progressBar;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView genres_recycler_view;
    private RecyclerView production_companies_rv;
    private AppBarLayout appBarLayout;
    private String title, overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        this.context = getApplicationContext();

        //Find views
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        img_movie = (ImageView) findViewById(R.id.image);
        tv_title = (TextView) findViewById(R.id.title);
        tv_original_title = (TextView) findViewById(R.id.original_title);
        webView = (WebView) findViewById(R.id.desc);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        genres_recycler_view = (RecyclerView) findViewById(R.id.genres_recycler_view);
        production_companies_rv = (RecyclerView) findViewById(R.id.production_companies_rv);

        setSupportActionBar(toolbar);

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        genres_recycler_view.setHasFixedSize(true);
        genres_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        production_companies_rv.setHasFixedSize(true);
        production_companies_rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        Intent iGet = getIntent();
        int movieId = iGet.getIntExtra("movieId", 0);


        API.movies().movieDetails(movieId, Config.API_KEY).enqueue(new retrofit2.Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                final MovieResponse movie = response.body();

                title = movie.getTitle();
                overview = movie.getOverview();
                if (actionBar != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setTitle(title);
                }

                appBarLayout.setExpanded(true);

                // hiding & showing the title when toolbar expanded & collapsed
                appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    boolean isShow = false;
                    int scrollRange = -1;

                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        if (scrollRange == -1) {
                            scrollRange = appBarLayout.getTotalScrollRange();
                        }
                        if (scrollRange + verticalOffset == 0) {
                            collapsingToolbarLayout.setTitle(title);
                            isShow = true;
                        } else if (isShow) {
                            collapsingToolbarLayout.setTitle(title);
                            isShow = false;
                        }
                    }
                });
                collapsingToolbarLayout.setTitle(title);
                renderMovie(movie);
                progressBar.setVisibility(View.GONE);
                coordinatorLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error loading!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void renderMovie(MovieResponse movie) {
        tv_title.setText(movie.getTitle());
        tv_original_title.setText(context.getString(R.string.tv_original_title, movie.getOriginalTitle(), movie.getReleaseDate().toString()));
        webView.setBackgroundColor(Color.parseColor("#ffffff"));
        webView.setFocusableInTouchMode(false);
        webView.setFocusable(false);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        WebSettings ws = webView.getSettings();
        Resources res = getResources();
        ws.setDefaultFontSize(15);
        String mimeType = "text/html; charset=UTF-8";
        String encoding = "utf-8";
        String text = "<html>"
                + "<head>"
                + "<style type=\"text/css\">body{color: #525252;}"
                + "</style></head>"
                + "<body><h1>Overview:</h1>"
                + movie.getOverview()
                + "</body>"
                + "</html>";

        webView.loadData(text, mimeType, encoding);

        Picasso.with(this)
                .load(Config.IMAGE_URL_BASE_PATH + movie.getBackdropPath())
                .into(img_movie, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) img_movie.getDrawable()).getBitmap();
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {

                            }
                        });
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(getApplicationContext(), "Error loading picture!", Toast.LENGTH_SHORT).show();
                    }
                });

        genres_recycler_view.setAdapter(
                new GenresAdapter(
                        movie.getGenres(),
                        R.layout.list_item_genre,
                        context
                )
        );

        production_companies_rv.setAdapter(
                new ProductionCompaniesAdapter(
                        movie.getProductionCompanies(),
                        R.layout.list_item_pc,
                        context
                )
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_share:
                String formattedString = android.text.Html.fromHtml(overview).toString();
                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_TITLE, title);
                share.putExtra(Intent.EXTRA_PACKAGE_NAME, getPackageName());
                share.putExtra(Intent.EXTRA_TEXT, formattedString);
                share.setType("text/plain");
                startActivity(share);
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
