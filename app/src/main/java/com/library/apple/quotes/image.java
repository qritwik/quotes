package com.library.apple.quotes;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Random;

public class image extends AppCompatActivity {


    private static final int uniqueID = 54768;

    Context context;
    ProgressBar progressBar;

    private static final int PERMISSION_REQUEST_CODE = 1000;
    ImageView imageView;
    TextView download, share, favourite;
    Toolbar toolbar;
    String url;
    ProgressDialog mProgressDialog;
    private AsyncTask mMyTask;

    String deviceId;
    private DatabaseReference mDatabase;

    File input_file;

    String fav1;
    ImageView heart_btn;
    int pos;
    String mDatabase1;
    String iop;

    private InterstitialAd mInterstitialAd;

    int id1;

    LinearLayout ll_download1,ll_shr,ll_favo;
    private int STORAGE_PERMISSION_CODE = 1;

    private AdView mAdView;

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch (requestCode)
//        {
//            case PERMISSION_REQUEST_CODE:
//            {
//                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                    Toast.makeText(getApplicationContext(),"Permission Granted",Toast.LENGTH_LONG).show();
//                else
//                    Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG).show();
//            }
//            break;
//        }
//    }



    @Override
    protected void onStart() {

        if(checkInternet()==false)
        {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(image.this,android.R.style.Theme_Holo_Dialog).create();
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        MobileAds.initialize(this, "ca-app-pub-8750480772839804~5657654976");

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-8750480772839804/9560600334");

        mAdView = findViewById(R.id.adView_image);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mInterstitialAd = new InterstitialAd(image.this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8750480772839804/7052204104");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        progressBar = (ProgressBar)findViewById(R.id.iop123);
        progressBar.setVisibility(View.GONE);

        ll_download1 = (LinearLayout)findViewById(R.id.ll_download);
        ll_shr = (LinearLayout)findViewById(R.id.ll_share);
        ll_favo = (LinearLayout)findViewById(R.id.ll_fav);





        toolbar = (Toolbar) findViewById(R.id.toolbar123456);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        mDatabase = FirebaseDatabase.getInstance().getReference().child("favourite");


        heart_btn = (ImageView)findViewById(R.id.fav_heart_btn);

        imageView = (ImageView) findViewById(R.id.image123);
        download = (TextView) findViewById(R.id.download);
        share = (TextView) findViewById(R.id.download);
        favourite = (TextView) findViewById(R.id.favourite);

        url = getIntent().getStringExtra("url");
        fav1 = getIntent().getStringExtra("fav");

        pos = getIntent().getIntExtra("pos",1);





        try{
            if(fav1.equalsIgnoreCase("true"))
            {
                heart_btn.setImageResource(R.drawable.yellow);
            }
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }





        Glide.with(getApplicationContext()).load(url).into(imageView);


        deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("favourite").child(deviceId);



        ll_favo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fav1==null || fav1=="false")
                {

                fav1 = "true";
                heart_btn.setImageResource(R.drawable.yellow);


                String name = "abcd";

                final HashMap<String, String> obj = new HashMap<>();
                obj.put("name",name);
                obj.put("url",url);
                obj.put("fav",fav1);


                mDatabase.push().setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Added to Favourite",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
            else if(fav1.equalsIgnoreCase("true"))
                {
                    fav1 = "false";
                    heart_btn.setImageResource(R.drawable.favorite);


                    mDatabase.removeValue();
                    Toast.makeText(getApplicationContext(),"Removed from Favourite",Toast.LENGTH_SHORT).show();







                }
            }
        });




        ll_download1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id1=1;

