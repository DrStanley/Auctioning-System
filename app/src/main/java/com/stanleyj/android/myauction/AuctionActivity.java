package com.stanleyj.android.myauction;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class AuctionActivity extends AppCompatActivity implements View.OnClickListener {
    Button myAuc, aucList, winList;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle barDrawerToggle;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_menu);
//     finding the tool bar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Options");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        myAuc = (Button) findViewById(R.id.myAuction);
        aucList = (Button) findViewById(R.id.auction_list);
        winList = (Button) findViewById(R.id.winner_list);
        fadeIn2(myAuc);
        fadeIn2(aucList);
        fadeIn2(winList);
        myAuc.setOnClickListener(this);
        aucList.setOnClickListener(this);
        winList.setOnClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        barDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(barDrawerToggle);
        barDrawerToggle.syncState();
        //         set back button to action bar
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        databaseReference = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Profile");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    System.out.println("datasnap " + dataSnapshot);
                    if (dataSnapshot.getValue() == null) {
                        return;
                    } else {
                        String aha = firebaseAuth.getCurrentUser().getEmail();
                        String img = dataSnapshot.child("image").getValue(String.class);

                        ImageView h_img = (ImageView) findViewById(R.id.h_image);
                        TextView h_txt = (TextView) findViewById(R.id.h_email);
                        Bitmap bb = decodeFromFirebaseBase64(img);
                        h_img.setImageBitmap(bb);
                        h_txt.setText(aha);

                    }

                } catch (Exception e) {
                    AlertDialog alertDialog = new AlertDialog.Builder(AuctionActivity.this).create();
                    alertDialog.setTitle("Update Alert");
                    alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
                    alertDialog.setMessage("Please Update Profile\n" + e.getMessage());
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        FirebaseDatabase mfirebaseDatabase2;
        DatabaseReference mdatabaseReference2;

        mfirebaseDatabase2 = FirebaseDatabase.getInstance();
        mdatabaseReference2 = mfirebaseDatabase2.getReference("Alert");
        mdatabaseReference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String produtc = dataSnapshot.child("product_id").getValue(String.class);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String produtc = dataSnapshot.child("product_id").getValue(String.class);
                Intent intent = new Intent(AuctionActivity.this, AuctionDetailActivity.class);
                intent.putExtra("prodid", produtc);
//                int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
                PendingIntent pendingIntent = PendingIntent.getActivity(AuctionActivity.this, 0, intent.putExtra("prodid", produtc), PendingIntent.FLAG_CANCEL_CURRENT);
                NotificationCompat.Builder b = new NotificationCompat.Builder(AuctionActivity.this);
                b.setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(AuctionActivity.this.getResources(), R.mipmap.ic_launcher))
                        .setTicker(produtc)
                        .setContentTitle("New Auction Item")
                        .setContentText("This product was added recently click to view")
                        .setPriority(Notification.PRIORITY_MAX)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentIntent(pendingIntent)
                        .setContentInfo("Alert");
                NotificationManager notificationManager = (NotificationManager) AuctionActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, b.build());


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        NavigationView nV = (NavigationView) findViewById(R.id.nav_view);
        nV.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int i = item.getItemId();
                Toast.makeText(AuctionActivity.this, "Please wait....", Toast.LENGTH_SHORT).show();
                if (i == R.id.hep) {
                    Intent dep = new Intent(AuctionActivity.this, HelpActivity.class);
                    startActivity(dep);
                    return true;
                } else if (i == R.id.payAuction) {
                    Intent dep = new Intent(AuctionActivity.this, PaymentActivity.class);
                    startActivity(dep);
                    return true;
                } else if (i == R.id.complain) {
                    Intent dep = new Intent(AuctionActivity.this, ComplainActivity.class);
                    startActivity(dep);
                    return true;
                } else if (i == R.id.logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent z = new Intent(getApplicationContext(), MainActivity.class);
                    z.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    z.putExtra("Exit", true);
                    startActivity(z);
                    return true;
                } else if (i == R.id.prof) {
                    Intent dep = new Intent(AuctionActivity.this, ProfileActivity.class);
                    startActivity(dep);
                    return true;
                } else if (i == R.id.hot) {
                    makecall();
                    return true;
                } else if (i == R.id.action_settings){
                    Intent dep = new Intent(AuctionActivity.this, SettingsActivity.class);
                    startActivity(dep);
                    return true;
                }
                return true;
            }
        });


    }

    private void makecall() {
        Intent calls = new Intent(Intent.ACTION_DIAL);
        calls.setData(Uri.parse("tel:+2348182878405"));
        startActivity(calls);
    }

    private void fadeIn2(View view) {
//        creating a fade-in animation
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
//        how long will it take in milliseconds ie 1000msec = 1sec\
        animation.setDuration(2000);
//        start animation
        view.startAnimation(animation);
//        make view visible after animation
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Menu inflater
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.help) {
            Intent dep = new Intent(AuctionActivity.this, HelpActivity.class);
            startActivity(dep);
        }
        if (barDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view == myAuc) {
            startActivity(new Intent(getApplicationContext(), MyAuctionActivity.class));

        }
        if (view == aucList) {
            Intent i = new Intent(getBaseContext(), AllAuctionActivity.class);
            startActivity(i);
        }
        if (view == winList) {
            Toast.makeText(getApplicationContext(), "You selected Winners List Auction", Toast.LENGTH_SHORT).show();
        }
    }

}