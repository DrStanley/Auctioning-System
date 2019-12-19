package Admin;

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
import com.stanleyj.android.myauction.R;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AdminDetailedAuctionActivity extends AppCompatActivity {
    FirebaseDatabase mfirebaseDatabase;
    DatabaseReference mdatabaseReference;
    FirebaseAuth firebaseAuth;
    String pid, pnam, pcat, pdis, pSdate, pEdate, acutionerId, uses, mod;
    TextView curbid, name, cate, discrip, startDate, endDate, dPID, status;
    EditText countD;
    int da, mon, yea, hur, mi;
    long cbid;
    ImageView imageView1, imageView2, imageView3;
    Button buttonRej, buttonCon, buttonDel;
    private ProgressDialog pd;

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detailed_auction);
        pd = new ProgressDialog(this);
        curbid = (TextView) findViewById(R.id.AdcurBid);
        name = (TextView) findViewById(R.id.AdName);
        cate = (TextView) findViewById(R.id.AdCategory);
        discrip = (TextView) findViewById(R.id.AdDescription);
        startDate = (TextView) findViewById(R.id.AdSdate);
        endDate = (TextView) findViewById(R.id.AdEdate);
        dPID = (TextView) findViewById(R.id.AdproID);
        status = (TextView) findViewById(R.id.stat);
        countD = (EditText) findViewById(R.id.Acountdown);
        imageView1 = (ImageView) findViewById(R.id.AdImage1);
        imageView2 = (ImageView) findViewById(R.id.AdImage2);
        imageView3 = (ImageView) findViewById(R.id.AdImage3);
        buttonCon = (Button) findViewById(R.id.Confirm);
        buttonDel = (Button) findViewById(R.id.Delete);
        buttonRej = (Button) findViewById(R.id.Reject);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAdmin_details);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        countD.setEnabled(false);
//        getting Extras from intent
        pid = getIntent().getStringExtra("prodid");
        pnam = getIntent().getStringExtra("pname");
        pcat = getIntent().getStringExtra("pcategory");
        pdis = getIntent().getStringExtra("pDescription");
        pSdate = getIntent().getStringExtra("Sdate");
        pEdate = getIntent().getStringExtra("Edate");
        acutionerId = getIntent().getStringExtra("owner");

//        setting value to textview
        name.setText(pnam);
        cate.setText(pcat);
        discrip.setText(pdis);
        startDate.setText(pSdate);
        endDate.setText(pEdate);
        dPID.setText(pid);
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
                uses = dataSnapshot.child("userID").getValue(String.class);
                mod = dataSnapshot.child("progress").getValue(String.class);
                String img1 = dataSnapshot.child("image1").getValue(String.class);
                String img2 = dataSnapshot.child("image2").getValue(String.class);
                String img3 = dataSnapshot.child("image3").getValue(String.class);
                timez();
                try {
                    status.setText(mod);
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

        buttonRej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> updates = new HashMap<String, Object>();

                updates.put("progress", "rejected");

                mdatabaseReference.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                        alertDialog.setTitle("Update " + pid);
                        alertDialog.setMessage("The product  has been rejected");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
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
        });
        buttonCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> updates = new HashMap<String, Object>();

                updates.put("progress", "confirmed");

                mdatabaseReference.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        FirebaseDatabase mfirebaseDatabase2;
                        DatabaseReference mdatabaseReference2;

                        mfirebaseDatabase2 = FirebaseDatabase.getInstance();
                        mdatabaseReference2 = mfirebaseDatabase2.getReference("Alert");

                        Map<String, Object> updates = new HashMap<String, Object>();
                        updates.put("product_id", pid);
                        mdatabaseReference2.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

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
                        AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                        alertDialog.setTitle("Update " + pid);
                        alertDialog.setMessage("The product  has been confirmed");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
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
        });

        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdatabaseReference.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                        alertDialog.setTitle(pid + " Deleted");
                        alertDialog.setMessage("Auction has been deleted");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                    }
                });
            }
        });

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
