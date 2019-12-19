package com.stanleyj.android.myauction;

/**
 * Created by Stanley on 24/11/2018.
 */
public class UserInformation {
    String name;
    String address;
    String DOB;
    String occupation;
    String state;
    String phone_num;
    String image;
    String nin;

    public UserInformation() {

    }

    public UserInformation(
            String name,
            String address,
            String DOB,
            String occupation,
            String phone_num,
            String state,
            String image,
            String NIN
    ) {
        this.name = name;
        this.nin = NIN;
        this.state = state;
        this.address = address;
        this.DOB = DOB;
        this.image = image;
        this.phone_num = phone_num;
        this.occupation = occupation;
    }
}