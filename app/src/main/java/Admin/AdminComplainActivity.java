package Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.FirebaseException;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stanleyj.android.myauction.ComplainDownloadModel;
import com.stanleyj.android.myauction.R;
import com.stanleyj.android.myauction.ViewHolder2;

public class AdminComplainActivity extends AppCompatActivity {


    RecyclerView mrecyclerView;
    FirebaseDatabase mfirebaseDatabase;
    DatabaseReference mdatabaseReference;
    FirebaseAuth firebaseAuth;
    LinearLayoutManager linearLayoutManager;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_complain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.adminComplainBar);
        setSupportActionBar(toolbar);
        pd = new ProgressDialog(AdminComplainActivity.this);
        getSupportActionBar().setTitle("Complains");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);

//        RecyclerVeiw
        mrecyclerView = (RecyclerView) findViewById(R.id.recyclerViewC);
        mrecyclerView.setHasFixedSize(true);

//        set layout as LinearLayout
        mrecyclerView.setLayoutManager(linearLayoutManager);

//        send query to firebase
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mdatabaseReference = mfirebaseDatabase.getReference("Complains");
    }


    @Override
    protected void onStart() {
        pd.setMessage(" Loading Data Please wait...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        super.onStart();


        FirebaseRecyclerAdapter<ComplainDownloadModel, ViewHolder2> firebaseRecyclerAdapter3 =
                new FirebaseRecyclerAdapter<ComplainDownloadModel, ViewHolder2>(
                        ComplainDownloadModel.class,
                        R.layout.comp,
                        ViewHolder2.class,
                        mdatabaseReference
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder2 viewHolder, ComplainDownloadModel model, int position) {
//                        Toast.makeText(getApplicationContext(),model.getProductName()+" and "+position,Toast.LENGTH_SHORT).show();
//                        String title, String ids,
//                                String reas, String compID
                        try {
                            viewHolder.setDetail2(AdminComplainActivity.this, model.getComplainTitle(), model.getComplainer()
                                    , model.getComplainDetails(), model.getComplainID());
                            pd.dismiss();
                        } catch (FirebaseException e) {

                            AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                            alertDialog.setTitle("Error Alert");
                            alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
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


                    @Override
                    public ViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder2 viewHolder = super.onCreateViewHolder(parent, viewType);

                        viewHolder.setOnClickListener(new ViewHolder2.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                TextView pName = (TextView) view.findViewById(R.id.cTitile);
                                TextView pCategory = (TextView) view.findViewById(R.id.complainId);
                                TextView proid = (TextView) view.findViewById(R.id.reason);
                                TextView gress = (TextView) view.findViewById(R.id.IDS);
//                                Intent intent = new Intent(view.getContext(), AdminDetailedAuctionActivity.class);
//                                intent.putExtra("pname", pName.getText().toString());
//                                intent.putExtra("pcategory", pCategory.getText().toString());
//                                intent.putExtra("prodid", proid.getText().toString());
//                                startActivity(intent);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        });
                        return viewHolder;
                    }
                };
//        set adapter to recylerview
        mrecyclerView.setAdapter(firebaseRecyclerAdapter3);
    }
}
