package com.library.apple.quotes;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;

    private RecyclerView recyclerView;
    private DatabaseReference myref;
    GridLayoutManager layoutManager;
    LottieAnimationView animationView;

    RelativeLayout relativeLayout;

    FloatingActionButton fab;
    boolean one1;
    private AdView mAdView;


    @Override
    protected void onStart() {

        if(checkInternet()==false)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this,android.R.style.Theme_Holo_Dialog).create();
            alertDialog.setTitle("Connection Failed");
            alertDialog.setMessage("Please check your internet connection");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    startActivity(getIntent());
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialog.show();
            alertDialog.setCancelable(false);
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MobileAds.initialize(this, "ca-app-pub-8750480772839804~5657654976");

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-8750480772839804/8986525894");

        mAdView = findViewById(R.id.adView_home);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);





        relativeLayout = (RelativeLayout)findViewById(R.id.home123);


        animationView = (LottieAnimationView)findViewById(R.id.av_home);
        animationView.setAnimation("redio.json");
        animationView.playAnimation();
        animationView.setVisibility(View.VISIBLE);
        fab = (FloatingActionButton)findViewById(R.id.fab_home);




        recyclerView=(RecyclerView)findViewById(R.id.rv_home);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

        fab.setImageResource(R.drawable.ic_photo_size_select_actual_black_24dp);
        one1=false;






        myref= FirebaseDatabase.getInstance().getReference().child("home");
        myref.keepSynced(true);

        Query myMostViewedPostsQuery = myref.orderByChild("name");



        final FirebaseRecyclerAdapter<Contact,MainActivity.BlogViewHolder> recyclerAdapter=new FirebaseRecyclerAdapter<Contact,MainActivity.BlogViewHolder>(
                Contact.class,
                R.layout.row,
                MainActivity.BlogViewHolder.class,
                myMostViewedPostsQuery
        ) {

            @Override
            protected void populateViewHolder(final MainActivity.BlogViewHolder viewHolder, final Contact model, final int position) {



                animationView.setVisibility(View.GONE);
                Glide.with(getApplicationContext()).load(model.getUrl()).into(viewHolder.imageView);

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(viewHolder.mContext,image.class);

                        intent.putExtra("url",model.getUrl());
                        intent.putExtra("fav",model.getFav());
                        intent.putExtra("pos",position);
                        MainActivity.this.startActivity(intent);

                    }
                });



            }
        };

        recyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerAdapter);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(one1==false) {



                    animationView.playAnimation();
                    animationView.setVisibility(View.VISIBLE);

                    GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getApplicationContext(), 1);
                    recyclerView.setLayoutManager(gridLayoutManager1);
                    one1=true;
                    fab.setImageResource(R.drawable.ic_photo_size_select_large_black_24dp);


                    myref = FirebaseDatabase.getInstance().getReference().child("home");
                    myref.keepSynced(true);

                    Query myMostViewedPostsQuery = myref.orderByChild("name");


                    final FirebaseRecyclerAdapter<Contact, MainActivity.BlogViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Contact, MainActivity.BlogViewHolder>(
                            Contact.class,
                            R.layout.row1,
                            MainActivity.BlogViewHolder.class,
                            myMostViewedPostsQuery
                    ) {

                        @Override
                        protected void populateViewHolder(final MainActivity.BlogViewHolder viewHolder, final Contact model, final int position) {


                            animationView.setVisibility(View.GONE);
                            Glide.with(getApplicationContext()).load(model.getUrl()).into(viewHolder.imageView);

                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(viewHolder.mContext, image.class);

                                    intent.putExtra("url", model.getUrl());
                                    intent.putExtra("fav", model.getFav());
                                    intent.putExtra("pos", position);
                                    MainActivity.this.startActivity(intent);

                                }
                            });


                        }
                    };

                    recyclerAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(recyclerAdapter);

                }

                else if(one1==true){

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    one1=false;
                    fab.setImageResource(R.drawable.ic_photo_size_select_actual_black_24dp);








                    myref= FirebaseDatabase.getInstance().getReference().child("home");
                    myref.keepSynced(true);

                    Query myMostViewedPostsQuery = myref.orderByChild("name");



                    final FirebaseRecyclerAdapter<Contact,MainActivity.BlogViewHolder> recyclerAdapter=new FirebaseRecyclerAdapter<Contact,MainActivity.BlogViewHolder>(
                            Contact.class,
                            R.layout.row,
                            MainActivity.BlogViewHolder.class,
                            myMostViewedPostsQuery
                    ) {

                        @Override
                        protected void populateViewHolder(final MainActivity.BlogViewHolder viewHolder, final Contact model, final int position) {



                            animationView.setVisibility(View.GONE);
                            Glide.with(getApplicationContext()).load(model.getUrl()).into(viewHolder.imageView);

                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(viewHolder.mContext,image.class);

                                    intent.putExtra("url",model.getUrl());
                                    intent.putExtra("fav",model.getFav());
                                    intent.putExtra("pos",position);
                                    MainActivity.this.startActivity(intent);

                                }
                            });



                        }
                    };

                    recyclerAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(recyclerAdapter);


                }

            }
        });






