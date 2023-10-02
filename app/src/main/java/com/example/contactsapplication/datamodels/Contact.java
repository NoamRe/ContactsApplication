package com.example.contactsapplication.datamodels;

public class Contact {
    private String m_FirstName;
    private String m_LastName;
    private String m_Gender;
    private String m_PhoneNumber;
    private String m_PhoneType;
    private String m_Address;
    private String m_City;
    private String m_Email;
    private String m_DateOfBirth;

    public Contact(String i_FirstName, String i_LastName, String i_Gender, String i_PhoneNumber, String i_PhoneType, String i_Address,
                   String i_City, String i_Email, String i_DateOfBirth) {
        this.m_FirstName = i_FirstName;
        this.m_LastName = i_LastName;
        this.m_Gender = i_Gender;
        this.m_PhoneNumber = i_PhoneNumber;
        this.m_PhoneType = i_PhoneType;
        this.m_Address = i_Address;
        this.m_City = i_City;
        this.m_Email = i_Email;
        this.m_DateOfBirth = i_DateOfBirth;
    }

    public String GetFirstName() {
        return m_FirstName;
    }

    public String GetLastName() {
        return m_LastName;
    }

    public String GetGender() {
        return m_Gender;
    }

    public String GetPhoneNumber() {
        return m_PhoneNumber;
    }

    public String GetPhoneType() {
        return m_PhoneType;
    }

    public String GetAddress() {
        return m_Address;
    }

    public String GetCity() {
        return m_City;
    }

    public String GetEmail() {
        return m_Email;
    }

    public String GetDateOfBirth() {
        return m_DateOfBirth;
    }

    public void SetFirstName(String i_FirstName) {
        m_FirstName = i_FirstName;
    }

    public void SetLastName(String i_LastName) {
        m_LastName = i_LastName;
    }

    public void SetGender(String i_Gender) {
        m_Gender = i_Gender;
    }

    public void SetPhoneNumber(String i_PhoneNumber) {
        m_PhoneNumber = i_PhoneNumber;
    }

    public void SetPhoneType(String i_PhoneType) {
        m_PhoneType = i_PhoneType;
    }

    public void SetAddress(String i_Address) {
        m_Address = i_Address;
    }

    public void SetCity(String i_City) {
        m_City = i_City;
    }

    public void SetEmail(String i_Email) {
        m_Email = i_Email;
    }

    public void SetDateOfBirth(String i_DateOfBirth) {
        m_DateOfBirth = i_DateOfBirth;
    }
}
