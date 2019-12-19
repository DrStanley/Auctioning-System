package com.stanleyj.android.myauction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {
    EditText editText;
    Button rest;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        editText = (EditText) findViewById(R.id.f_email);
        pd = new ProgressDialog(this);
        rest = (Button) findViewById(R.id.reset);
        rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editText.toString())) {
                    //email is empty
                    Toast.makeText(getApplicationContext(), "Please Enter email", Toast.LENGTH_SHORT).show();
                    return;

                }
                pd.setMessage("Resetting User Password...");
                pd.show();
                pd.setCanceledOnTouchOutside(false);
                FirebaseAuth.getInstance().sendPasswordResetEmail(editText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                pd.dismiss();
                                if (task.isSuccessful()) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(ForgotActivity.this).create();
                                    alertDialog.setTitle("Alert");
                                    alertDialog.setMessage("Reset process Successful!!\nPlease check your Email");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();
                                } else {
                                    AlertDialog alertDialog = new AlertDialog.Builder(ForgotActivity.this).create();
                                    alertDialog.setTitle("Alert");
                                    alertDialog.setMessage("Couldn't Reset  Password \n"
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
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
