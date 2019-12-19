package com.stanleyj.android.myauction;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class AddNewAuctionActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    Spinner spinner;
    TextView endT, startT, TimS, TimE;
    ArrayList<String> list2 = new ArrayList<String>();
    TextView textView;
    EditText pName, pDesc, bid;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    DatePickerDialog.OnDateSetListener onDateSetListener2;
    TimePickerDialog.OnTimeSetListener onTimeSetListener;
    TimePickerDialog.OnTimeSetListener onTimeSetListener1;
    Button upload;
    String imgUri1, imgUri2, imgUri3, tag;
    int startday, startmonth, startyear, endday, endmonth, endyear, starthour, startminute, endhour, endminute;
    ImageView imageView1, imageView3, imageView2;
    boolean img1, img2, img3;
    Uri imgUri;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_auction);
        //     finding the tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add New Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        views();
        adds();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Auctions");
        pd = new ProgressDialog(this);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, list2);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        clicks();
        SetListener();


    }

    private void SetListener() {
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                startmonth = month;
                startday = day;
                startyear = year;
                String date = day + "/" + month + "/" + year;
                Calendar cal = Calendar.getInstance();
                int year2 = cal.get(Calendar.YEAR);
                int month2 = cal.get(Calendar.MONTH) + 1;
                int day2 = cal.get(Calendar.DAY_OF_MONTH);
                if (day < day2 || month < month2 || year < year2) {
                    Toast.makeText(getApplicationContext(), "Can't Select Date lesser than current", Toast.LENGTH_SHORT).show();
                } else {
                    startT.setText(date);
                }
            }
        };
        onDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                endmonth = month;
                endday = day;
                endyear = year;
                Calendar cal = Calendar.getInstance();
                int year2 = cal.get(Calendar.YEAR);
                int month2 = cal.get(Calendar.MONTH) + 1;
                int day2 = cal.get(Calendar.DAY_OF_MONTH);
                String date = day + "/" + month + "/" + year;
                if (day < day2 & month == month2 & year == year2) {
                    Toast.makeText(getApplicationContext(), "Can't Select Date lesser than current", Toast.LENGTH_SHORT).show();
                } else if (day >= day2 & month < month2 & year == year2) {
                    Toast.makeText(getApplicationContext(), "Can't Select Date lesser than current", Toast.LENGTH_SHORT).show();
                } else {
                    endT.setText(date);

                }
            }
        };
        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                starthour = hour;
                startminute = min;
                String date = hour + ":" + min;
                TimS.setText(date);
            }
        };
        onTimeSetListener1 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                endhour = hour;
                endminute = min;
                String date = hour + ":" + min;
                TimE.setText(date);
            }
        };

    }

    private void adds() {
        list2.add("Select Category");
        list2.add("Jewelleries");
        list2.add("Mobile devices");
        list2.add("Computer Accessories");
        list2.add("Gadgets");
    }

    private void views() {
        spinner = (Spinner) findViewById(R.id.category_spinner1);
        endT = (TextView) findViewById(R.id.End_datePicker);
        startT = (TextView) findViewById(R.id.Start_datePicker);
        TimE = (TextView) findViewById(R.id.End_timePicker);
        TimS = (TextView) findViewById(R.id.Start_timePicker);
        pName = (EditText) findViewById(R.id.Product_name);
        pDesc = (EditText) findViewById(R.id.description);
        bid = (EditText) findViewById(R.id.minBid);
        upload = (Button) findViewById(R.id.auction);
        imageView1 = (ImageView) findViewById(R.id.img1);
        imageView2 = (ImageView) findViewById(R.id.img2);
        imageView3 = (ImageView) findViewById(R.id.img3);
    }

    void clicks() {

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load();
            }
        });

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img1 = true;
                openImage();

            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img2 = true;
                openImage();

            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img3 = true;
                openImage();

            }
        });
        endT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddNewAuctionActivity.this,
                        android.R.style.Widget_Material_Light_DatePicker,
                        onDateSetListener2,
                        year, month, day
                );
                datePickerDialog.setMessage("Choose End Date");
                datePickerDialog.getWindow().setTitle("Choose End Date");
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();
            }
        });
        startT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

