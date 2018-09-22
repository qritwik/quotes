package com.library.apple.quotes;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class sorry extends Fragment{

    View myView;

    private RecyclerView recyclerView;
    private DatabaseReference myref;
    GridLayoutManager layoutManager;
    LottieAnimationView animationView;

    FloatingActionButton fab;
    boolean one1;

    private AdView mAdView;

    @Override
    public void onStart() {

        final FragmentManager fragmentManager = getFragmentManager();


        if(checkInternet()==false)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity(),android.R.style.Theme_Holo_Dialog).create();
            alertDialog.setTitle("Connection Failed");
            alertDialog.setMessage("Please check your internet connection");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    fragmentManager.beginTransaction().replace(R.id.content_frame,new sorry()).commit();
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



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.sorry,container,false);

        MobileAds.initialize(getActivity(), "ca-app-pub-8750480772839804~5657654976");

        AdView adView = new AdView(getActivity());
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-8750480772839804/3385240075");

        mAdView = myView.findViewById(R.id.adView_sorry);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        animationView = (LottieAnimationView)myView.findViewById(R.id.av_sorry);
        animationView.setAnimation("poi.json");
        animationView.playAnimation();
        animationView.setVisibility(View.VISIBLE);
        fab = (FloatingActionButton) myView.findViewById(R.id.fab_sorry);




        recyclerView=(RecyclerView)myView.findViewById(R.id.rv_sorry);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        fab.setImageResource(R.drawable.ic_photo_size_select_actual_black_24dp);
        one1=false;
        myref= FirebaseDatabase.getInstance().getReference().child("sorry");
        myref.keepSynced(true);

        Query myMostViewedPostsQuery = myref.orderByChild("name");


        final FirebaseRecyclerAdapter<Contact,sorry.BlogViewHolder> recyclerAdapter=new FirebaseRecyclerAdapter<Contact,sorry.BlogViewHolder>(
                Contact.class,
                R.layout.row,
                sorry.BlogViewHolder.class,
                myMostViewedPostsQuery
        ) {

            @Override
            protected void populateViewHolder(final sorry.BlogViewHolder viewHolder, final Contact model, final int position) {



                animationView.setVisibility(View.GONE);
                Glide.with(getActivity()).load(model.getUrl()).into(viewHolder.imageView);

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(viewHolder.mContext,image.class);

                        intent.putExtra("url",model.getUrl());
                        intent.putExtra("fav",model.getFav());
                        sorry.this.startActivity(intent);

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

                    GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 1);
                    recyclerView.setLayoutManager(gridLayoutManager1);
                    one1=true;
                    fab.setImageResource(R.drawable.ic_photo_size_select_large_black_24dp);


                    myref = FirebaseDatabase.getInstance().getReference().child("sorry");
                    myref.keepSynced(true);

                    Query myMostViewedPostsQuery = myref.orderByChild("name");


                    final FirebaseRecyclerAdapter<Contact, sorry.BlogViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Contact, sorry.BlogViewHolder>(
                            Contact.class,
                            R.layout.row1,
                            sorry.BlogViewHolder.class,
                            myMostViewedPostsQuery
                    ) {

                        @Override
                        protected void populateViewHolder(final sorry.BlogViewHolder viewHolder, final Contact model, final int position) {


                            animationView.setVisibility(View.GONE);
                            Glide.with(getActivity()).load(model.getUrl()).into(viewHolder.imageView);

                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(viewHolder.mContext, image.class);

                                    intent.putExtra("url", model.getUrl());
                                    intent.putExtra("fav", model.getFav());
                                    intent.putExtra("pos", position);
                                    sorry.this.startActivity(intent);

                                }
                            });


                        }
                    };

                    recyclerAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(recyclerAdapter);

                }

                else if(one1==true){

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    one1=false;
                    fab.setImageResource(R.drawable.ic_photo_size_select_actual_black_24dp);








                    myref= FirebaseDatabase.getInstance().getReference().child("sorry");
                    myref.keepSynced(true);

                    Query myMostViewedPostsQuery = myref.orderByChild("name");



                    final FirebaseRecyclerAdapter<Contact,sorry.BlogViewHolder> recyclerAdapter=new FirebaseRecyclerAdapter<Contact,sorry.BlogViewHolder>(
                            Contact.class,
                            R.layout.row,
                            sorry.BlogViewHolder.class,
                            myMostViewedPostsQuery
                    ) {

                        @Override
                        protected void populateViewHolder(final sorry.BlogViewHolder viewHolder, final Contact model, final int position) {



                            animationView.setVisibility(View.GONE);
                            Glide.with(getActivity()).load(model.getUrl()).into(viewHolder.imageView);

                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(viewHolder.mContext,image.class);

                                    intent.putExtra("url",model.getUrl());
                                    intent.putExtra("fav",model.getFav());
                                    intent.putExtra("pos",position);
                                    sorry.this.startActivity(intent);

                                }
                            });



                        }
                    };

                    recyclerAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(recyclerAdapter);


                }

            }
        });


        return myView;
    }


    public boolean checkInternet()
    {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        return connected;
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
}


