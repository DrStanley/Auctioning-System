package com.stanleyj.android.myauction;

/**
 * Created by Stanley on 2019/06/21.
 */
public class AuctionModel {
    String productName;
    String category;
    String image1;
    String image2;
    String image3;
    String description;
    String userID;
    String winnerID;
    String prodID;
    String status;
    String progress;
    int startday, startmonth, startyear, starthour, startminute,
            endday, endmonth, endyear, endhour, endminute;

    int setBid;
    int currentBid;

    public AuctionModel() {
    }

    public AuctionModel(String productName, String category, String image1, String image2, String image3,
                        String description, int startday, int startmonth, int startyear,
                        int starthour, int startminute, int endday, int endmonth, int endyear,
                        int endhour, int endminute, String userID, String prodID, String winnerID, int setBid,
                        int currentBid,String status, String progress) {
        this.productName = productName;
        this.category = category;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.description = description;
        this.startday = startday;
        this.startmonth = startmonth;
        this.startyear = startyear;
        this.starthour = starthour;
        this.startminute = startminute;
        this.endday = endday;
        this.endmonth = endmonth;
        this.endyear = endyear;
        this.endhour = endhour;
        this.endminute = endminute;
        this.userID = userID;
        this.prodID = prodID;
        this.winnerID = winnerID;
        this.setBid = setBid;
        this.status = status;
        this.progress = progress;
        this.currentBid = currentBid;
    }

}
