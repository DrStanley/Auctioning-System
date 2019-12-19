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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class RegisterActivity extends AppCompatActivity {
    EditText em, pw, cpw;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String email, pass, cpass;
    ImageButton imageButton;
    private ProgressDialog pd;

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        pd = new ProgressDialog(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarReg);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        views();

        firebaseAuth = FirebaseAuth.getInstance();
   /*error point*/
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }

    private void registerUser() {
        email = em.getText().toString();
        pass = pw.getText().toString();
        cpass = cpw.getText().toString();
        if (TextUtils.isEmpty(email)) {
            //email is empty
            Toast.makeText(this, "Please Enter email", Toast.LENGTH_SHORT).show();
            return;

        }
        if (TextUtils.isEmpty(pass)) {
            //password is empty
            Toast.makeText(this, "Please Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

//        checks if email is valid
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(cpass)) {
            Toast.makeText(this, "Please Enter Confirm password ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!cpass.equals(pass)) {
            //password match confirm password
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        pd.setMessage("Registering User...");
        pd.show();
        pd.setCanceledOnTouchOutside(false);
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, pass).
                    addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pd.dismiss();
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // email sent
                                                    final View view = findViewById(android.R.id.content);
                                                    Snackbar.make(view, "Please check your email for verification", Snackbar.LENGTH_SHORT)
                                                            .setAction(null, null)
                                                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                                            .show();
                                                    FirebaseAuth.getInstance().signOut();
                                                }
                                            }
                                        });
                                Toast.makeText(RegisterActivity.this,
                                        "Registration Successful !!!",
                                        Toast.LENGTH_SHORT).show();
                                SharedPreferences Pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor piss = Pref.edit();
                                piss.clear();
                                piss.putBoolean("Updated", false);
                                piss.putBoolean("Login", true);
                                piss.putString("Username", email);
                                piss.apply();
                                Intent z = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(z);
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                                alertDialog.setTitle("Alert");
                                alertDialog.setMessage("Couldn't  Register User \n"
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
        } catch (Exception f) {
            AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Alert " + f.getMessage() + " " + f.getCause());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

    }

    private void views() {
        em = (EditText) findViewById(R.id.email);
        pw = (EditText) findViewById(R.id.pwd);
        cpw = (EditText) findViewById(R.id.cpwd);
        imageButton = (ImageButton) findViewById(R.id.register);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

//    private boolean validateEmail(String data){
//        Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
//        Matcher emailMatcher = emailPattern.matcher(data);
//        return emailMatcher.matches();
//    }
}