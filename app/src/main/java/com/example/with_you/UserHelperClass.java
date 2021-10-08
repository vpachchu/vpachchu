package com.example.with_you;

import android.widget.EditText;

public class UserHelperClass {
    String personName, personKeyword, personUserName, personMob01,personMob02,personMob03;

    public UserHelperClass()
    {
    }

    public UserHelperClass(String personName, String personKeyword, String personUserName, String personMob01, String personMob02, String personMob03) {
        this.personName = personName;
        this.personKeyword = personKeyword;
        this.personUserName = personUserName;
        this.personMob01 = personMob01;
        this.personMob02 = personMob02;
        this.personMob03 = personMob03;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonKeyword() {
        return personKeyword;
    }

    public void setPersonKeyword(String personKeyword) {
        this.personKeyword = personKeyword;
    }

    public String getPersonUserName() {
        return personUserName;
    }

    public void setPersonUserName(String personUserName) {
        this.personUserName = personUserName;
    }

    public String getPersonMob01() {
        return personMob01;
    }

    public void setPersonMob01(String personMob01) {
        this.personMob01 = personMob01;
    }

    public String getPersonMob02() {
        return personMob02;
    }

    public void setPersonMob02(String personMob02) {
        this.personMob02 = personMob02;
    }

    public String getPersonMob03() {
        return personMob03;
    }

    public void setPersonMob03(String personMob03) {
        this.personMob03 = personMob03;
    }
}
