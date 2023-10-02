package com.example.contactsapplication.genderapi;

import com.google.gson.annotations.SerializedName;

public class GenderApiResponse {
    @SerializedName("count")
    private int m_Count;
    @SerializedName("name")
    private String m_Name;
    @SerializedName("gender")
    private String m_Gender;
    @SerializedName("probability")
    private double m_Probability;

    public String GetGender() {
        return m_Gender;
    }
}
