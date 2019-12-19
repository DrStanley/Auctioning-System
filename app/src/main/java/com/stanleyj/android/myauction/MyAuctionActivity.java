package com.stanleyj.android.myauction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.FirebaseException;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MyAuctionActivity extends AppCompatActivity {
    RecyclerView mrecyclerView;
    FirebaseDatabase mfirebaseDatabase;
    DatabaseReference mdatabaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser use;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_auction);
        pd = new ProgressDialog(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Auctions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        use = firebaseAuth.getCurrentUser();


//        RecyclerVeiw
        mrecyclerView = (RecyclerView) findViewById(R.id.recyclerView3);
        mrecyclerView.setHasFixedSize(true);

//        set layout as LinearLayout
        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        send query to firebase
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mdatabaseReference = mfirebaseDatabase.getReference("Auctions");


    }


    //    loads data into the recyclerview
    @Override
    protected void onStart() {
        pd.setMessage(" Loading Data Please wait...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        super.onStart();

//        System.out.println("gooo2 " + use.getUid() + " or " + firebaseAuth.getUid());
        final Query firebaseQuery = mdatabaseReference.orderByChild("userID").startAt(firebaseAuth.getUid()).
                endAt(firebaseAuth.getUid() + "\uf8ff");

        firebaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    Toast.makeText(MyAuctionActivity.this, "No item found", Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        FirebaseRecyclerAdapter<AuctonDownloadModel, ViewHolder2> firebaseRecyclerAdapter3 =
                new FirebaseRecyclerAdapter<AuctonDownloadModel, ViewHolder2>(
                        AuctonDownloadModel.class,
                        R.layout.row2,
                        ViewHolder2.class,
                        firebaseQuery
                ) {

                    @Override
                    protected void populateViewHolder(ViewHolder2 viewHolder, AuctonDownloadModel model, int position) {
                        try {

//                            Toast.makeText(MyAuctionActivity.this, "here is "+firebaseAuth.getUid(), Toast.LENGTH_SHORT).show();
                            viewHolder.setDetail(getApplicationContext(), model.getProductName(), model.getCategory(),
                                    model.getImage1(), model.getImage2(), model.getImage3(),
                                    model.getDescription(), model.getProdID(), firebaseAuth.getUid(), model.getUserID(),
                                    model.getStartday(), model.getStartmonth(),
                                    model.getStartyear(), model.getStarthour(), model.getStartminute(), model.getEndday(),
                                    model.getEndmonth(), model.getEndyear(), model.getEndhour(), model.getEndminute(),
                                    model.getCurrentBid(), model.getProgress());
                            pd.dismiss();

                        } catch (FirebaseException e) {
                            pd.dismiss();
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
                                nPrice = nPrice.replace("₦", "");
                                Intent intent = new Intent(view.getContext(), AuctionDetailActivity.class);

                                intent.putExtra("pname", pName.getText().toString());
                                intent.putExtra("pcategory", pCategory.getText().toString());
                                intent.putExtra("prodid", proid.getText().toString());
                                intent.putExtra("pDescription", pDescription.getText().toString());
                                intent.putExtra("Sdate", Sdate.getText().toString());
                                intent.putExtra("Edate", Edate.getText().toString());
                                intent.putExtra("owner", owner.getText().toString());
                                intent.putExtra("curbid", nPrice);
                                intent.putExtra("mode", "bad");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        inflate the menu items
        getMenuInflater().inflate(R.menu.myauction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("Exit", true);
                startActivity(i);
                return true;
            case R.id.add_auction:
                startActivity(new Intent(getApplicationContext(), AddNewAuctionActivity.class));
                return true;
            case R.id.exit:
                FirebaseAuth.getInstance().signOut();
                Intent w = new Intent(getApplicationContext(), LoginActivity.class);
                w.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                w.putExtra("Exit", true);
                startActivity(w);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
