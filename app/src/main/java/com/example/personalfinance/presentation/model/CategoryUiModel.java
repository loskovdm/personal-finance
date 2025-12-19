package com.example.personalfinance.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryUiModel implements Parcelable {

    private int id;
    private String name;

    public CategoryUiModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected CategoryUiModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Parcelable.Creator<CategoryUiModel> CREATOR = new Creator<CategoryUiModel>() {
        @Override
        public CategoryUiModel createFromParcel(Parcel in) {
            return new CategoryUiModel(in);
        }

        @Override
        public CategoryUiModel[] newArray(int size) {
            return new CategoryUiModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
}