//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);





    }

    public boolean checkInternet()
    {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        return connected;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent intent = new Intent(MainActivity.this,Setting.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager fragmentManager = getFragmentManager();

        relativeLayout.setVisibility(View.GONE);
        if (id == R.id.success) {

            fragmentManager.beginTransaction().replace(R.id.content_frame,new success()).commit();
            toolbar.setTitle("Success");

            } else if (id == R.id.life) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new life()).commit();
            toolbar.setTitle("Life");

        } else if (id == R.id.student) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new student()).commit();
            toolbar.setTitle("Student");

        } else if (id == R.id.teacher) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new teacher()).commit();
            toolbar.setTitle("Teacher");

        } else if (id == R.id.friend) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new friend()).commit();
            toolbar.setTitle("Friendship");


        } else if (id == R.id.love) {



            fragmentManager.beginTransaction().replace(R.id.content_frame,new love()).commit();
            toolbar.setTitle("Love");



        }
        else if (id == R.id.morning) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new morning()).commit();
            toolbar.setTitle("Morning Wishes");

        }
        else if (id == R.id.anniversary) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new anniversary()).commit();
            toolbar.setTitle("Anniversary Wishes");

        }
        else if (id == R.id.relationship) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new relationship()).commit();
            toolbar.setTitle("Relationship");

        }
        else if (id == R.id.depression) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new depression()).commit();
            toolbar.setTitle("Depression");

        }else if (id == R.id.angry) {

            fragmentManager.beginTransaction().replace(R.id.content_frame,new angry()).commit();
            toolbar.setTitle("Angry");

        }else if (id == R.id.sorry) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new sorry()).commit();
            toolbar.setTitle("Sorry");

        }else if (id == R.id.hate) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new hate()).commit();
            toolbar.setTitle("Hate");

        }else if (id == R.id.fav) {

            fragmentManager.beginTransaction().replace(R.id.content_frame,new fav()).commit();
            toolbar.setTitle("Favourites");

        }else if (id == R.id.home) {



            drawer.closeDrawer(GravityCompat.START);

            Handler h =new Handler() ;

            h.postDelayed(new Runnable() {

                public void run() {

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }},700);





        }



        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public static class BlogViewHolder extends RecyclerView.ViewHolder  {
        View mView;
        Context mContext;
        ImageView imageView;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mContext = itemView.getContext();
            imageView = (ImageView)itemView.findViewById(R.id.img3);



        }


    }


    boolean click1 = true;

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if(click1==true)
            {
                Toast.makeText(getApplicationContext(),"Press again to exit.",Toast.LENGTH_SHORT).show();
                click1=false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        click1=true;

                    }
                },3000);
            }
            else if(click1==false)
            {
                finishAffinity();
                System.exit(0);
            }

        }



    }
}
