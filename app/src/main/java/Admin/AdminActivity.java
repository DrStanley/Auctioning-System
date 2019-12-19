package Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.stanleyj.android.myauction.R;

public class AdminActivity extends AppCompatActivity {

    TextView adem;
    LinearLayout linearLayout1,linearLayout3,linearLayout4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.admin_bar);
        adem = (TextView)findViewById(R.id.ademail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin");
        linearLayout1 = (LinearLayout)findViewById(R.id.confirm_it);
        linearLayout3 = (LinearLayout)findViewById(R.id.pay_it);
        linearLayout4 = (LinearLayout)findViewById(R.id.complain_it);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String AdminUSN = getIntent().getStringExtra("email");
        adem.setText("Admin: "+AdminUSN);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Admin_All_Auction_Activity.class));
            }
        });
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminActivity.this, "Pay auction", Toast.LENGTH_SHORT).show();
            }
        });
        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminComplainActivity.class));
            }
        });
    }
}
