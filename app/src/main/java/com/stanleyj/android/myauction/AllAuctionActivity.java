package com.stanleyj.android.myauction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.FirebaseException;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;

public class AllAuctionActivity extends AppCompatPreferenceActivity {

    RecyclerView mrecyclerView;
    FirebaseDatabase mfirebaseDatabase;
    DatabaseReference mdatabaseReference;
    LinearLayoutManager linearLayoutManager;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(this);
        setContentView(R.layout.activity_all_auction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("AUCTIONS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        mrecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mrecyclerView.setHasFixedSize(true);


//        set layout as LinearLayout
        mrecyclerView.setLayoutManager(linearLayoutManager);

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

        final Query firebaseQuery = mdatabaseReference.orderByChild("progress").startAt("confirmed").
                endAt("confirmed" + "\uf8ff");
        firebaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    Toast.makeText(AllAuctionActivity.this, "No item found", Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<AuctonDownloadModel, ViewHolder> firebaseRecyclerAdapter3 =
                new FirebaseRecyclerAdapter<AuctonDownloadModel, ViewHolder>(
                        AuctonDownloadModel.class,
                        R.layout.row,
                        ViewHolder.class,
                        firebaseQuery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, AuctonDownloadModel model, int position) {

                        try {
                            viewHolder.setDetails(getApplicationContext(), model.getProductName(), model.getCategory(),
                                    model.getImage1(), model.getImage2(), model.getImage3(),
                                    model.getDescription(), model.getProdID(), model.getUserID(), model.getStartday(), model.getStartmonth(),
                                    model.getStartyear(), model.getStarthour(), model.getStartminute(), model.getEndday(),
                                    model.getEndmonth(), model.getEndyear(), model.getEndhour(), model.getEndminute(),
                                    model.getCurrentBid());
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
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                TextView pName = (TextView) view.findViewById(R.id.pName);
                                TextView pCategory = (TextView) view.findViewById(R.id.pCategory);
                                TextView proid = (TextView) view.findViewById(R.id.proID);
                                TextView curBid = (TextView) view.findViewById(R.id.curBid);
                                TextView pDescription = (TextView) view.findViewById(R.id.pDescription);
                                TextView Sdate = (TextView) view.findViewById(R.id.Sdate);
                                TextView Edate = (TextView) view.findViewById(R.id.Edate);
                                TextView owner = (TextView) view.findViewById(R.id.USD);
                                ImageView rImage1 = (ImageView) view.findViewById(R.id.rImage1);
                                ImageView rImage2 = (ImageView) view.findViewById(R.id.rImage2);
                                ImageView rIage3 = (ImageView) view.findViewById(R.id.rImage3);

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

    public String encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        return imageEncoded;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
