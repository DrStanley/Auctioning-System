package com.stanleyj.android.myauction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;

    Button updateButton;
    Uri imgUri;
    TextView textView;
    FirebaseUser use;
    ImageView prof_pix;
    String spin, cont;
    ImageButton eddy;
    ArrayList<String> list2 = new ArrayList<String>();
    Spinner spinner;
    DatabaseReference databaseReference;
    String name, address, date_birth, occup, phone_num, ail, mims;
    EditText nam, adrex, dob, occ, phn, nins;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    boolean choose_image = false;
    private ProgressDialog pd, npd;

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addState();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        eddy = (ImageButton) findViewById(R.id.edit);
        prof_pix = (ImageView) findViewById(R.id.profile_image);
        use = firebaseAuth.getCurrentUser();
        spinner = (Spinner) findViewById(R.id.state_spinner);
        views();
        pd = new ProgressDialog(this);
        npd = new ProgressDialog(this);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, list2);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


//        Shared preference
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        Shared preference to get the extra data stored;
        final SharedPreferences.Editor editor = sharedPref.edit();

        String piss = sharedPref.getString("Username", null);

        final boolean check_update = sharedPref.getBoolean("Updated", false);
        editor.apply();
        textView.setText(piss);

//        selecting profile pix from device
        prof_pix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        eddy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enab(true);
            }
        });

        editor.apply();
        System.out.println(check_update + " Checked");
        if (check_update) {
            enab(false);
            pd.setMessage("Loading Data...");
            pd.show();
            pd.setCanceledOnTouchOutside(false);

            databaseReference = firebaseDatabase.getReference("Users").child(use.getUid()).child("Profile");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {

                        System.out.println("datasnap " + dataSnapshot);
                        if (dataSnapshot.getValue() == null) {
                            pd.dismiss();
                            enab(true);
                            return;

                        } else {
                            enab(false);
                            String address = dataSnapshot.child("address").getValue(String.class);
                            String aha = dataSnapshot.child("name").getValue(String.class);
                            String age = dataSnapshot.child("DOB").getValue(String.class);
                            String occu = dataSnapshot.child("occupation").getValue(String.class);
                            String phun = dataSnapshot.child("phone_num").getValue(String.class);
                            String stat = dataSnapshot.child("state").getValue(String.class);
                            String img = dataSnapshot.child("image").getValue(String.class);
                            String sis = dataSnapshot.child("nin").getValue(String.class);
                            nam.setText(aha);
                            adrex.setText(address);
                            dob.setText(age);
                            occ.setText(occu);
                            phn.setText(phun);
                            nins.setText(sis);
                            Bitmap bb = decodeFromFirebaseBase64(img);
                            prof_pix.setImageBitmap(bb);
                            System.out.println("State " + stat);
                            setSpinnerText(spinner, stat);
                            pd.dismiss();
                        }
                        ail = sharedPref.getString("Username", null);
                        editor.apply();
                        textView.setText(ail);
                    } catch (Exception e) {
                        AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
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

        } else {
            System.out.println("INVISIBLE");
        }

        updateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "Loading......", Toast.LENGTH_SHORT).show();
                databaseReference = firebaseDatabase.getReference("Users").child(use.getUid()).child("Profile");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            update();
                        } else {
                            update2();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    private void openImage() {
        try {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
        } catch (Exception ignored) {

        }
    }

    private void
    update2() {
//        Toast.makeText(getApplicationContext(), "update 2 running", Toast.LENGTH_SHORT).show();
        name = nam.getText().toString();
        address = adrex.getText().toString();
        date_birth = dob.getText().toString();
        occup = occ.getText().toString();
        phone_num = phn.getText().toString();
        mims = nins.getText().toString();
        if (TextUtils.isEmpty(name)) {
            //name is empty
            Toast.makeText(this, "Please Enter name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mims)) {
            //NIN is empty
            Toast.makeText(this, "Please Enter NIN", Toast.LENGTH_SHORT).show();
            return;

        }
        if (TextUtils.isEmpty(address)) {
            //address is empty
            Toast.makeText(this, "Please Enter address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(date_birth)) {
            //date_birth is empty
            Toast.makeText(this, "Please Enter date of birth", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(occup)) {
            //occupation number is empty
            Toast.makeText(this, "Please Enter occupation", Toast.LENGTH_SHORT).show();
            return;
        }
        spin = spinner.getSelectedItem().toString();

        if (spin.equals("Select State")) {
            Toast.makeText(getApplicationContext(), "Please Select a state", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mims.length() != 11) {
            Toast.makeText(ProfileActivity.this, "Invalid NIN", Toast.LENGTH_SHORT).show();
            return;
        }
        if (date_birth.length() != 4) {
            Toast.makeText(ProfileActivity.this, "Incorrect Date of Birth", Toast.LENGTH_SHORT).show();
            return;
        }
//        gets the model from UserInformation to update into the firebase database
        try {
            Drawable mDrawable = prof_pix.getDrawable();
            Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
            String imgUri = encodeBitmapAndSaveToFirebase(mBitmap);
            updateButton.setEnabled(false);
            Map<String, Object> updates = new HashMap<String, Object>();
            npd.setMessage("Saving Data Please wait...");
            npd.show();
            updates.put("DOB", date_birth);
            updates.put("address", address);
            updates.put("image", imgUri);
            updates.put("name", name);
            updates.put("nin", mims);
            updates.put("occupation", occup);
            updates.put("phone_num", phone_num);
            updates.put("state", spin);
            databaseReference.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    npd.dismiss();

                    AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
                    alertDialog.setTitle("Alert");

                    alertDialog.setMessage("Information Saved");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    enab(false);
                                    updateButton.setEnabled(true);
                                }
                            });
                    alertDialog.show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    npd.dismiss();
                    AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
                    alertDialog.setMessage("Error Saving: " + e.getMessage() + "\nCause: " + e.getCause());
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    enab(false);
                                    updateButton.setEnabled(true);
                                }
                            });
                    alertDialog.show();
                }
            });

        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Error Updating\n" + e.getMessage());
            alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);

            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }

        SharedPreferences Pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor piss = Pref.edit();
        piss.putBoolean("Updated", true);
        piss.putBoolean("Login", true);
        piss.putString("Username", ail);
        piss.apply();
        AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
        alertDialog.setTitle("Alert");
        System.out.println(Pref.getBoolean("Updated", false) + " here 2" + Pref.getBoolean("Login", false));

        alertDialog.setMessage("Information Updated");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        enab(false);

                    }
                });
        alertDialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SharedPreferences Pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor piss = Pref.edit();

        super.onActivityResult(requestCode, resultCode, data);
        try {
//        if (requestCode == RESULT_OK && requestCode == PICK_IMAGE) {
            try {
                imgUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imageInByte = stream.toByteArray();
                long length = imageInByte.length;
                long spec = 500000;
                if (length > spec) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Error Setting image\n" +
                            "Image size is > 500kb");
                    alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }
