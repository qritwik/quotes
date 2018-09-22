package com.library.apple.quotes;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class fav extends Fragment{

    View myView;

    private RecyclerView recyclerView;
    private DatabaseReference myref;
           DatabaseReference mref1;
    GridLayoutManager layoutManager;
    LottieAnimationView animationView;
    String deviceId;
    ImageView heart_btn;

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
                    fragmentManager.beginTransaction().replace(R.id.content_frame,new fav()).commit();
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
        myView = inflater.inflate(R.layout.fav,container,false);

        animationView = (LottieAnimationView)myView.findViewById(R.id.av_fav);
        animationView.setAnimation("love.json");
        animationView.playAnimation();
        animationView.setVisibility(View.VISIBLE);





        recyclerView=(RecyclerView)myView.findViewById(R.id.rv_fav);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);



        String deviceId = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        myref= FirebaseDatabase.getInstance().getReference().child("favourite").child(deviceId);
        myref.keepSynced(true);

        mref1 = FirebaseDatabase.getInstance().getReference().child("favourite");






        final FirebaseRecyclerAdapter<Contact_fav,fav.BlogViewHolder> recyclerAdapter=new FirebaseRecyclerAdapter<Contact_fav,fav.BlogViewHolder>(
                Contact_fav.class,
                R.layout.row,
                fav.BlogViewHolder.class,
                myref
        ) {

            @Override
            protected void populateViewHolder(final fav.BlogViewHolder viewHolder, final Contact_fav model, final int position) {





                animationView.setVisibility(View.GONE);

                Glide.with(getActivity()).load(model.getUrl()).into(viewHolder.imageView);

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(viewHolder.mContext,image.class);

                        intent.putExtra("url",model.getUrl());
                        intent.putExtra("fav",model.getFav());


                        fav.this.startActivity(intent);




                    }
                });



            }




        };

        recyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerAdapter);








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


