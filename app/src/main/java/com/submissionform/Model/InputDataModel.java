package com.submissionform.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "InputDataModel")
public class InputDataModel {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "full_name")
    public String fullName;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "phone")
    public String phone;

    @ColumnInfo(name = "loanNotes")
    public String loanNotes;

    @ColumnInfo(name = "applicationStatus")
    public String applicationStatus;

    @ColumnInfo(name = "referralSourceOther")
    public String referralSourceOther;

    @ColumnInfo(name = "whetherReferralSourceOther")
    public Boolean whetherReferralSourceOther;

    @ColumnInfo(name = "referralSource")
    public String referralSource;

    @ColumnInfo(name = "leadCreated")
    public String leadCreated;
}
