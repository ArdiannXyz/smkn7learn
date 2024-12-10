package com.example.smk7.Guru.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class BankTugasModel implements Parcelable {

    private String nama;
    private String status;

    // Constructor
    public BankTugasModel(String nama, String status) {
        this.nama = nama;
        this.status = status;
    }

    // Getter methods
    public String getNama() {
        return nama;
    }

    public String getStatus() {
        return status;
    }

    // Implementasi Parcelable
    protected BankTugasModel(Parcel in) {
        nama = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nama);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BankTugasModel> CREATOR = new Creator<BankTugasModel>() {
        @Override
        public BankTugasModel createFromParcel(Parcel in) {
            return new BankTugasModel(in);
        }

        @Override
        public BankTugasModel[] newArray(int size) {
            return new BankTugasModel[size];
        }
    };
}