                if(ContextCompat.checkSelfPermission(image.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                    Download download = new Download();
                    download.execute(url);
                }
                else {
                    requestStoragePermission();

                    }






            }
        });


        ll_shr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id1 = 2;

                if(ContextCompat.checkSelfPermission(image.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                    shareImage share = new shareImage();
                    share.execute(url);
                }
                else {
                    requestStoragePermission();

                }

            }
        });








    }

    private void requestStoragePermission(){

        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Download option needs storage permission")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(image.this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        }
        else{
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == STORAGE_PERMISSION_CODE)
        {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){


                if(id1==1)
                {
                    Download download = new Download();
                    download.execute(url);

                }
                else if(id1==2)
                {
                    shareImage share = new shareImage();
                    share.execute(url);
                }

            }else{
                Toast.makeText(getApplicationContext(),"Permission DENIED",Toast.LENGTH_LONG).show();

            }
        }
    }

    class Download extends AsyncTask<String,Integer,String>
    {



//        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {

//            progressDialog = new ProgressDialog(image.this);
//            progressDialog.setMessage("Download in progress...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progressDialog.setMax(100);
//            progressDialog.setProgress(0);
//            progressDialog.show();

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String path = params[0];
            int file_lenght=0;
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                file_lenght = urlConnection.getContentLength();

                File new_folder = new File("sdcard/Pictures/Quotes");
                if(!new_folder.exists())
                {
                    new_folder.mkdir();
                }
                Random random = new Random();
                iop = String.valueOf(random.nextInt());

                input_file = new File(new_folder, iop+".jpg");
                InputStream inputStream = new BufferedInputStream(url.openStream(),8192);
                byte[] data = new byte[1024];
                int total = 0;
                int count = 0;

                OutputStream outputStream = new FileOutputStream(input_file);
                while ((count=inputStream.read(data))!= -1)
                {
                    total += count;
                    outputStream.write(data,0,count);
                    int progress = (int)total*100/file_lenght;
                    publishProgress(progress);

                }
                inputStream.close();
                outputStream.close();
                addImageToGallery(input_file.getAbsolutePath(),image.this);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Download Complete";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

//            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
//            progressDialog.hide();

            progressBar.setVisibility(View.GONE);



            Uri photoURI = FileProvider.getUriForFile(image.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    input_file);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(photoURI, "image/*");


            showNotification(getApplicationContext(),"Download completed","Click here to view the image",intent);



            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();



            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
        }
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


    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }


    class shareImage extends AsyncTask<String,Integer,String>
    {



        @Override
        protected void onPreExecute() {



            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String path = params[0];
            int file_lenght=0;
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                file_lenght = urlConnection.getContentLength();

                File new_folder = new File("sdcard/Pictures/Quotes_share");
                if(!new_folder.exists())
                {
                    new_folder.mkdir();
                }
                Random random = new Random();
                iop = String.valueOf(random.nextInt());

                input_file = new File(new_folder, iop+".jpg");
                InputStream inputStream = new BufferedInputStream(url.openStream(),8192);
                byte[] data = new byte[1024];
                int total = 0;
                int count = 0;

                OutputStream outputStream = new FileOutputStream(input_file);
                while ((count=inputStream.read(data))!= -1)
                {
                    total += count;
                    outputStream.write(data,0,count);
                    int progress = (int)total*100/file_lenght;
                    publishProgress(progress);

                }
                inputStream.close();
                outputStream.close();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Image shared";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onPostExecute(String result) {

            progressBar.setVisibility(View.GONE);



            Uri photoURI = FileProvider.getUriForFile(image.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    input_file);

            Intent intent = new Intent();

            intent.setAction(Intent.ACTION_SEND);

            intent.setType("image/*");



//            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
//            intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
            intent.putExtra(Intent.EXTRA_STREAM, photoURI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


            try {
                startActivity(Intent.createChooser(intent, "Share this quote"));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "No App Available", Toast.LENGTH_SHORT).show();
            }



        }
    }









    public void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_photo_size_select_large_black_24dp)
                .setContentTitle(title)
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
            Intent intent = new Intent(image.this,Setting.class);
            startActivity(intent);
        }

        if (id == android.R.id.home) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
