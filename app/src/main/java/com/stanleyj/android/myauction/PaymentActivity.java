package com.stanleyj.android.myauction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class PaymentActivity extends AppCompatActivity {

    Button very, pross;
    EditText prodotId;
    TextView proname, pid, des, bid, cat, name;
    LinearLayout linearLayout;
    ImageView img1, img2, img3;
    FirebaseDatabase mfirebaseDatabase;
    DatabaseReference mdatabaseReference;
    FirebaseAuth firebaseAuth;
    ProgressDialog pd;
    int payamount;
    String pds;

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(this);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPay);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Confirm Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViews();

        very = (Button) findViewById(R.id.verify);
        pross = (Button) findViewById(R.id.proceed);
        linearLayout.setVisibility(View.INVISIBLE);
        pross.setVisibility(View.INVISIBLE);
        very.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pds = prodotId.getText().toString();
                if (!pds.contains("P") | pds.length() != 14) {
                    Toast.makeText(PaymentActivity.this, "Invalid ID", Toast.LENGTH_SHORT).show();
                    prodotId.setEnabled(true);
                    return;
                }
                try {
                    firebaseAuth = FirebaseAuth.getInstance();
                    mfirebaseDatabase = FirebaseDatabase.getInstance();
                    mdatabaseReference = mfirebaseDatabase.getReference("Auctions").child(pds);
                    prodotId.setEnabled(false);

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
                                    String ids = dataSnapshot.child("prodID").getValue(String.class);
                                    String category = dataSnapshot.child("category").getValue(String.class);
                                    String productName = dataSnapshot.child("productName").getValue(String.class);
                                    String descript = dataSnapshot.child("description").getValue(String.class);
                                    int highest = dataSnapshot.child("currentBid").getValue(int.class);
                                    payamount = highest;
                                    try {
                                        pid.setText(ids);
                                        cat.setText(category);
                                        bid.setText("Winning Bid:₦" + highest);
                                        name.setText(productName);
                                        des.setText(descript);
                                        img1.setImageBitmap(decodeFromFirebaseBase64(im1));
                                        img2.setImageBitmap(decodeFromFirebaseBase64(im2));
                                        img3.setImageBitmap(decodeFromFirebaseBase64(im3));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    linearLayout.setVisibility(View.VISIBLE);
                                    pross.setVisibility(View.VISIBLE);
                                    pd.dismiss();
                                } else {
                                    AlertDialog alertDialog = new AlertDialog.Builder(PaymentActivity.this).create();
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
                                AlertDialog alertDialog = new AlertDialog.Builder(PaymentActivity.this).create();
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
                            prodotId.setEnabled(true);

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
                    Toast.makeText(PaymentActivity.this, "Invalid Product ID", Toast.LENGTH_SHORT).show();
                }
            }
        });
        pross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PaymentActivity.this,PaymentFinalActivity.class).putExtra("proid",pds));

            }
        });
    }

    private void findViews() {
        proname = (TextView) findViewById(R.id.payName);
        pid = (TextView) findViewById(R.id.payproID);
        des = (TextView) findViewById(R.id.payDescription);
        bid = (TextView) findViewById(R.id.payBid);
        cat = (TextView) findViewById(R.id.payCategory);
        name = (TextView) findViewById(R.id.payName);
        img1 = (ImageView) findViewById(R.id.payImage1);
        linearLayout = (LinearLayout) findViewById(R.id.lay);
        img2 = (ImageView) findViewById(R.id.payImage2);
        img3 = (ImageView) findViewById(R.id.payImage3);
        prodotId = (EditText) findViewById(R.id.product_id);
    }


}