//                make date change to miliseconds
                Calendar calendar = new GregorianCalendar(year, month, day);
                calendar.getTimeInMillis();


                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddNewAuctionActivity.this,
                        android.R.style.Widget_Material_Light_DatePicker,
                        onDateSetListener,
                        year, month, day
                );
                datePickerDialog.setMessage("Choose Start Date");
                datePickerDialog.getWindow().setTitle("Choose Start Date");
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();
            }
        });
        TimS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int min = cal.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddNewAuctionActivity.this,
                        android.R.style.Widget_Material_Light_DatePicker,
                        onTimeSetListener, hour, min,
                        DateFormat.is24HourFormat(AddNewAuctionActivity.this));
                timePickerDialog.setMessage("Choose Start Time");
                timePickerDialog.getWindow().setTitle("Choose Start Time");
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                timePickerDialog.show();
            }
        });
        TimE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int min = cal.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddNewAuctionActivity.this,
                        android.R.style.Widget_Material_Light_DatePicker,
                        onTimeSetListener1, hour, min,
                        DateFormat.is24HourFormat(AddNewAuctionActivity.this));
                timePickerDialog.setMessage("Choose End Time");
                timePickerDialog.getWindow().setTitle("Choose End Time");
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                timePickerDialog.show();
            }
        });
    }

    private void load() {
        if (TextUtils.isEmpty(pName.getText().toString())) {
            //product name is empty
            Toast.makeText(this, "Please Enter Product Name", Toast.LENGTH_SHORT).show();
            return;

        }
        if ((spinner.getSelectedItem().toString()).equals("Select Category")) {
            //spinner is selected
            Toast.makeText(this, "Please Select a category", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pDesc.getText().toString())) {
            // product description is empty
            Toast.makeText(this, "Please Enter Description", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(bid.getText().toString())) {
            // product description is empty
            Toast.makeText(this, "Please Enter Description", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TimE.getText().toString().equals("Pick Time")) {
            Toast.makeText(this, "Please Select End Time", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TimS.getText().toString().equals("Pick Time")) {
            Toast.makeText(this, "Please Select Start Time", Toast.LENGTH_SHORT).show();
            return;
        }
        if (endT.getText().toString().equals("Pick Date")) {
            Toast.makeText(this, "Please Select End Date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (startT.getText().toString().equals("Pick Date")) {
            Toast.makeText(this, "Please Select Start Date", Toast.LENGTH_SHORT).show();
            return;
        }
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hrs = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);

        Calendar calendar = new GregorianCalendar(year, month, day, hrs, min, sec);

        tag = "P" + Long.toString(calendar.getTimeInMillis());
        databaseReference = firebaseDatabase.getReference("Auctions");
//        if (startday == day && startyear == year && startmonth == month) {
        try {

            pd.setMessage("Uploading Data...");
            pd.show();
            pd.setCanceledOnTouchOutside(false);
            Drawable mDrawable1 = imageView1.getDrawable();
            Drawable mDrawable2 = imageView2.getDrawable();
            Drawable mDrawable3 = imageView3.getDrawable();
            Bitmap mBitmap1 = ((BitmapDrawable) mDrawable1).getBitmap();
            Bitmap mBitmap2 = ((BitmapDrawable) mDrawable2).getBitmap();
            Bitmap mBitmap3 = ((BitmapDrawable) mDrawable3).getBitmap();
            imgUri1 = encodeBitmapAndSaveToFirebase(mBitmap1);
            imgUri2 = encodeBitmapAndSaveToFirebase(mBitmap2);
            imgUri3 = encodeBitmapAndSaveToFirebase(mBitmap3);

            DatabaseReference databaseReference2 = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).child("Profile");

            databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {

                        System.out.println("datasnap " + dataSnapshot);
                        if (dataSnapshot.hasChildren()) {
                            AuctionModel AM = new AuctionModel(
                                    pName.getText().toString(),
                                    spinner.getSelectedItem().toString(), imgUri1, imgUri2,
                                    imgUri3, pDesc.getText().toString(), startday, startmonth,
                                    startyear, starthour, startminute, endday, endmonth, endyear,
                                    endhour, endminute, firebaseAuth.getUid(),
                                    tag, firebaseAuth.getUid(), Integer.parseInt(bid.getText().toString()),
                                    Integer.parseInt(bid.getText().toString()), "start", "unconfirmed");

                            databaseReference.child(tag).setValue(AM).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    pd.dismiss();

                                    AlertDialog alertDialog = new AlertDialog.Builder(AddNewAuctionActivity.this).create();
                                    alertDialog.setTitle("Alert");
                                    alertDialog.setMessage("Data Uploaded");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    finish();

                                                }
                                            });
                                    alertDialog.show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    AlertDialog alertDialog = new AlertDialog.Builder(AddNewAuctionActivity.this).create();
                                    alertDialog.setTitle("Alert");
                                    alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
                                    alertDialog.setMessage("Failed to Uploaded\n" + "" + e.getMessage() + "\n" + e.getCause());
                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();
                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), "Please update your Profile", Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        pd.dismiss();
                        AlertDialog alertDialog = new AlertDialog.Builder(AddNewAuctionActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
                        alertDialog.setMessage("Failed to Uploaded\n" + "" + e.getMessage() + "\n" + e.getCause());
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } catch (Exception e) {
            pd.dismiss();
            AlertDialog alertDialog = new AlertDialog.Builder(AddNewAuctionActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
            alertDialog.setMessage("Error Uploading" + e.getMessage() + "\n" + e.getCause());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    private void openImage() {
        try {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
        } catch (Exception ignored) {

        }
    }

    public String encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            imgUri = data.getData();

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageInByte = stream.toByteArray();
            long length = imageInByte.length;
            long spec = 500000;
            if (length > spec) {
                AlertDialog alertDialog = new AlertDialog.Builder(AddNewAuctionActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
                alertDialog.setMessage("Error Setting image\n" +
                        "Image size is > 500kb");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return;
            }
            if (img1) {
                imageView1.setImageBitmap(bitmap);
                img1 = false;
            } else if (img2) {
                imageView2.setImageBitmap(bitmap);
                img2 = false;
            } else if (img3) {
                imageView3.setImageBitmap(bitmap);
                img3 = false;
            }

        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(AddNewAuctionActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Error Updating\n" + e.getMessage());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}