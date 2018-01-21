package com.example.bhargav.customalertdialog;

/**
 * Created by Bhargav on 11/13/2017.
 */

public class PasswordCriteria {

    private Boolean lower;
    private Boolean upper;
    private Boolean number;
    private Boolean special;

    private int length;

    private String specialCharacters;

    public PasswordCriteria(Boolean lower, Boolean upper, Boolean number, Boolean special, int length, String specialCharacters) {
        this.lower = lower;
        this.upper = upper;
        this.number = number;
        this.special = special;
        this.length = length;
        this.specialCharacters = specialCharacters;
    }

    public Boolean getLower() {
        return this.lower;
    }

    public Boolean getUpper() {
        return this.upper;
    }

    public Boolean getnumber() {
        return this.number;
    }

    public Boolean getSpecial() {
        return this.special;
    }

    public int getLength() {
        return this.length;
    }

    public String getSpecialCharacters() {
        return this.specialCharacters;
    }
}
