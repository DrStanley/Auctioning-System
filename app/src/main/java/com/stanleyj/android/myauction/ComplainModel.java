package com.stanleyj.android.myauction;

/**
 * Created by Stanley on 2019/08/17.
 */
public class ComplainModel {
    String productID;
    String complainTitle;
    String complainDetails;
    String complainer;
    String complainID;

    public ComplainModel() {
    }


    public ComplainModel(String complainID, String productID, String complainTitle, String complainDetails, String complainer) {
        this.complainID = complainID;
        this.productID = productID;
        this.complainTitle = complainTitle;
        this.complainDetails = complainDetails;
        this.complainer = complainer;
    }
}