//                setting bitmap into imageview
                prof_pix.setImageBitmap(bitmap);
                choose_image = true;
            } catch (Exception e) {
                AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
                alertDialog.setMessage("Error Updating\n" + e.getMessage());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
//                        Intent u = new Intent(ProfileActivity.this, AuctionActivity.class);
//                        startActivity(u);
//                        finish();
//                            enab(false);

                            }
                        });
            }

            cont = imgUri.toString();
            piss.putString("value", cont);
            System.out.println("from " + imgUri + " this is the uri" + cont + piss);

//        } else {
//            Toast.makeText(getApplicationContext(), "cant set image", Toast.LENGTH_SHORT).show();
//        }
        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);

            alertDialog.setMessage("Error Updating\n" + e.getMessage());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
//                        Intent u = new Intent(ProfileActivity.this, AuctionActivity.class);
//                        startActivity(u);
//                        finish();
//                            enab(false);

                        }
                    });
        }
    }

    public String encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        return imageEncoded;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), AuctionActivity.class);
        startActivity(i);
        finish();
    }

    private void update() {
        name = nam.getText().toString();
        address = adrex.getText().toString();
        date_birth = dob.getText().toString();
        occup = occ.getText().toString();
        phone_num = phn.getText().toString();
        mims = nins.getText().toString();

        if (TextUtils.isEmpty(name)) {
            //name is empty
            Toast.makeText(this, "Please Enter name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mims)) {
            //NIN is empty
            Toast.makeText(this, "Please Enter NIN", Toast.LENGTH_SHORT).show();
            return;

        }
        if (TextUtils.isEmpty(address)) {
            //address is empty
            Toast.makeText(this, "Please Enter address", Toast.LENGTH_SHORT).show();
            return;

        }
        if (TextUtils.isEmpty(date_birth)) {
            //date_birth is empty
            Toast.makeText(this, "Please Enter date of birth", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(occup)) {
            //occupation number is empty
            Toast.makeText(this, "Please Enter occupation", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!choose_image) {
            //image is not selected number
            Toast.makeText(this, "Please choose a picture", Toast.LENGTH_SHORT).show();
            return;
        }
        spin = spinner.getSelectedItem().toString();

        if (spin.equals("Select State")) {
            Toast.makeText(getApplicationContext(), "Please Select a state", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mims.length() != 11) {
            Toast.makeText(ProfileActivity.this, "Invalid NIN", Toast.LENGTH_SHORT).show();
            return;
        }
        if (date_birth.length() != 4) {
            Toast.makeText(ProfileActivity.this, "Incorrect Date of Birth", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            npd.setMessage("Saving Data Please wait...");
            npd.show();
            Drawable mDrawable = prof_pix.getDrawable();
            Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
            String imgUri = encodeBitmapAndSaveToFirebase(mBitmap);
            UserInformation UI = new UserInformation(name, address, date_birth, occup, phone_num, spin, imgUri, mims);
            databaseReference.setValue(UI).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    npd.dismiss();
                    AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
                    alertDialog.setTitle("Alert");

                    alertDialog.setMessage("Information Saved");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    enab(false);
                                }
                            });
                    alertDialog.show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    npd.dismiss();
                    AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);

                    alertDialog.setMessage("Error Saving: " + e.getMessage() + "\nCause: " + e.getCause());
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    enab(false);
                                    updateButton.setEnabled(true);
                                }
                            });
                    alertDialog.show();
                }
            });

        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Error Updating\n" + e.getMessage());
            alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);

            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
        SharedPreferences Pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor piss = Pref.edit();
        piss.putBoolean("Updated", true);
        piss.putBoolean("Login", true);
        piss.putString("Username", ail);
        piss.apply();
        System.out.println(Pref.getBoolean("Updated", false) + " here 2" + Pref.getBoolean("Login", false));


    }


    private void views() {
        textView = (TextView) findViewById(R.id.mail);
        nam = (EditText) findViewById(R.id.name);
        adrex = (EditText) findViewById(R.id.addrex);
        nins = (EditText) findViewById(R.id.nin);
        phn = (EditText) findViewById(R.id.phn);
        dob = (EditText) findViewById(R.id.dob);
        occ = (EditText) findViewById(R.id.occupation);
        updateButton = (Button) findViewById(R.id.update);

    }

    void enab(boolean tex) {
        nam.setEnabled(tex);
        adrex.setEnabled(tex);
        phn.setEnabled(tex);
        dob.setEnabled(tex);
        spinner.setEnabled(tex);
        updateButton.setEnabled(tex);
        occ.setEnabled(tex);
        nins.setEnabled(tex);

    }

    void addState() {
        list2.add("Select State");
        list2.add("Abia State");
        list2.add("Adamawa State");
        list2.add("Akwa Ibom State");
        list2.add("Anambra State");
        list2.add("Bauchi State");
        list2.add("Bayelsa State");
        list2.add("Benue State");
        list2.add("Borno State");
        list2.add("Cross River State");
        list2.add("Delta State");
        list2.add("Ebonyi State");
        list2.add("Edo State");
        list2.add("Ekiti State");
        list2.add("Enugu State");
        list2.add("Federal Capital Territory");
        list2.add("Gombe State");
        list2.add("Imo State");
        list2.add("Jigawa State");
        list2.add("Kaduna State");
        list2.add("Kano State");
        list2.add("Katsina State");
        list2.add("Kebbi State");
        list2.add("Kogi State");
        list2.add("Kwara State");
        list2.add("Lagos State");
        list2.add("Nasarawa State");
        list2.add("Niger State");
        list2.add("Ogun State");
        list2.add("Ondo State");
        list2.add("Osun State");
        list2.add("Oyo State");
        list2.add("Plateau State");
        list2.add("Rivers State");
        list2.add("Sokoto State");
        list2.add("Taraba State");
        list2.add("Yobe State");
        list2.add("Zamfara State");
    }

    private void setSpinnerText(Spinner spin, String txt) {
        for (int o = 0; o < spin.getAdapter().getCount(); o++) {
            if (spin.getAdapter().getItem(o).toString().contains(txt)) {
                spin.setSelection(o);
            }
        }
    }
}