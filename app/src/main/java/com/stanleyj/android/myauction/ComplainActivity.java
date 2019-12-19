package com.stanleyj.android.myauction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ComplainActivity extends AppCompatActivity {

    EditText prdID, title, reson;
    Button send;
    FirebaseDatabase mfirebaseDatabase;
    DatabaseReference mdatabaseReference;
    FirebaseAuth firebaseAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);
        pd = new ProgressDialog(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.complain_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Complain");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prdID = (EditText) findViewById(R.id.ComproducID);
        title = (EditText) findViewById(R.id.comTitle);
        reson = (EditText) findViewById(R.id.comdetails);
        send = (Button) findViewById(R.id.sendComplain);
        firebaseAuth = FirebaseAuth.getInstance();
        mfirebaseDatabase = FirebaseDatabase.getInstance();
//        mdatabaseReference = mfirebaseDatabase.getReference();
        mdatabaseReference = mfirebaseDatabase.getReference("Complains").child("C" + System.currentTimeMillis());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(prdID.getText().toString())) {
                    //email is empty
                    Snackbar.make(view, "Please enter the product ID you are complaining for", Snackbar.LENGTH_SHORT)
                            .setAction(null, null)
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                            .show();
                    return;

                }
                if (TextUtils.isEmpty(title.getText().toString())) {
                    //email is empty
                    Snackbar.make(view, "Please enter the Title of your complain", Snackbar.LENGTH_SHORT)
                            .setAction(null, null)
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                            .show();
                    return;

                }
                if (TextUtils.isEmpty(reson.getText().toString())) {
                    //email is empty
                    Snackbar.make(view, "Please enter the reason for your complain", Snackbar.LENGTH_SHORT)
                            .setAction(null, null)
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                            .show();
                    return;

                }


                try {
                    pd.setMessage("Sending Please wait...");
                    pd.show();
                    ComplainModel CI = new ComplainModel("C" + System.currentTimeMillis(), prdID.getText().toString(),
                            title.getText().toString(), reson.getText().toString(), firebaseAuth.getUid());
                    mdatabaseReference.setValue(CI).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pd.dismiss();
                            Snackbar.make(findViewById(android.R.id.content), "Complain Sent", Snackbar.LENGTH_SHORT)
                                    .setAction(null, null)
                                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                    .show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            AlertDialog alertDialog = new AlertDialog.Builder(ComplainActivity.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);

                            alertDialog.setMessage("Error Sending Complain\nError: " + e.getMessage() + "\nCause: " + e.getCause());
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
                    AlertDialog alertDialog = new AlertDialog.Builder(ComplainActivity.this).create();
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

            }
        });
    }
}
