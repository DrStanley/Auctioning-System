package com.stanleyj.android.myauction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AuctionDetailActivity extends AppCompatActivity {

    FirebaseDatabase mfirebaseDatabase;
    DatabaseReference mdatabaseReference;
    FirebaseAuth firebaseAuth;
    String pid, pnam, pcat, pdis, pSdate, pEdate, uses, mod;
    TextView curbid, name, cate, discrip, startDate, endDate, dPID;
    EditText countD;
    int da, mon, yea, hur, mi;
    long cbid;
    ImageView imageView1, imageView2, imageView3;
    Button buttonBid;
    EditText editTextBid;
    private ProgressDialog pd;

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_detail);
        pd = new ProgressDialog(this);
        curbid = (TextView) findViewById(R.id.dcurBid);
        name = (TextView) findViewById(R.id.dName);
        cate = (TextView) findViewById(R.id.dCategory);
        discrip = (TextView) findViewById(R.id.dDescription);
        startDate = (TextView) findViewById(R.id.dSdate);
        endDate = (TextView) findViewById(R.id.dEdate);
        dPID = (TextView) findViewById(R.id.dproID);
        countD = (EditText) findViewById(R.id.countdown);
        imageView1 = (ImageView) findViewById(R.id.dImage1);
        imageView2 = (ImageView) findViewById(R.id.dImage2);
        imageView3 = (ImageView) findViewById(R.id.dImage3);
        buttonBid = (Button) findViewById(R.id.butBid);
        editTextBid = (EditText) findViewById(R.id.bids);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarD);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();

        countD.setEnabled(false);

//        getting Extras from intent
        pid = getIntent().getStringExtra("prodid");
        Toast.makeText(AuctionDetailActivity.this, "checking "+pid, Toast.LENGTH_SHORT).show();
        mod = getIntent().getStringExtra("mode");
        if (Objects.equals(mod, "bad")) {
            buttonBid.setVisibility(View.INVISIBLE);
            editTextBid.setVisibility(View.INVISIBLE);
        }

        getSupportActionBar().setTitle(pid);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mdatabaseReference = mfirebaseDatabase.getReference("Auctions").child(pid);
        pd.setMessage(" Loading Data Please wait...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        mdatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                da = dataSnapshot.child("endday").getValue(int.class);
                yea = dataSnapshot.child("endyear").getValue(int.class);
                mon = dataSnapshot.child("endmonth").getValue(int.class);
                hur = dataSnapshot.child("endhour").getValue(int.class);
                mi = dataSnapshot.child("endminute").getValue(int.class);

                int Sda = dataSnapshot.child("startday").getValue(int.class);
                int Syea = dataSnapshot.child("startyear").getValue(int.class);
                int Smon = dataSnapshot.child("startmonth").getValue(int.class);
                int Shur = dataSnapshot.child("starthour").getValue(int.class);
                int Smi = dataSnapshot.child("startminute").getValue(int.class);

                uses = dataSnapshot.child("userID").getValue(String.class);
                pnam = dataSnapshot.child("productName").getValue(String.class);
                pcat = dataSnapshot.child("category").getValue(String.class);
                pdis = dataSnapshot.child("description").getValue(String.class);
                pSdate = "StartDate: " + Sda + "/" + Smon + "/" + Syea
                        + "\t\t\t\t" + Shur + ":" + Smi;

                pEdate = "EndDate: " + da + "/" + mon + "/" + yea
                        + "\t\t\t\t" + hur + ":" + mi;
                String img1 = dataSnapshot.child("image1").getValue(String.class);
                String img2 = dataSnapshot.child("image2").getValue(String.class);
                String img3 = dataSnapshot.child("image3").getValue(String.class);
                timez();
                try {
                    //        setting value to view
                    name.setText(pnam);
                    cate.setText(pcat);
                    discrip.setText(pdis);
                    startDate.setText(pSdate);
                    endDate.setText(pEdate);
                    dPID.setText(pid);
                    imageView1.setImageBitmap(decodeFromFirebaseBase64(img1));
                    imageView2.setImageBitmap(decodeFromFirebaseBase64(img2));
                    imageView3.setImageBitmap(decodeFromFirebaseBase64(img3));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        try {
            mdatabaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    mdatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long biding = dataSnapshot.child("currentBid").getValue(long.class);
                            cbid = biding;
                            curbid.setText("₦" + biding);
                            pd.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    mdatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long biding = dataSnapshot.child("currentBid").getValue(long.class);
                            curbid.setText("₦" + biding);
                            pd.dismiss();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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

            buttonBid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (cbid > Integer.parseInt(editTextBid.getText().toString())) {
                            Toast.makeText(AuctionDetailActivity.this, "Please You cannot bid less than the current bid ₦"
                                    + cbid, Toast.LENGTH_SHORT).show();
                        } else {

                            if (Objects.equals(uses, firebaseAuth.getUid())) {
                                Toast.makeText(AuctionDetailActivity.this, "You Can't Bid for this product", Toast.LENGTH_SHORT).show();
                            } else {

                                Map<String, Object> updates = new HashMap<String, Object>();
                                updates.put("currentBid", Integer.parseInt(editTextBid.getText().toString()));
                                updates.put("winnerID", firebaseAuth.getUid());

                                mdatabaseReference.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(AuctionDetailActivity.this, "Bid ₦"
                                                + Integer.parseInt(editTextBid.getText().toString()) + " made", Toast.LENGTH_SHORT).show();
                                        editTextBid.setText(null);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                                        alertDialog.setTitle("Error Alert");
                                        alertDialog.setMessage("The following Error occurred\n" + e.getMessage());
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    } catch (Exception e) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                        alertDialog.setTitle("Error Alert");
                        alertDialog.setMessage("The following Error occurred\n" + e.getMessage());
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        pd.dismiss();
                                    }
                                });
                    }
                }
            });

        } catch (Exception e) {
            pd.dismiss();
            AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
            alertDialog.setTitle("Error Alert");
            alertDialog.setMessage("The following Error occurred\n" + e.getMessage());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            pd.dismiss();
                        }
                    });
        }

    }

    void timez() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hrs = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        try {
            Calendar calendar = Calendar.getInstance();

            calendar.set(year, month, day, hrs, min, sec);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(yea, mon, da, hur, mi);
            long set = calendar2.getTimeInMillis() - calendar.getTimeInMillis();
            //        countdown
            new CountDownTimer(set, 1000) {

                public void onTick(long millisUntilFinished) {
                    long millis = millisUntilFinished;
                    //Convert milliseconds into hour,minute and seconds
                    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                    countD.setText(hms);//set text
                }

                public void onFinish() {
                    countD.setText("Finished");
                    countD.setTextColor(Color.RED);
                    buttonBid.setClickable(false);
                    editTextBid.setEnabled(false);
                    Map<String, Object> updates = new HashMap<String, Object>();
                    updates.put("status", "end");
                    mdatabaseReference.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            AlertDialog alertDialog = new AlertDialog.Builder(AuctionDetailActivity.this).create();
                            alertDialog.setTitle("Alert");

                            alertDialog.setMessage("Auction Is Over");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    });
                    Toast.makeText(AuctionDetailActivity.this, "You Can't Bid for this product", Toast.LENGTH_LONG).show();

                    buttonBid.setEnabled(false);
                }

            }.start();

        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
            alertDialog.setTitle("Error Alert");
            alertDialog.setMessage("The following Error occurred\n" + e.getMessage());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            pd.dismiss();
                        }
                    });
        }
    }

}