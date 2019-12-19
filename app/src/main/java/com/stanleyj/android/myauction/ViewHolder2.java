package com.stanleyj.android.myauction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Stanley on 2019/06/24.
 */
public class ViewHolder2 extends RecyclerView.ViewHolder {
    View mView;
    private ViewHolder2.ClickListener mClickListener;

    public ViewHolder2(View itemView) {
        super(itemView);

        mView = itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());

                return true;
            }
        });
    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    //    set details to recycler view row
    public void setDetail(Context ctx, String productsName, String category,
                          String image1, String image2, String image3,
                          String description, String proID, String usd, String ownerID, int startday, int startmonth,
                          int startyear, int starthour, int startminute,
                          int endday, int endmonth, int endyear,
                          int endhour, int endminute, int currentBid, String progress) {
//        get views
        TextView pName = (TextView) mView.findViewById(R.id.pName2);
        TextView pCategory = (TextView) mView.findViewById(R.id.pCategory2);
        TextView proid = (TextView) mView.findViewById(R.id.proID2);
        TextView gress = (TextView) mView.findViewById(R.id.progress2);
        TextView curBid = (TextView) mView.findViewById(R.id.curBid2);
        TextView pDescription = (TextView) mView.findViewById(R.id.pDescription2);
        TextView Sdate = (TextView) mView.findViewById(R.id.Sdate2);
        TextView Edate = (TextView) mView.findViewById(R.id.Edate2);
        TextView userID = (TextView) mView.findViewById(R.id.USD2);
        ImageView rImage1 = (ImageView) mView.findViewById(R.id.r2Image1);
        ImageView rImage2 = (ImageView) mView.findViewById(R.id.r2Image2);
        ImageView rImage3 = (ImageView) mView.findViewById(R.id.r2Image3);


        try {

            pName.setText(productsName);
            pCategory.setText(category);
            gress.setText(progress);
            if (progress.equals("rejected")) {
                gress.setTextColor(Color.RED);
            }
            userID.setText(usd);
            proid.setText(proID);
            curBid.setText("Current Bid ₦" + currentBid);
            pDescription.setText(description);
            Sdate.setText("StartDate: " + startday + "/" + startmonth + "/" + startyear
                    + "\t\t\t\t" + starthour + ":" + startminute);
            Edate.setText("EndDate: " + endday + "/" + endmonth + "/" + endyear
                    + "\t\t\t\t" + endhour + ":" + endminute);

            rImage1.setImageBitmap(decodeFromFirebaseBase64(image1));
            rImage2.setImageBitmap(decodeFromFirebaseBase64(image2));
            rImage3.setImageBitmap(decodeFromFirebaseBase64(image3));
//            }
//            else {
//                AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
//                alertDialog.setTitle("Error Alert");
//                alertDialog.setMessage("No Ongoing Auction Yet \nPlease check Upcoming Auctions");
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", 
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//            }

        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
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

    //    set details to recycler view comp
    public void setDetail2(Context ctx, String title, String ids,
                           String reas, String compID) {
//        get views
        TextView pName = (TextView) mView.findViewById(R.id.cTitile);
        TextView pCategory = (TextView) mView.findViewById(R.id.complainId);
        TextView proid = (TextView) mView.findViewById(R.id.reason);
        TextView gress = (TextView) mView.findViewById(R.id.IDS);

        try {

            Toast.makeText(ctx, ids+" "+title, Toast.LENGTH_SHORT).show();
            pName.setText(title);
            pCategory.setText(ids);
            gress.setText(compID);
            proid.setText(reas);
        } catch (Exception e) {

            AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
            alertDialog.setTitle("Error Alert");
            alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
            alertDialog.setMessage("The following Error occurred\n" + e.getMessage());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
    }

    public void setOnClickListener(ViewHolder2.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    // interface to send call backs
    public interface ClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

}
