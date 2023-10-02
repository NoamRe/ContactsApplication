package com.example.contactsapplication.database;

import android.provider.BaseColumns;

public final class ContactsApplicationContract {
    private ContactsApplicationContract() {
    }

    public static class Contacts implements BaseColumns {
        public static final String TABLE_NAME = "Contacts";
        public static final String COLUMN_NAME_FirstName = "FirstName";
        public static final String COLUMN_NAME_LastName = "LastName";
        public static final String COLUMN_NAME_Gender = "Gender";
        public static final String COLUMN_NAME_PhoneNumber = "PhoneNumber";
        public static final String COLUMN_NAME_DateOfBirth = "DateOfBirth";
        public static final String COLUMN_NAME_PhoneType = "PhoneType";
        public static final String COLUMN_NAME_Email = "Email";
        public static final String COLUMN_NAME_Address = "Address";
        public static final String COLUMN_NAME_City = "City";
    }

    public static class Users implements BaseColumns {
        public static final String TABLE_NAME = "Users";
        public static final String COLUMN_NAME_Username = "Username";
        public static final String COLUMN_NAME_Password = "Password";
    }

    public static class UsersContacts implements BaseColumns {
        public static final String TABLE_NAME = "UsersContacts";
        public static final String COLUMN_NAME_UserID = "UserID";
        public static final String COLUMN_NAME_ContactID = "ContactID";
    }
}
