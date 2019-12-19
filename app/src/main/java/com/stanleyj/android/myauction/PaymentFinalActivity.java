package com.stanleyj.android.myauction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Objects;

public class PaymentFinalActivity extends AppCompatActivity {

    FirebaseDatabase mfirebaseDatabase;
    DatabaseReference mdatabaseReference;
    FirebaseAuth firebaseAuth;
    ProgressDialog pd;
    String pid;
    Button masterButton,otherButton;

    TextView proname, amount, tax ,total;
    ImageView img1, img2, img3;

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_final);
        findViews();
        pd = new ProgressDialog(this);
        //        getting Extras from intent
        pid = getIntent().getStringExtra("proid");

        masterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PaymentFinalActivity.this, "Master Card option is not yet available", Toast.LENGTH_SHORT).show();
            }
        });
        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PaymentFinalActivity.this, "Other option is not yet available", Toast.LENGTH_SHORT).show();
            }
        });

        try {
            firebaseAuth = FirebaseAuth.getInstance();
            mfirebaseDatabase = FirebaseDatabase.getInstance();
            mdatabaseReference = mfirebaseDatabase.getReference("Auctions").child(pid);
            pd.setMessage(" Loading Data Please wait...");
            pd.setCanceledOnTouchOutside(false);
            pd.show();
            mdatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String idd = dataSnapshot.child("winnerID").getValue(String.class);
                    String stats = dataSnapshot.child("status").getValue(String.class);
                    if (Objects.equals(stats, "end")) {
                        if (Objects.equals(idd, firebaseAuth.getUid())) {
                            String im1 = dataSnapshot.child("image1").getValue(String.class);
                            String im2 = dataSnapshot.child("image2").getValue(String.class);
                            String im3 = dataSnapshot.child("image3").getValue(String.class);
                            String productName = dataSnapshot.child("productName").getValue(String.class);
                            int highest = dataSnapshot.child("currentBid").getValue(int.class);
                            try {
                                proname.setText(productName);
                                tax.setText("₦" + 80);
                                amount.setText("₦"+highest);
                                total.setText("₦"+(80+highest));
                                img1.setImageBitmap(decodeFromFirebaseBase64(im1));
                                img2.setImageBitmap(decodeFromFirebaseBase64(im2));
                                img3.setImageBitmap(decodeFromFirebaseBase64(im3));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            pd.dismiss();
                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(PaymentFinalActivity.this).create();
                            alertDialog.setTitle("Winner ?");
                            alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
                            alertDialog.setMessage("Sorry this product is not won by you ");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                            pd.dismiss();
                        }
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(PaymentFinalActivity.this).create();
                        alertDialog.setTitle("Auctioning");
                        alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
                        alertDialog.setMessage("Sorry this product is still in auction ");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        pd.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
                    alertDialog.setMessage("Couldn't Login User \n"
                            + databaseError.getMessage());
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            });
        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
            alertDialog.setTitle("Alert");
            alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
            alertDialog.setMessage("Couldn't Login User \n"
                    + e.getMessage());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();        }

    }

    private void findViews() {
        proname = (TextView) findViewById(R.id.finalName);
        amount = (TextView) findViewById(R.id.finalamount);
        tax = (TextView) findViewById(R.id.tax);
        total = (TextView) findViewById(R.id.sum);
        otherButton = (Button) findViewById(R.id.others);
        masterButton = (Button) findViewById(R.id.mastercard);
        img1 = (ImageView) findViewById(R.id.payImage1);
        img2 = (ImageView) findViewById(R.id.payImage2);
        img3 = (ImageView) findViewById(R.id.payImage3);
    }
}
