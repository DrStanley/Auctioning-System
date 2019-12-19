package com.stanleyj.android.myauction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Admin.AdminActivity;

public class LoginActivity extends AppCompatActivity {
    EditText editTextEm, editTextPa;
    String em, pa;
    Button login;
    TextView paswrd, regis;
    FirebaseDatabase mfirebaseDatabase;
    DatabaseReference mdatabaseReference;
    FirebaseAuth firebaseAuth;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getIntent().getBooleanExtra("Exit", false)) {
            System.out.println();
            finish();
        }
//        Shared preference
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        Shared preference to get the extra data stored;
                SharedPreferences.Editor editor = sharedPref.edit();
                boolean check_log = sharedPref.getBoolean("Login", false);
                String piss = sharedPref.getString("Username", null);
                editor.apply();
                System.out.println(check_log + "here 3" + piss);

//        getSupportActionBar().setTitle("Login Page");
                editTextEm = (EditText) findViewById(R.id.usn);
                pd = new ProgressDialog(LoginActivity.this);
                editTextPa = (EditText) findViewById(R.id.lpwd);
                login = (Button) findViewById(R.id.log);
                paswrd = (TextView) findViewById(R.id.f_pass);
                regis = (TextView) findViewById(R.id.regAct);
                firebaseAuth = FirebaseAuth.getInstance();

                if (check_log) {
                    editTextEm.setText(piss);
                }
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LoginUser();
            }
        });
        paswrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ForgotActivity.class);
                startActivity(i);
            }
        });
        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });
    }


    private void LoginUser() {
        em = editTextEm.getText().toString();
        pa = editTextPa.getText().toString();
        if (TextUtils.isEmpty(em)) {
            //email is empty
            Toast.makeText(this, "Please Enter email", Toast.LENGTH_SHORT).show();
            return;

        }
        if (TextUtils.isEmpty(pa)) {
            //passwordc is empty
            Toast.makeText(this, "Please Enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        pd.setMessage("Logging User in...");
        pd.show();
        pd.setCanceledOnTouchOutside(false);

        final View view = findViewById(android.R.id.content);
        firebaseAuth.signInWithEmailAndPassword(em, pa).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
//                    Toast.makeText(LoginActivity.this, "Login Successfull !!!", Toast.LENGTH_SHORT).show();
//                    Shared preference
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()) {
                        editTextPa.setText(null);
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        Shared preference to get the extra data stored;
                        SharedPreferences.Editor editor = sharedPref.edit();
                        final boolean check_update = sharedPref.getBoolean("Updated", false);
                        editor.putBoolean("Login", true);
                        editor.putBoolean("Updated", true);
                        editor.putString("Username", em);
                        editor.apply();

                        mfirebaseDatabase = FirebaseDatabase.getInstance();
                        mdatabaseReference = mfirebaseDatabase.getReference();
                        mdatabaseReference = mfirebaseDatabase.getReference("Users").child(firebaseAuth.getUid());
                        mdatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("type")) {
                                    String admin_check = dataSnapshot.child("type").getValue(String.class);
                                    if (admin_check.equals("admin")) {
                                        startActivity(new Intent(getApplicationContext(), AdminActivity.class).putExtra("email", em));
                                        pd.dismiss();
                                        Snackbar.make(view, "Login Successful !!!", Snackbar.LENGTH_LONG)
                                                .setAction(null, null)
                                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                                .show();

                                    }
                                } else if (check_update) {
                                    startActivity(new Intent(getApplicationContext(), AuctionActivity.class).putExtra("email", em));
                                    pd.dismiss();
                                    Snackbar.make(view, "Login Successful !!!", Snackbar.LENGTH_LONG)
                                            .setAction(null, null)
                                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                            .show();

                                } else {
                                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class).putExtra("email", em));
                                    pd.dismiss();
                                    Snackbar.make(view, "Login Successful !!!", Snackbar.LENGTH_LONG)
                                            .setAction(null, null)
                                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                            .show();

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }else {
                        pd.dismiss();
                        // email sent
                        final View view = findViewById(android.R.id.content);
                        Snackbar.make(view, "Please verify your email", Snackbar.LENGTH_SHORT)
                                .setAction(null, null)
                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                .show();
                    }


                } else {
                    pd.dismiss();
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
                    alertDialog.setMessage("Couldn't Login User \n"
                            + task.getException().getMessage());
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
