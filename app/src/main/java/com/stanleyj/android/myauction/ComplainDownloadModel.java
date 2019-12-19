package com.stanleyj.android.myauction;

/**
 * Created by Stanley on 2019/08/19.
 */
public class ComplainDownloadModel {
    String productID;
    String complainTitle;
    String complainDetails;
    String complainer;
    String complainID;
    public ComplainDownloadModel() {
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getComplainTitle() {
        return complainTitle;
    }

    public void setComplainTitle(String complainTitle) {
        this.complainTitle = complainTitle;
    }

    public String getComplainDetails() {
        return complainDetails;
    }

    public void setComplainDetails(String complainDetails) {
        this.complainDetails = complainDetails;
    }

    public String getComplainer() {
        return complainer;
    }

    public void setComplainer(String complainer) {
        this.complainer = complainer;
    }

    public String getComplainID() {
        return complainID;
    }

    public void setComplainID(String complainID) {
        this.complainID = complainID;
    }
}
