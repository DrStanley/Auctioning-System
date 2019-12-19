package Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.stanleyj.android.myauction.AuctonDownloadModel;
import com.stanleyj.android.myauction.R;
import com.stanleyj.android.myauction.ViewHolder2;

public class Admin_All_Auction_Activity extends AppCompatActivity {

    RecyclerView mrecyclerView;
    FirebaseDatabase mfirebaseDatabase;
    DatabaseReference mdatabaseReference;
    FirebaseAuth firebaseAuth;
    LinearLayoutManager linearLayoutManager;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__all__auction_);
        pd = new ProgressDialog(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.adminalltoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All AUCTIONS");
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
        mrecyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        mrecyclerView.setHasFixedSize(true);

//        set layout as LinearLayout
        mrecyclerView.setLayoutManager(linearLayoutManager);

//        send query to firebase
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mdatabaseReference = mfirebaseDatabase.getReference("Auctions");
    }

    @Override
    protected void onStart() {
        pd.setMessage(" Loading Data Please wait...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        super.onStart();


        FirebaseRecyclerAdapter<AuctonDownloadModel, ViewHolder2> firebaseRecyclerAdapter3 =
                new FirebaseRecyclerAdapter<AuctonDownloadModel, ViewHolder2>(
                        AuctonDownloadModel.class,
                        R.layout.row2,
                        ViewHolder2.class,
                        mdatabaseReference
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder2 viewHolder, AuctonDownloadModel model, int position) {
//                        Toast.makeText(getApplicationContext(),model.getProductName()+" and "+position,Toast.LENGTH_SHORT).show();

                        try {
                            viewHolder.setDetail(Admin_All_Auction_Activity.this, model.getProductName(), model.getCategory(),
                                    model.getImage1(), model.getImage2(), model.getImage3(),
                                    model.getDescription(), model.getProdID(), firebaseAuth.getUid(), model.getUserID(),
                                    model.getStartday(), model.getStartmonth(),
                                    model.getStartyear(), model.getStarthour(), model.getStartminute(), model.getEndday(),
                                    model.getEndmonth(), model.getEndyear(), model.getEndhour(), model.getEndminute(),
                                    model.getCurrentBid(), model.getProgress());
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
                                TextView pName = (TextView) view.findViewById(R.id.pName2);
                                TextView pCategory = (TextView) view.findViewById(R.id.pCategory2);
                                TextView proid = (TextView) view.findViewById(R.id.proID2);
                                TextView curBid = (TextView) view.findViewById(R.id.curBid2);
                                TextView pDescription = (TextView) view.findViewById(R.id.pDescription2);
                                TextView Sdate = (TextView) view.findViewById(R.id.Sdate2);
                                TextView Edate = (TextView) view.findViewById(R.id.Edate2);
                                TextView owner = (TextView) view.findViewById(R.id.USD2);
                                //getting data from views
                                String nPrice = curBid.getText().toString();
                                Intent intent = new Intent(view.getContext(), AdminDetailedAuctionActivity.class);
                                intent.putExtra("pname", pName.getText().toString());
                                intent.putExtra("pcategory", pCategory.getText().toString());
                                intent.putExtra("prodid", proid.getText().toString());
                                intent.putExtra("pDescription", pDescription.getText().toString());
                                intent.putExtra("Sdate", Sdate.getText().toString());
                                intent.putExtra("Edate", Edate.getText().toString());
                                intent.putExtra("owner", owner.getText().toString());
                                startActivity(intent);
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
