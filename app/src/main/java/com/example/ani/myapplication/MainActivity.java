package com.example.ani.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.ConstraintSet.GONE;
import static com.example.ani.myapplication.FlickrApi.FLICKRKEY;
import static com.example.ani.myapplication.FlickrApi.FLICKR_SEARCH_METHOD;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        RecyclerView recyclerView;
        List<Photo> list;
        int thispage;
        ProgressBar progress;
        Boolean isScrolling = true;
        int currentitems,totalitem,scrolledoutitem;
        private  SearchView searchView;
        private MyAdapter myAdapter;
        private  FloatingActionButton fab;
        LinearLayout add,delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerlist);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        progress = (ProgressBar) findViewById(R.id.progress);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         fab = (FloatingActionButton) findViewById(R.id.fab);
         add = (LinearLayout) findViewById(R.id.addll);
         delete = (LinearLayout) findViewById(R.id.deletell);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(add.getVisibility()== View.VISIBLE){
                add.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                }
                else
                {
                    add.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);

                }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentitems = layoutManager.getChildCount();
                totalitem = layoutManager.getItemCount();
                scrolledoutitem = layoutManager.findFirstVisibleItemPosition();
                if( (currentitems+scrolledoutitem == totalitem)){
                    isScrolling = false;
                    getData(searchView.getQuery().toString(),thispage +1);
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.material_search_menu, menu);
         searchView = (SearchView) (menu.findItem(R.id.action_search)).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                getData(s,1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getData(s,1);

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private  void getData(String text, final int page){
        progress.setVisibility(View.VISIBLE);
        Call<Flickrsearch> flickrsearchCall = FlickrApi.getService().getImages(FLICKR_SEARCH_METHOD,FLICKRKEY,text,5,page,"json",1);
            flickrsearchCall.enqueue(new Callback<Flickrsearch>() {
                @Override
                public void onResponse(Call<Flickrsearch> call, Response<Flickrsearch> response) {
                        Flickrsearch flickrsearch = response.body();
                    if(flickrsearch!=null) {
                        Photos photos = flickrsearch.getPhotos();

                        if(photos!=null){
                            thispage =photos.getPage();
                            if(page == 1)
                            {
                                list = photos.getPhoto();
                                myAdapter = new MyAdapter(list);
                                recyclerView.setAdapter(new MyAdapter(list));


                            }
                            else
                            {
                                list.addAll(photos.getPhoto());
                                myAdapter.notifyDataSetChanged();

                            }

                        }
                    }
                    progress.setVisibility(View.GONE);

                    isScrolling = true;
                        Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Flickrsearch> call, Throwable t) {

                    Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }
            });
    }

}
